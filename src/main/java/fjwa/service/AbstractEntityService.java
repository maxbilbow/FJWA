package fjwa.service;

import click.rmx.debug.OnlineBugger;
import click.rmx.debug.RMXException;
import click.rmx.threads.RMXThreadMap;
import fjwa.model.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractEntityService<E extends IEntity> implements EntityService<E> {
    private static final int STD_DB_UPDATE_TIME = 5;
    private List<E> entities = new ArrayList<>();
    protected RMXThreadMap<Integer> threads =
            new RMXThreadMap<>(
                    null,
                    (t,s) -> OnlineBugger.getInstance().addLog(t.getName() + " completed: " + s));

    protected final int NEW_THREAD = -1, PUSH_THREAD = 2, PULL_THREAD = 1, PULL_DATA = 2;

    protected abstract JpaRepository<E, Long> repository();

    @Override
    @Transactional
    public Thread pullData() {

        Thread thread = threads.getOrDefault(PULL_DATA, new Thread());
        if (!thread.isAlive() || thread.getName() != "pullData")
           thread = threads.runOnAfterThread(() -> {
                        List<E> newList = repository().findAll();
                        if (newList != null)
                            this.entities = newList;
                        this.lastUpdate = Instant.now();
                    },
                    e -> OnlineBugger.getInstance().addException(e), PULL_DATA);
        thread.setName("pullData");
        return thread;
//
//
//            threads.put(PULL_DATA, threads.runOnNewThread(() -> {
//                        List<E> newList = repository().findAll();
//                        if (newList != null)
//                            this.entities = newList;
//                        this.lastUpdate = Instant.now();
//                    })
//            );
    }


//	ThreadGroup threadGroup = new ThreadGroup(this.getClass().getSimpleName());


    @Override
    @Transactional
    public E save(E entity) {
        this.entities.add(entity);
        threads.runOnAfterThread(
                () -> repository().save(entity),
                e -> entities.remove(entity),
                PUSH_THREAD).setName("save: " + entity);
        return entity;
    }


    @Override
    @Transactional
    public boolean removeOne(E entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
            threads.runOnAfterThread(
                    () -> repository().delete(entity),
                    e -> addError(RMXException.unexpected(e, "Failed to remove entity from list and table")),
                    PUSH_THREAD
            ).setName("removeOne: " + entity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<E> removeIf(Predicate<E> predicate) {
        List<E> toRemove = new ArrayList<>();
        for (E entity : getEntities()) {
            if (predicate.test(entity))
                toRemove.add(entity);
        }
        threads.runOnAfterThread(
                () -> toRemove.forEach(ent -> {
                    try {
                        repository().delete(ent);
                    } catch (Exception e) {
                        addError(RMXException.unexpected(e));
                    }
                }),
                e -> addError(RMXException.unexpected(e)), PUSH_THREAD)
        .setName("removeIf");
        return getEntities(0);
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
                    addError(RMXException.unexpected(e, "Could not sync " + entity));
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
        return entities;
    }

    private Instant lastUpdate = Instant.now();

    @Override
    public Instant getLastUpdate() {
        return lastUpdate;
    }


    @Override
    @Transactional
    public void addNew(E entity) {
        this.getEntities().add(entity);
        threads.runOnAfterThread(
                () -> repository().save(entity),
                e -> OnlineBugger.getInstance().addException("Failed to add " + entity),
                PUSH_THREAD).setName("addNew: " + entity);
    }
}
