package fjwa.service;

import click.rmx.debug.RMXException;
import click.rmx.debug.WebBugger;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;


public interface EntityService<E> {
//	@Deprecated
//	List<E> findAllEntities();

	List<E> getEntities();

	@Transactional
	default List<E> getEntities(int secondsBetweenDBCheck) {
		if (Duration.between(getLastUpdate(), Instant.now()).getSeconds() > secondsBetweenDBCheck)
			this.pullData();
		return this.getEntities();
	}

	E save(E entity);

	boolean removeOne(E entity);

	default String getErrors() {
		return WebBugger.getInstance().getErrorHtml();
	}

	default RMXException addError(RMXException e) {
		if (e != null)
			WebBugger.getInstance().addException(e);
		return e;
	}

	Instant getLastUpdate();




	Thread pullData();

//	void pushData();

	@Transactional
	default List<E> removeAll() {
		return removeIf(e -> true);
	}

	List<E> removeIf(Predicate<E> predicate);

	/**
	 * @Warning Will always returns null unless overridden
	 * @return null
	 */
	@Null
	default E addNew()
	{
		return null;
	}

	void addNew(E entity);
}
