package fjwa.service;

import fjwa.model.Bomb;
import fjwa.repository.BombRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BombService extends EntityService<Bomb> {
	

	
	

	BombRepository getRepository();
	
	void defuse();

	@Override
	@Transactional
	default Bomb addNew() {
		Bomb bomb = Bomb.newInstance();
		this.addNew(bomb);
		return bomb;
	}


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
