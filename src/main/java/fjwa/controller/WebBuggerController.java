package fjwa.controller;

import click.rmx.debug.WebBugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by bilbowm on 13/10/2015.
 */
@Controller
public class WebBuggerController {

    @Autowired
    private WebBugger debug;// = WebBugger.getInstance();


    @RequestMapping(value = "/errorLog.json", method = RequestMethod.GET)
    public @ResponseBody
    List<Collection<String>> get()
    {
        return Arrays.asList(debug.getErrors(),debug.getLogs());
    }



}
