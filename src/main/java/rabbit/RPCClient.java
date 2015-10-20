package rabbit;


import click.rmx.debug.WebBugger;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

import static rabbit.Application.RPC_QUEUE_NAME;

/**
 * Created by bilbowm on 19/10/2015.
 */
public class RPCClient {
    private Connection connection;
    private Channel channel;
    private String requestQueueName = RPC_QUEUE_NAME;
    private String replyQueueName;
    private QueueingConsumer consumer;
    private boolean initialised;

    public RPCClient()  {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();

            replyQueueName = channel.queueDeclare().getQueue();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, true, consumer);
            initialised = true;
        } catch (IOException e) {
            WebBugger.getInstance().addException(e, "RPCClient failed to start");
            initialised = false;
        }
    }

    public String call(String message) throws Exception {
        if(!initialised) {
            return "null";
        }
        String response = null;
        String corrId = java.util.UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }
}
