package rabbit;


import click.rmx.debug.RMXException;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static click.rmx.debug.Bugger.print;
import static rabbit.Application.QUEUE_NAME;
import static rabbit.Application.TASK_QUEUE_NAME;
import static rabbit.Application.DEBUG_QUEUE;
/**
 * Created by bilbowm on 19/10/2015.
 */
@Deprecated
public class Receiver {

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                print(" [" + timestamp() + "] Received '" + message + "'");
                sendMessage(message, DEBUG_QUEUE);

            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public static void sendMessage(String message, String queue) {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
            channel.basicPublish("", queue, null, message.getBytes());
            print("RabbitMQ::SEND [" + timestamp() + "] '" + message + "'");

        } catch (IOException e) {
            print(RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED").toString(), true);
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                print(RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED").toString(), true);
            }
            try {
                connection.close();
            } catch (IOException e) {
                print(RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED").toString(), true);
            }
        }
    }

    public static String timestamp()
    {
        return DateTimeFormatter.ISO_INSTANT
                .format(Instant.now()).split("T")[1];//.split(".")[0];
    }
}
