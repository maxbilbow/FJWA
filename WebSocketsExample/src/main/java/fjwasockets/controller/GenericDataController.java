package fjwasockets.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by bilbowm on 22/10/2015.
 */
@Controller
@RequestMapping("/")
public class GenericDataController {

    @MessageMapping("/fjwa/data/lists")
    @SendTo({"/topic/fjwa/data/lists"})
    public List sendList(List list) {
        list.forEach(System.out::println);
        return list;
    }

    @MessageMapping("/fjwa/data/maps")
    @SendTo({"/topic/fjwa/data/maps"})
    public Map senderror(Map map) {
        map.keySet().forEach(System.out::println);
        return map;
    }
}
