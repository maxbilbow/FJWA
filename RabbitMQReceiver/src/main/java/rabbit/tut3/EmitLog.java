package rabbit.tut3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import rabbit.Sender;

import static click.rmx.debug.Bugger.print;
import static rabbit.Application.EXCHANGE_LOGS;

/**
 * Created by bilbowm on 19/10/2015.
 */

public class EmitLog extends Sender {

    public static void send(String[] argv)
            throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_LOGS, "fanout");

        String message = getMessage(argv);

        channel.basicPublish(EXCHANGE_LOGS, "", null, message.getBytes());
        print(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }



    
}
