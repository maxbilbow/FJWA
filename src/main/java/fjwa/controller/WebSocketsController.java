package fjwa.controller;

import click.rmx.debug.WebBugger;
import fjwa.model.WebSocketDesc;
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

    WebSocketDesc defaultSocket = new WebSocketDesc("echo.websocket.org");

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model)
    {
        model.addAttribute("sockets", sockets);

        return "webSockets";
    }


    WebSocketDesc[] sockets = {
            defaultSocket
    };


}
