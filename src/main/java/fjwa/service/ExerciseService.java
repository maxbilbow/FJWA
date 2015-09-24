package fjwa.service;

import java.util.List;

import fjwa.model.Activity;
import fjwa.model.Exercise;


public interface ExerciseService extends EntityService<Exercise> {
    List<Activity> findAllActivities();
}