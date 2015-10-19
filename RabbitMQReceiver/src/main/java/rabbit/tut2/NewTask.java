package rabbit.tut2;

import com.rabbitmq.client.*;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class NewTask {

    private static final String QUEUE_NAME = "hello";
    private static final String TASK_QUEUE_NAME = "";

    private static String getMessage(String[] strings){
        if (strings.length < 1)
            return "Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

    public static void send(String[] args)
    {
        System.out.println("Hello!");

        Connection connection = null;
        Channel channel = null;
        String message = getMessage(args);


        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            print(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }

    public static void receive(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    System.out.println(" [x] Received '" + message + "'");
                    try {
                        doWork(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(" [x] Done");
                    }
                }
            };
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            channel.basicConsume(TASK_QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
