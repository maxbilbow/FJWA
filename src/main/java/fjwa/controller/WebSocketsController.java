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

    String defaultSocket = "";

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model)
    {
        model.addAttribute("sockets", sockets);
        model.addAttribute("feeds", feeds);
        return "webSockets";
    }

    String [] feeds = {
            "'/exchange/debug_topic_exchange/debug.log'",
            "'/exchange/debug_topic_exchange/debug.error'",
            "'/app/chat' (replies to '/topic/messages')",
            "'/app/debug/logs' (replies to '/topic/debug/logs')",
            "'/app/debug/errors' (replies to '/topic/debug/logs')",
            " ",
            "'ws://localhost:8080/ws-tut1/websocket/echo' -> simple echo server"
    };
    String[] sockets = {
            "ws://localhost:8080/debug-server/updates",
            "http://localhost:8080/spring-ng-chat/chat",
            "http://localhost:15674/stomp",
            "ws://localhost:8080/ws-tut1/websocket/echo",
            "ws://localhost:8081/FJWA/websocket/echo",
            "ws://localhost:8081/FJWA/websocket/bombs"
    };


}
