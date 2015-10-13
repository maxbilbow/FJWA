package fjwa.controller;

import click.rmx.debug.OnlineBugger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by bilbowm on 13/10/2015.
 */
@Controller
@RequestMapping("/errorLog")
public class OnlineBuggerController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String get(@RequestParam(value = "format", defaultValue = "html") String format)
    {
        return OnlineBugger.getInstance().getErrorHtml();
    }
}
