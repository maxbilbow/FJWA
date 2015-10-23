package fjwa.controller;

import click.rmx.debug.WebBugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by bilbowm on 20/10/2015.
 */
@Controller
@RequestMapping("/webSockets.html")
public class WebSocketsController {

    @Autowired
    WebBugger debug;

    String defaultSocket = "echo.websocket.org";

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model)
    {
        model.addAttribute("sockets", sockets);

        return "webSockets";
    }


    String[] sockets = {
            defaultSocket,
            "localhost:8080/spring-ng-chat/chat",
            "localhost:8080/spring-ng-chat/debug",
            "localhost:15674/stomp"
    };


}
