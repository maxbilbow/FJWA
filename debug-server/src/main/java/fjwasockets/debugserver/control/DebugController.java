package fjwasockets.debugserver.control;

import click.rmx.debug.WebBugger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Created by bilbowm on 23/10/2015.
 */
@Controller
@RequestMapping("/index")
public class DebugController {

    private WebBugger debug = WebBugger.getInstance();

    private boolean serverIsActive = false;

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model)
    {
        model.addAttribute("logs", debug.getLogs());
        model.addAttribute("errors", debug.getErrors());
        model.addAttribute("status",
                serverIsActive ?
                        "<span style=\"color: green\">SERVER IS ON</span>" :
                        "<span style=\"color: red\">SERVER IS OFF</span>"
        );
        model.addAttribute("connect", serverIsActive ? "Stop" : "Start");
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String start(ModelMap model)
    {
        if (serverIsActive) {
            try {
                serverIsActive = !debug.closeServer();
                debug.addLog("Debug server was closed");
            } catch (IOException e) {
                debug.addException(e, "Server could not be closed!");
                serverIsActive = true;
            }
        } else {
            //Start rabbitMQ Debug log receiver
            try {
                debug.startDebugQueue();
                debug.addLog("RabbitMQ Topic Server Started");
                serverIsActive = true;
            } catch (Exception e) {
                debug.addException(e, "Rabbit Topic server failed.");
                serverIsActive = false;
            }
        }
        return get(model);
    }
}
