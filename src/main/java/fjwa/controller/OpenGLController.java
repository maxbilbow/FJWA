package fjwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by bilbowm on 15/10/2015.
 */
@Controller
@RequestMapping("/gl")
public class OpenGLController {

    @RequestMapping(method = RequestMethod.GET)
    public String get()
    {
        return "gl";
    }
}
