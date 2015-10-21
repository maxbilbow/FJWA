package rabbit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rabbit.tut2.NewTask;
import rabbit.tut2.Worker;
import rabbit.tut3.EmitLog;
import rabbit.tut3.ReceiveLogs;
import rabbit.tut4.EmitLogDirect;
import rabbit.tut4.ReceiveLogsDirect;
import rabbit.tut5.EmitLogTopic;
import rabbit.tut5.ReceiveLogsTopic;
import rabbit.tut6.RPCClient;
import rabbit.tut6.RPCServer;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 19/10/2015.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static final String
            QUEUE_NAME = "websockets",
            TASK_QUEUE_NAME = "task_queue",
            DEBUG_QUEUE = "debug_log",
            EXCHANGE_LOGS = "logs",
            EXCHANGE_DIRECT_LOGS = "direct_logs",
            EXCHANGE_TOPICS = "debug_topic_exchange",
            RPC_QUEUE_NAME = "rpc_queue";

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
    private static final String tutorial = "-Rabbit:tut";
    private static void send(String[] args) throws Exception {

        for (String msg :args) {
            if (msg.contains(tutorial + 2)) {
                NewTask.send(args);
                break;
            }
            if (msg.contains(tutorial + 3)) {
                EmitLog.send(args);
                break;
            }
            if (msg.contains(tutorial + 4)) {
                EmitLogDirect.send(args);
                break;
            }
            if (msg.contains(tutorial + 5)) {
                EmitLogTopic.send(args);
                break;
            }
            if (msg.contains(tutorial + 6)) {
                RPCClient fibonacciRpc = new RPCClient();

                print(" [x] Requesting fib(30)");
                String response = fibonacciRpc.call("30");
                print(" [.] Got '" + response + "'");

                print(" [x] Requesting fib(31)");
                response = fibonacciRpc.call("31");
                print(" [.] Got '" + response + "'");

                print(" [x] Requesting fib(32)");
                response = fibonacciRpc.call("32");
                print(" [.] Got '" + response + "'");

                fibonacciRpc.close();
            }

        }
    }

    private static void receive(String[] args) throws Exception {

        for (String msg :args) {
            if (msg.contains(tutorial + 2)) {
                Worker.receive(args);
                break;
            }
            if (msg.contains(tutorial + 3)) {
                ReceiveLogs.receive(args);
                break;
            }
            if (msg.contains(tutorial + 4)) {
                ReceiveLogsDirect.receive(args);
                break;
            }
            if (msg.contains(tutorial + 5)) {
                ReceiveLogsTopic.receive(args);
                break;
            }
            if (msg.contains(tutorial + 6)) {
                RPCServer.receive(args);
            }

        }

    }
}

