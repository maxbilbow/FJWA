package fjwa.repository;


import org.springframework.stereotype.Repository;

import fjwa.model.Bomb;

@Repository("bombRepository")
public class BombRepositoryImpl extends AbstractRepository<Bomb> implements EntityRepository<Bomb> {

	@Override
	protected String FIND_ALL() {
		return Bomb.FIND_ALL_BOMBS;
	}

	@Override
	protected Class<Bomb> CLASS() {
		return Bomb.class;
	}

}
