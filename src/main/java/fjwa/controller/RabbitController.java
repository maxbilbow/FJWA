package fjwa.controller;

import click.rmx.debug.WebBugger;
import fjwa.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rabbit.Rabbit;

import javax.validation.Valid;

/**
 * Created by bilbowm on 19/10/2015.
 */
@Controller
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    WebBugger debug;

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model, @ModelAttribute("aMessage") Message aMessage) {
        if (aMessage == null) {
            aMessage = new Message();
            aMessage.setMessage("Hello, rabbit.");
            aMessage.setTopic("debug.log");
        }


        model.addAttribute("aMessage", aMessage);
        model.addAttribute("topics", topics);

        return "rabbit";
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public String sendMessage(ModelMap model, @NotNull String message)
//    {
//        model.addAttribute("message", message);
//        Rabbit.sendMessage(message, QUEUE_NAME);
//        return "rabbit";
//    }

    String[] topics = {
        "--select--", "debug.log", "debug.error"
    };

    @RequestMapping(method = RequestMethod.POST)
    public String sendMessageWithTopic(
            ModelMap model,
            @Valid @ModelAttribute("aMessage") Message aMessage,
            BindingResult result)
    {
        model.addAttribute("aMessage", aMessage);
        model.addAttribute("topics", topics);

        if (result.hasErrors())
            debug.addLog(result.toString());
        else
            try {
                Rabbit.sendMessageWithTopic(aMessage,this);
            } catch (Exception e) {
                debug.addException(e);
            }
        return get(model,aMessage);
    }


//
}
