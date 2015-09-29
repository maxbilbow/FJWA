package fjwa.service;

import click.rmx.debug.RMXException;
import fjwa.model.Bomb;

import java.util.List;

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
