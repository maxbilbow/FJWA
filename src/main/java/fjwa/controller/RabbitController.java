package fjwa.controller;

import click.rmx.debug.WebBugger;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import static rabbit.Recv.QUEUE_NAME;

/**
 * Created by bilbowm on 19/10/2015.
 */
@Controller
@RequestMapping("/rabbit")
public class RabbitController {

    @Autowired
    WebBugger debug;

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            debug.addLog("RabbitMQ: Message Sent");
        } catch (IOException e) {
            e.printStackTrace();
            debug.addException("RabbitMQ: Message Failed");
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
                debug.addException(e);
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
                debug.addException(e);
            }
        }
        return "redirect:";
    }
}
