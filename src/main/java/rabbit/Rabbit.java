package rabbit;

import click.rmx.debug.RMXException;
import click.rmx.debug.WebBugger;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class Rabbit {
    public final static String QUEUE_NAME = "hello";
    public final static String DEBUG_QUEUE = "WebBugger";
    public static String timestamp()
    {
        return DateTimeFormatter.ISO_INSTANT
                .format(Instant.now()).split("T")[1];//.split(".")[0];
    }
    public static void startDebugReceiver() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(DEBUG_QUEUE, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                String response = "<span style=\"color: red;\">" +
                        "RabbitMQ::RECV: [" +
                        timestamp() +
                        "] </span>'" + message + "'";
                System.out.println(response);
               WebBugger.getInstance().addLog(response);
            }
        };
        channel.basicConsume(DEBUG_QUEUE, true, consumer);
    }

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
                System.out.println(" ["+timestamp()+"] Received '" + message + "'");
//                System.out.println("  --FROM: " + consumerTag);
                try {
                    sendMessage(message, DEBUG_QUEUE);
                }catch (Exception e) {
                    System.err.println("Failed to send receipt confirmation");
                    throw e;
                }
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
            WebBugger.getInstance().addLog("RabbitMQ::SEND [" + timestamp() + "] '"+message+"'");

        } catch (IOException e) {
           WebBugger.getInstance().addException(
                   RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED")
           );
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                WebBugger.getInstance().addException(
                        RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED")
                );
            }
            try {
                connection.close();
            } catch (IOException e) {
                WebBugger.getInstance().addException(
                        RMXException.unexpected(e, "RabbitMQ::SEND [" + timestamp() + "] Message FAILED")
                );
            }
        }
    }
}