package fjwa.service;

import java.util.Collection;
import java.util.List;


public interface EntityService<E> {
	List<E> findAllEntities();

	E save(E entity);

	boolean remove(E entity);
	
	String getErrors();
	
	List<E> synchronize();
	
	List<E> removeAll();
}
