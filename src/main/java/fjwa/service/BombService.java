package fjwa.service;

import java.util.List;

import fjwa.RMXException;
import fjwa.model.Bomb;

public interface BombService extends EntityService<Bomb> {
	

	
	
	RMXException addError(RMXException e);
	
	
	List<Bomb> defuse();

//	
//	Collection<Bomb> findAllEntities();
//
//	Bomb save(Bomb entity);
//
//	boolean remove(Bomb entity);
//	
//	
//	String getErrors();
//	
//	Collection<Bomb> synchronize();
	
}
