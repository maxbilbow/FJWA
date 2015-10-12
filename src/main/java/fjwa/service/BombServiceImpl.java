package fjwa.service;


import click.rmx.debug.RMXException;
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
	public List<Bomb> defuse() {
		for (Bomb bomb : getEntities())
			bomb.setLive(false);
		return this.synchronize();
	}

	@Override
	public List<Bomb> removeAll() {
		for (Bomb bomb : getEntities()) {
			if (bomb.isDiffusable())
				try {
					this.repository().delete(bomb);
				} catch (Exception e) {
					this.addError(RMXException.unexpected(e));
				}
			else
				System.err.println(bomb.getName() + " simply cannot be stopped!");
		}
		return this.findAllEntities();
	}


	//	@Override
//	@Transactional
//	public void cleanUp() {
//		for (Bomb bomb : getEntities())
//			if (bomb != null && (bomb.hasTimeRunOut() || !bomb.isLive()))
//				this.remove(bomb);
//	}






}
