package fjwa.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.Range;


@Entity
@Table(name="goals")
@NamedQueries( {
	@NamedQuery(
			name=Goal.FIND_ALL_GOALS,
			query="Select g from Goal g"
			),
	@NamedQuery(
			name=Goal.FIND_GOAL_REPORTS,
			query="Select new "+ GoalReport.CLASS
			+ "(g.minutes, e.minutes, e.activity) "
			+ "from Goal g, Exercise e where g.id = e.goal.id"
			)
})
public class Goal implements IEntity {
	public static final String 
	FIND_GOAL_REPORTS = "findGoalReports",
	FIND_ALL_GOALS = "findAllGoals",
	CLASS = "fjwa.model.Goal";

	@Id
	@GeneratedValue
	@Column(name="GOAL_ID")
	private Long id;
	
	@Range(min = 1, max = 120)
	@Column(name="MINUTES")
	private int minutes;

	@OneToMany(mappedBy = "goal", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Exercise> exercises = new ArrayList<>();
	
	public List<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
