package fjwa.service;

import click.rmx.debug.OnlineBugger;
import click.rmx.debug.RMXException;
import fjwa.model.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractEntityService<E extends IEntity> implements EntityService<E> {
	private static final int STD_DB_UPDATE_TIME = 5;
	private List<E> entities = new ArrayList<>();
	private HashMap<Integer, Thread> threads = new HashMap<>();

	protected final int PUSH_THREAD = 0, PULL_THREAD = 1, NEW_THREAD = -1;

	protected abstract JpaRepository<E, Long> repository();

//	@Override
	@Transactional
	protected List<E> pullData() {
		runOnAfterThread(() -> {
			List<E> newList = repository().findAll();
			if (newList != null)
				this.entities = newList;
			this.lastUpdate = Instant.now();
		}, PULL_THREAD);
		return this.getEntities();
	}

	@Override
	public String getErrors() {
		return OnlineBugger.getInstance().getErrorHtml();
	}
//	ThreadGroup threadGroup = new ThreadGroup(this.getClass().getSimpleName());



	@Override
	@Transactional
	public E save(E entity) {
		this.entities.add(entity);
		runOnAfterThread(
				() -> repository().save(entity),
				e -> entities.remove(entity),
				PUSH_THREAD);
		return entity;
	}


	protected Thread runMethodOnThread(Runnable runnable) {
		return runMethodOnThread(runnable, null);
	}

	protected Thread runOnAfterThread(Runnable runnable, int threadId)
	{
		return runOnAfterThread(runnable, null, threadId);
	}

	protected Thread runOnAfterThread(Runnable runnable, Exceptional failAction, int threadId)
	{
		Thread thread = threads.getOrDefault(threadId,new Thread());
		if (thread != null && thread.isAlive())
			try {
				thread.join();
			} catch (InterruptedException e) {
				if (failAction != null)
					failAction.run(e);
				else
					this.addError(RMXException.unexpected(e));
			}
		return threads.put(threadId, runMethodOnThread(runnable,failAction));
	}

	interface Exceptional {
		void run(Exception e);
	}
	protected Thread runMethodOnThread(Runnable runnable, Exceptional failAction)
	{
		Thread newThread =  new Thread(() -> {
			try {
				runnable.run();
			} catch (Exception e) {
				if (failAction != null)
					failAction.run(e);
				else
					this.addError(RMXException.unexpected(e));
			}
		});
		newThread.run();
		return newThread;
	}

	@Override
	@Transactional
	public boolean removeOne(E entity) {
		if (entities.contains(entity)) {
			entities.remove(entity);
			runOnAfterThread(
					() -> repository().delete(entity),
					e -> addError(RMXException.unexpected(e, "Failed to remove entity from list and table")),
					PUSH_THREAD
			);
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public List<E> removeAll() {
		return removeIf(e -> true);
	}

	@Override
	@Transactional
	public List<E> removeIf(Predicate<E> predicate)
	{
		List<E> toRemove = new ArrayList<>();
		for (E entity : getEntities()) {
			if (predicate.test(entity))
				toRemove.add(entity);
		}
		runOnAfterThread(() -> toRemove.forEach(ent -> {
				try {
					repository().delete(ent);
				} catch (Exception e) {
					addError(RMXException.unexpected(e));
				}
			}), PUSH_THREAD);
		return getEntities(0);
	}

	public RMXException addError(RMXException e) {
		if (e == null)
			return e;
//		if (e.isSerious())
			OnlineBugger.getInstance().addHtml(e.html());
//		else
//			this.errorLog += "<br/>" + e.html();
		return e;
	}

//	@Override
	@Transactional
	@Deprecated
	private void pushData() {
		try {
			for (E entity : getEntities()) {
				try {
					repository().save(entity);//.pushData(entity);
				} catch (Exception e) {
					addError(RMXException.unexpected(e,"Could not sync " + entity));
				}
			}
		} catch (Exception e) {
			this.addError(RMXException.unexpected(e));
		}
	}

	/**
	 * @return the entities
	 */
	public List<E> getEntities() {
		return entities != null ? entities : pullData();
	}

	private Instant lastUpdate = Instant.now();

	@Override
	@Transactional
	public List<E> getEntities(int secondsBetweenDBCheck) {
		if (Duration.between(lastUpdate, Instant.now()).getSeconds() > secondsBetweenDBCheck)
			this.pullData();
		return this.getEntities();
	}
}
