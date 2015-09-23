package fjwa.repository;

import java.util.List;

import fjwa.RMXException;
import fjwa.model.Goal;
import fjwa.model.GoalReport;

public interface GoalRepository extends EntityRepository<Goal> {
	List<GoalReport> findAllGoalReports() throws RMXException;
}
