package fjwa.service;


import java.util.List;

import fjwa.repository.BombRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fjwa.model.Bomb;


@Service("bombService")
public class BombServiceImpl extends AbstractEntityService<Bomb> implements BombService {

	@Autowired
	private BombRepository bombRepository;
	
	@Override
	protected JpaRepository<Bomb, Long> repository() {
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
