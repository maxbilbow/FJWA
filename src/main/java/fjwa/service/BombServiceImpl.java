package fjwa.service;


import fjwa.model.Bomb;
import fjwa.repository.BombRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
	public void defuse() {
		threads.runOnAfterMainThread(t -> {
			List<Bomb> entities = getEntities();
			entities.forEach(bomb -> {
				bomb.setLive(false);
				repository().save(bomb);
			});
		}).setName("diffuse()");
	}

	@Override
	public BombRepository getRepository() {
		return bombRepository;
	}

	//	@Override
//	@Transactional
//	public void cleanUp() {
//		for (Bomb bomb : getEntities())
//			if (bomb != null && (bomb.hasTimeRunOut() || !bomb.isLive()))
//				this.removeOne(bomb);
//	}






}
