package rabbit.tut2;

import com.rabbitmq.client.*;
import rabbit.Sender;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;
import static rabbit.Application.TASK_QUEUE_NAME;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class NewTask extends Sender {

    public static void send(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        String message = getMessage(args);

        channel.basicPublish( "", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        print(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();




    }

}
