package fjwa.repository;

import java.util.List;

import fjwa.RMXException;
import fjwa.model.Goal;
import fjwa.model.GoalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

@Repository("goalRepository")
public interface GoalRepository  extends JpaRepository<Goal, Long> {

	@Query("Select new " + GoalReport.CLASS
					+ "(g.minutes, e.minutes, e.activity) "
					+ "from Goal g, Exercise e where g.id = e.goal.id"
	)
	 List<GoalReport> findGoalReports();



}
