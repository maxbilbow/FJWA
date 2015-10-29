package click.rmx.cmd;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 23/10/2015.
 */
@SpringBootApplication
public class App {
    public static void main(String[] args)
    {
        print("Hello, world.");
//        CmdSocketTool.start("http://localhost:8080/spring-ng-chat/debug");
//        CmdSocketTool.start("ws://echo.websocket.org");
    }
}
