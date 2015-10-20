package rabbit.tut5;

import click.rmx.debug.WebBugger;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import rabbit.Application;
import com.rabbitmq.client.*;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = Application.EXCHANGE_TOPICS;
    public static void receive(String... argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        for (String bindingKey : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String log = " [x] Received '" +
                        envelope.getRoutingKey() +
                        "':'" + message + "'";
                print(log);
                WebBugger.getInstance().addLog(log);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
