package rabbit.tut4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import rabbit.Sender;

import static click.rmx.debug.Bugger.print;
import static rabbit.Application.EXCHANGE_DIRECT_LOGS;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class EmitLogDirect extends Sender {



    public static void send(String[] argv)
            throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_DIRECT_LOGS, "direct");

        String severity = getSeverity(argv);
        String message = getMessage(argv);

        channel.basicPublish(EXCHANGE_DIRECT_LOGS, severity, null, message.getBytes());
        print(" [x] Sent '" + severity + "':'" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getSeverity(String[] argv) {
        return "-Rabbit:tut4";
    }

}
