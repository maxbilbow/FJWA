package fjwa.controller;

import fjwa.messages.Message;
import fjwa.messages.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class ChatController {

    @RequestMapping(value = "/talk.html", method = RequestMethod.GET)
    public String viewApplication() {
        return "chat";
    }

    @RequestMapping(value = "/socketsExample.html", method = RequestMethod.GET)
    public String get(ModelMap model) {
        model.addAttribute("fjwa_root","/FJWA");
        return "socketsExample";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public OutputMessage sendMessage(Message message) {
        return new OutputMessage(message, new Date());
    }

    @MessageMapping("/hello")
    @SendTo("/topic/message")
    public OutputMessage helloMessage(Message message) {
        return new OutputMessage(message, new Date());
    }
}