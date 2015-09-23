package fjwa.repository;



import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fjwa.RMXException;
import fjwa.model.Goal;
import fjwa.model.GoalReport;

@Repository("goalRepository")
public class GoalRepositoryImpl extends AbstractRepository<Goal> implements GoalRepository {

	
	@Override
	public List<GoalReport> findAllGoalReports() throws RMXException {
		TypedQuery<GoalReport> query = em.createNamedQuery(Goal.FIND_GOAL_REPORTS, GoalReport.class); 
		return query.getResultList();
	}

	@Override
	protected String FIND_ALL() {
		return Goal.FIND_ALL_GOALS;
	}

	@Override
	protected Class<Goal> CLASS() {
		return Goal.class;
	}

}
