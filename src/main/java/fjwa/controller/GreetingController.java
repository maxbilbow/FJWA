package fjwa.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import websockets.Greeting;
import websockets.HelloMessage;

@Controller
public class GreetingController {


    @RequestMapping("/socketsExample.html")
    public String get(ModelMap model)
    {
        model.addAttribute("fjwa_root","/FJWA");
        return "socketsExample";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}