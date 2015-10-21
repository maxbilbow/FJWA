package rabbit;

import click.rmx.debug.Bugger;
import click.rmx.debug.WebBugger;
import com.rabbitmq.client.*;
import fjwa.messages.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static click.rmx.debug.Bugger.timestamp;


/**
 * Created by bilbowm on 19/10/2015.
 */
public class Rabbit {
    public final static String QUEUE_NAME = "websockets";
    private final static String EXCHANGE_NAME = WebBugger.DEBUG_EXCHANGE_NAME;



    public static void sendMessageWithTopic(Message msg, Object sender)
            throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String routingKey = msg.getTopic();
        String message = msg.getMessage();


        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        WebBugger.getInstance().addLog("[" + timestamp() + "] "+sender.getClass().getSimpleName()+" Sent '" + routingKey + "':'" + message + "'");

        connection.close();
    }

    @Deprecated
    public static void startDebugQueue(String... topics) throws IOException {
        List argv = Arrays.asList(new String[]{"debug.#", "#.log", "#.error", "#.exception"});
        String[] factory = topics;
        int queueName = topics.length;

        String bindingKey;
        for(int consumer = 0; consumer < queueName; ++consumer) {
            bindingKey = factory[consumer];
            argv.add(bindingKey);
        }

        ConnectionFactory var7 = new ConnectionFactory();
        var7.setHost("localhost");
        Connection connection = var7.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String var8 = channel.queueDeclare().getQueue();
        if(argv.size() < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        Iterator var9 = argv.iterator();

        while(var9.hasNext()) {
            bindingKey = (String)var9.next();
            channel.queueBind(var8, "debug_topic_exchange", bindingKey);
        }

        Bugger.print(" [WebBugger] Waiting for messages. To exit press CTRL+C");
        DefaultConsumer var10 = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String topic = envelope.getRoutingKey().toLowerCase();
                String log = "[" + timestamp() + "] WebBugger received \'" + topic + "\':\'" + message + "\'";
                Bugger.print(log);
                if(!topic.contains("error") && !topic.contains("exception")) {
                    WebBugger.getInstance().addLog(log);
                } else {
                    WebBugger.getInstance().addException(log);
                }

            }
        };
        channel.basicConsume(var8, true, var10);
    }
}