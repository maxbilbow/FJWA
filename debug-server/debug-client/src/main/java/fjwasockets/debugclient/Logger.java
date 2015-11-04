package fjwasockets.debugclient;

import click.rmx.debug.WebBugger;

import org.codehaus.jackson.map.ObjectMapper;
import com.mongodb.util.JSONParseException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * Created by bilbowm on 27/10/2015.
 */
public class Logger {

    private static final String EXCHANGE_NAME = WebBugger.DEBUG_EXCHANGE_NAME;
    private final String appId;

    public Logger(String appId) {
        this.appId = appId;
    }

    public void send(String message, AMQP.BasicProperties properties, String... routing)
            throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        if (properties == null)
            properties = defaultProperties();

        for (String routingKey : routing) {
            channel.basicPublish(EXCHANGE_NAME, routingKey, properties , message.getBytes());
            System.out.println(" ["+appId+"] Sent '" + routingKey + "':'" + message + "'");
        }
        connection.close();
    }

    private AMQP.BasicProperties defaultProperties()
    {
        return new AMQP.BasicProperties.Builder()
            .appId(appId)
            .timestamp(new Date())
            .build();
    }

    public void send(Object object, AMQP.BasicProperties properties, String... routing) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
           json = mapper.writeValueAsString(object);
        } catch (JSONParseException e) {
            try {
                send("Couuld not parse as JSON will use toString() instead.", null,  "debug.warning");
            } catch (Exception e1) {
                e1.printStackTrace();
                json = object.toString();
            }
        }
        send(json, null, routing);
    }

       public void logWarning(String message)
    {
        try {
            send(message, null, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logException(String message)
    {
        try {
            send(message, null, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logMessage(String message)
    {
        try {
            send(message, null, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logWarning(Object object)
    {
        try {
            send(object, null, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logException(Object object)
    {
        try {
            send(object, null, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logMessage(Object object)
    {
        try {
            send(object, null, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

    }

}