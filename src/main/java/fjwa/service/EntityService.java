package fjwa.service;

import java.util.List;
import java.util.function.Predicate;


public interface EntityService<E> {
//	@Deprecated
//	List<E> findAllEntities();

	List<E> getEntities();

	List<E> getEntities(int secondsBetweenDBCheck);

//	List<E> pullData();

	E save(E entity);

	boolean removeOne(E entity);
	
	String getErrors();


//	void pushData();
	
	List<E> removeAll();

	List<E> removeIf(Predicate<E> predicate);
}
