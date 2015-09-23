package fjwa.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fjwa.model.Activity;
import fjwa.model.Exercise;
import fjwa.repository.EntityRepository;


@Service("exerciseService")
public class ExerciseServiceImpl extends AbstractEntityService<Exercise> implements ExerciseService {

	@Autowired
	private EntityRepository<Exercise> exerciseRepository;
	
	@Override
	protected EntityRepository<Exercise> repository() {
		return exerciseRepository;
	}
	
	@Override
	public List<Activity> findAllActivities() {
		
		List<Activity> activities = new ArrayList<Activity>();
		
		Activity run = new Activity();
		run.setDesc("Run");
		activities.add(run);
		
		Activity bike = new Activity();
		bike.setDesc("Bike");
		activities.add(bike);
		
		Activity swim = new Activity();
		swim.setDesc("Swim");
		activities.add(swim);
		
		return activities;
	}
}
