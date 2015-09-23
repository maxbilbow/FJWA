package fjwa.repository;

import java.util.List;

import fjwa.RMXException;
import fjwa.model.GoalReport;
import fjwa.model.IEntity;


public interface EntityRepository<E extends IEntity> {

	List<E> loadAll() throws RMXException;

	E save(E entity) throws RMXException;

	E remove(E expired) throws RMXException;

	@Deprecated
	E synchronize(E entity) throws RMXException;

	Object executeQuery(String qText) throws RMXException;
	
	Object queryList(String qText) throws RMXException;

	
}
