package rabbit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rabbit.tut2.NewTask;
import rabbit.tut2.Worker;
import rabbit.tut3.EmitLog;
import rabbit.tut3.ReceiveLogs;
import rabbit.tut4.EmitLogDirect;
import rabbit.tut4.ReceiveLogsDirect;

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
            DEBUG_QUEUE = "debug_log",
            EXCHANGE_LOGS = "logs",
            EXCHANGE_DIRECT_LOGS = "direct_logs";
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
            if (msg.contains(prefix + 2)) {
                NewTask.send(args);
                break;
            }
            if (msg.contains(prefix + 3)) {
                EmitLog.send(args);
                break;
            }
            if (msg.contains(prefix + 4)) {
                EmitLogDirect.send(args);
                break;
            }

        }
    }

    private static void receive(String[] args) throws Exception {
        String prefix = "-Rabbit:tut";

        for (String msg :args) {
            if (msg.contains(prefix + 2)) {
                Worker.receive(args);
                break;
            }
            if (msg.contains(prefix + 3)) {
                ReceiveLogs.receive(args);
                break;
            }
            if (msg.contains(prefix + 4)) {
                ReceiveLogsDirect.receive(args);
                break;
            }

        }

    }
}

