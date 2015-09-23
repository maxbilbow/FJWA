package fjwa.service;

import java.util.List;

import fjwa.model.Activity;
import fjwa.model.Exercise;


public interface ExerciseService {
	List<Activity> findAllActivities();

	Exercise save(Exercise entity);
}