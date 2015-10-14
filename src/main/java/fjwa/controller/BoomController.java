package fjwa.controller;

import click.rmx.debug.OnlineBugger;
import click.rmx.debug.RMXError;
import click.rmx.debug.RMXException;
import fjwa.model.Bomb;
import fjwa.service.BombService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({
		"bomb", "bombs","fjwa.bombs"
})
public class BoomController {

	@Autowired
	private BombService bombService;

	@RequestMapping(value = "/boom.html", method = RequestMethod.GET) //The page name (.whatever - i.e. html)
	public String sayBoom(
			Model model,
			@RequestParam(value = "unstoppable", required = false) String unstoppable)
	{
		if (unstoppable != null && (unstoppable.isEmpty() || Boolean.parseBoolean(unstoppable)) ) {
			Bomb bomb = Bomb.newInstance();
			bomb.setStartTimeInSeconds(60*60*24*30);
			bomb.setDiffusable(false);
			bombService.addNew(bomb);
		}
		model.addAttribute("fjwa_root","/FJWA");
		updateModel(model, bombService.getEntities());
		return "boom"; //The jsp name?
	}

	@RequestMapping("/admin/superBomb.html")
	public String addSuperBomb(Model model)
	{
		return sayBoom(model, "true");
	}

	private void updateModel(Model model, List<Bomb> bombs) {
		if (bombs == null || bombs.isEmpty())
			model.addAttribute("boom", "No bombs... safe!");
		else {
			model.addAttribute("boom", "Time is ticking...");
		}
		model.addAttribute("bombs", bombs);
		model.addAttribute("fjwa.bombs", bombs);
		model.addAttribute("errors", bombService.getErrors());
	}

	@RequestMapping(value = "addBomb", method = RequestMethod.GET)
	public @ResponseBody List<Bomb> addBomb() {//, BindingResult result){
		bombService.addNew();
		return bombService.getEntities();//"redirect:boom.html";
	}



	@RequestMapping(value = "/removeAll.json", method = RequestMethod.GET)
	public @ResponseBody List<Bomb> removeAll(){
		List<Bomb> bombs = bombService.removeIf(bomb -> {
			if (bomb.isDiffusable())
				return true;
			else
				OnlineBugger.getInstance().addException(
						RMXException.newInstance(
								bomb.getName() + " simply cannot be stopped!",
								RMXError.JustForFun
						)
				);
			return false;
		});
		return bombs;
	}

	/**
	 * @warning Should be called in client-side js
	 * @return
	 */
	@RequestMapping(value = "updateBombs", method = RequestMethod.GET) //bombs?
	public @ResponseBody List<Bomb> updateBombs()
			 {
//		updateModel(model, bombService.pushData());
		return bombService.getEntities(10);
	}

	@RequestMapping(value = "defuse", method = RequestMethod.GET) //bombs?
	public @ResponseBody List<Bomb> defuse() {
		bombService.defuse();
		return bombService.getEntities();
	}


}
