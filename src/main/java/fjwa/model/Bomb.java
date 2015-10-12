package fjwa.model;
import java.time.Duration;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "bomb_table")
@NamedQueries( {
	@NamedQuery(
			name=Bomb.FIND_ALL_BOMBS,
			query="Select b from Bomb b"
			)
})
public class Bomb implements IEntity {
	public static final String 
	FIND_ALL_BOMBS = "findAllBombs", 
	CLASS = "fjwa.model.Bomb";
	@Id
	@GeneratedValue
	private Long id;
	
	private Instant startTime;

	private String description;
	
	private boolean live = true;
	
	public static Bomb newInstance() {
		return new Bomb();
	}
	
	private boolean diffusable;
	
	@Range(min = 0)
	private int startTimeInSeconds;

	private boolean outOfTime = false;
	
	
	

	public Bomb() {
		this.setStartTimeInSeconds(
				(int) (Math.random() * 20 + 5)
				);
		diffusable = true;
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println(getName() + " Was DELETED by the GC!");
		super.finalize();
	}

	public String getDescription() {
		this.description = "Bomb " + this.getName() + ": ";
		if (this.isOutOfTime())
			description += "<span style=\"color: red;\">BOOM!</span>";
		else if (!live)
			description += "<span style=\"color: green;\">Diffused with " + this.getStartTimeInSeconds() + " remaining.</span>";
		else
			description += this.timeRemaining() + " seconds!";
		return description;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return "B" + this.id;
	}


	public Instant getStartTime() {
		return startTime;
	}


	public int getStartTimeInSeconds() {
		return startTimeInSeconds;
	}

	public boolean isOutOfTime() {
		return this.outOfTime = timeRemaining() < 0;
	}	

	public boolean isLive() {
		return live && !this.isOutOfTime();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setLive(boolean live) {
		if (this.diffusable) {
			this.setStartTimeInSeconds(this.timeRemaining());
//		this.startTime = Instant.now();
			this.live = live;
		} else {
			System.err.println("Bomb cannot be diffused. Run!");
		}
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public void setStartTimeInSeconds(int timeInSeconds) {
		this.startTimeInSeconds = timeInSeconds > 0 ? timeInSeconds : 0;
		this.startTime = Instant.now();
	}
	
	public int timeRemaining() {
		if (!live) {
			this.startTime = Instant.now();
			return this.startTimeInSeconds;
		}
		Instant now = Instant.now();
		Duration dt = Duration.between(this.startTime, now);
		int timeInSeconds = (int) (this.startTimeInSeconds - dt.getSeconds());
		return timeInSeconds;
	}

	public boolean isDiffusable() {
		return diffusable;
	}

	public void setDiffusable(boolean diffusable) {
		this.diffusable = diffusable;
	}
}
