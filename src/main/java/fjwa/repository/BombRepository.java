package fjwa.repository;


import fjwa.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fjwa.model.Bomb;

@Repository("bombRepository")
public interface BombRepository extends JpaRepository<Bomb, Long> {

}
