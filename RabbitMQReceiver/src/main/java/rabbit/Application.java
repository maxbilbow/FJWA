package rabbit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rabbit.tut2.NewTask;
import rabbit.tut2.Worker;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 19/10/2015.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static final String
            QUEUE_NAME = "hello",
            TASK_QUEUE_NAME = "task_queue",
            DEBUG_QUEUE = "debug_log";
    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Application.class, args);
        for (String msg :args) {
            print("arg: " + msg);
            if (msg.contains("-Rabbit:send")) {
                send(args);
                break;
            }
            if (msg.contains("-Rabbit:receive")) {
                receive(args);
                break;
            }
        }

    }

    private static void send(String[] args) throws IOException {
        String prefix = "-Rabbit:tut";
        for (String msg :args) {
            if (msg.contains(prefix + "2"))
                NewTask.send(args);
            if (msg.contains(prefix + "3"))
                NewTask.send(args);

        }
    }

    private static void receive(String[] args) throws Exception {
        String prefix = "-Rabbit:tut";
        for (String msg :args) {
            if (msg.contains(prefix + "2"))
                Worker.receive(args);;
            if (msg.contains(prefix + "3"))
                Worker.receive(args);

        }

    }
}

