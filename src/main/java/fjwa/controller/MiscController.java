package fjwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by bilbowm (Max Bilbow) on 17/11/2015.
 */
@Controller
public class MiscController {


    @RequestMapping(value = "other", method = RequestMethod.GET)
    public String get(ModelMap model, HttpSession session) {
        return "other";
    }
}
