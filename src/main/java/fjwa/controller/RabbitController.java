package fjwa.controller;

import click.rmx.debug.WebBugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rabbit.Rabbit;

import javax.validation.constraints.NotNull;

import static rabbit.Rabbit.QUEUE_NAME;

/**
 * Created by bilbowm on 19/10/2015.
 */
@Controller
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    WebBugger debug;

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model) {
        Rabbit.sendMessage("Arrived at the rabbit page...", QUEUE_NAME);
        model.addAttribute("message", "Hello, Rabbit!");
        return "rabbit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String sendMessage(ModelMap model, @NotNull String message)
    {
        model.addAttribute("message", message);
        Rabbit.sendMessage(message, QUEUE_NAME);
        return "rabbit";
    }


//
}
