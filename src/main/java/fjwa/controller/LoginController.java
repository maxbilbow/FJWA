package fjwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by bilbowm on 29/09/2015.
 */
@Controller
public class LoginController {

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login(ModelMap model) {
        //TODO stop json requests repeatedly accessing here.
        return "login";
    }

    @RequestMapping(value="/loginFailed", method=RequestMethod.GET)
    public String loginFailed(ModelMap model) {
        System.err.println("Login Failed");

        model.addAttribute("error", "true");
        return "login";
    }

    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logout(ModelMap model) {
        return "logout";
    }

    @RequestMapping(value="/403", method=RequestMethod.GET)
    public String error403(ModelMap model) {
        return "403";
    }

    @RequestMapping(value = "/goAway", method = RequestMethod.GET)
    public String goAway()
    {
        return "goAway";
    }

    @RequestMapping(value = "/bad", method = RequestMethod.GET)
    public String bad()
    {
        return "goAway";
    }
}
