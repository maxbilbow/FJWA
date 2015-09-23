package fjwa.service;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fjwa.RMXException;
import fjwa.model.Bomb;
import fjwa.repository.EntityRepository;


@Service("bombService")
public class BombServiceImpl extends AbstractEntityService<Bomb> implements BombService {

	@Autowired
	private EntityRepository<Bomb> bombRepository;
	
	@Override
	protected EntityRepository<Bomb> repository() {
		return bombRepository;
	}
	
	@Override
	@Transactional
	public List<Bomb> defuse() {
		for (Bomb bomb : getEntities())
			bomb.setLive(false);
		return this.synchronize();
	}


	
//	@Override
//	@Transactional
//	public void cleanUp() {
//		for (Bomb bomb : getEntities())
//			if (bomb != null && (bomb.hasTimeRunOut() || !bomb.isLive()))
//				this.remove(bomb);
//	}




	

}
