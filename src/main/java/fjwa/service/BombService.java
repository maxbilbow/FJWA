package fjwa.service;

import click.rmx.debug.RMXException;
import fjwa.model.Bomb;

public interface BombService extends EntityService<Bomb> {
	

	
	
	RMXException addError(RMXException e);
	
	
	void defuse();


//	
//	Collection<Bomb> findAllEntities();
//
//	Bomb save(Bomb entity);
//
//	boolean removeOne(Bomb entity);
//	
//	
//	String getErrors();
//	
//	Collection<Bomb> pushData();
	
}
