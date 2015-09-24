package fjwa.repository;

import fjwa.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bilbowm on 24/09/2015.
 */
@Repository("exerciseRepository")
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}
