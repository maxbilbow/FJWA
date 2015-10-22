package fjwa.controller;


import fjwa.messages.Message;
import fjwa.messages.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * Created by bilbowm on 22/10/2015.
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

    @RequestMapping(method = RequestMethod.GET)
    public String viewApplication() {
        return "index";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public OutputMessage sendMessage(Message message) {
        return new OutputMessage(message, new Date());
    }
}
