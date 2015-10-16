package fjwa.controller;

import click.rmx.debug.RMXException;
import click.rmx.debug.WebBugger;
import fjwa.model.Goal;
import fjwa.model.GoalReport;
import fjwa.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static click.rmx.debug.Tests.note;

@Controller
@SessionAttributes("goal")
public class GoalController {
	
	@Autowired
	private GoalService goalService;

	@RequestMapping(value = "addGoal", method = RequestMethod.GET)
	public String addGoal(Model model, HttpSession session) {
		note();
		Goal goal = (Goal) session.getAttribute("goal");

		if (goal == null) {
			goal = new Goal();
			goal.setMinutes(10);
		}

		model.addAttribute("goal", goal);
		
		return "addGoal";
	}

	//TODO: Fix PreAuth below
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasPermission(#goal, 'createGoal')")
	@RequestMapping(value = "addGoal", method = RequestMethod.POST)
	public String updateGoal(@Valid @ModelAttribute("goal") Goal goal,
							 BindingResult result) {
		
		note("result has errors: " + result.hasErrors());

		note("Goal set: " + goal.getMinutes());
		
		if(result.hasErrors()) {
			return "addGoal";
		} else {
			goalService.save(goal);
		}
		
		return "redirect:index.jsp";
	}
	
	@RequestMapping(value = "getGoals", method = RequestMethod.GET)
	public String getGoals(Model model) {
		note();
		try {
			goalService.pullData().join();
		} catch (InterruptedException e) {
			WebBugger.getInstance().addException(RMXException.unexpected(e));
		}
		Collection<Goal> goals = goalService.getEntities();
		model.addAttribute("goals", goals);
		return "getGoals";
	}
	
	@RequestMapping(value = "getGoalReports", method = RequestMethod.GET)
	public String getGoalReports(Model model) {
		note();
		List<GoalReport> goalReports = goalService.findAllGoalReports();
		model.addAttribute("goalReports", goalReports);
		return "getGoalReports";
	}


	@RequestMapping(value = "/GoalReports", method = RequestMethod.GET)
	public @ResponseBody List<GoalReport> GoalReports() {
		note();
		return goalService.findAllGoalReports();
	}

}
