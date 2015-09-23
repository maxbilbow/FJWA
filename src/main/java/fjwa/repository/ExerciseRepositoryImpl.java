package fjwa.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import fjwa.model.Exercise;

@Repository("exerciseRepository")
public class ExerciseRepositoryImpl extends AbstractRepository<Exercise> {

	@Override
	protected String FIND_ALL() {
		return Exercise.FIND_ALL_EXERCISES;
	}

	@Override
	protected Class<Exercise> CLASS() {
		return Exercise.class;
	}


}
