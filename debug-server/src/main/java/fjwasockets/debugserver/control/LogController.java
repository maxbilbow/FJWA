package fjwasockets.debugserver.control;

import fjwasockets.debugserver.repository.LogRepository;
import fjwasockets.debugserver.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by Max on 25/10/2015.
 */
@Controller
@RequestMapping("/index")
public class LogController {


    @Resource//(type = LogService.class)
    private LogService service;

    @Resource//(type = LogRepository.class)
    private LogRepository repository;

    private boolean serverIsActive = false;

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model)
    {

        model.addAttribute("logs", repository.getMessages());
        model.addAttribute("errors", repository.getErrors());
        model.addAttribute("warnings", repository.getWarnings());
        model.addAttribute("status",
                serverIsActive ?
                        "<span style=\"color: green\">SERVER IS ON</span>" :
                        "<span style=\"color: red\">SERVER IS OFF</span>"
        );
        model.addAttribute("connect", serverIsActive ? "Stop" : "Start");
        return "version2";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String start(ModelMap model)
    {
        if (serverIsActive) {
            try {
                serverIsActive = !service.closeServer();
                service.addLog("Debug server was closed");
            } catch (Exception e) {
                e.printStackTrace();
                service.addException("Server could not be closed! >> " + e.toString());
                serverIsActive = true;
            }
        } else {
            //Start rabbitMQ Debug log receiver
            try {
                service.startDebugQueue();
                service.addLog("RabbitMQ Topic Server Started");
                serverIsActive = true;
            } catch (Exception e) {
                e.printStackTrace();
                service.addException("Rabbit Topic server failed." + e.toString());
                serverIsActive = false;
            }
        }
        return get(model);
    }
}
