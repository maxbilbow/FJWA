package fjwa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fjwa.RMXException;
import fjwa.model.Goal;
import fjwa.model.GoalReport;
import fjwa.repository.EntityRepository;
import fjwa.repository.GoalRepository;

@Service("goalService")
public class GoalServiceImpl extends AbstractEntityService<Goal> implements GoalService {

	
	@Autowired
	private GoalRepository goalRepository;
	
	@Override
	protected EntityRepository<Goal> repository() {
		return goalRepository;
	}

	@Override
	@Transactional
	public List<GoalReport> findAllGoalReports() {
		try { 
			return ((GoalRepository) repository()).findAllGoalReports();
			
//			return (List<GoalReport>) repository().queryList(""
//					+ "Select new " + GoalReport.class.getName()
////					+ "fjwa.model.GoalReport"
//					+ "(g.minutes, e.minutes, e.activity) "
//					+ "from Goal g, Exercise e where g.id = e.goal.id");
		} catch (RMXException e) {
			this.addError(e);
		}
		return null;
	}
	


}
