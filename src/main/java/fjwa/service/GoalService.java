package fjwa.service;


import java.util.List;

import fjwa.RMXException;
import fjwa.model.Goal;
import fjwa.model.GoalReport;


public interface GoalService extends EntityService<Goal> {

	List<GoalReport> findAllGoalReports();
	
}
