package rabbit.tut3;

/**
 * Created by bilbowm on 19/10/2015.
 */

import com.rabbitmq.client.*;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;
import static rabbit.Application.EXCHANGE_LOGS;
public class ReceiveLogs {

    public static void receive(String[] argv) throws Exception {
        print("Tut3: ReceiveLogs...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_LOGS, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_LOGS, "");

        print(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                print(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}