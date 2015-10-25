package fjwasockets.debugserver.service;

import click.rmx.debug.RMXException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import fjwasockets.debugserver.model.Log;
import fjwasockets.debugserver.model.LogType;
import fjwasockets.debugserver.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static click.rmx.debug.Bugger.print;
import static click.rmx.debug.WebBugger.DEBUG_EXCHANGE_NAME;

/**
 * Created by Max on 25/10/2015.
 */
@Service
public class LogService {
    private Connection connection;
    private Channel channel;
    private Consumer consumer;

    @Resource//(type = LogRepository.class)
    private LogRepository repository;

    public boolean closeServer() throws IOException, TimeoutException {
        boolean chClosed = false, cnnClosed = false;
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
            chClosed = true;
        }
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
            cnnClosed = true;
        }
        this.consumer = null;
        return chClosed && cnnClosed;
    }

    @Transactional
    public void addLog(String message) {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Message);
        repository.save(log);
    }

    @Transactional
    public void addException(String message)
    {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Exception);
        repository.save(log);
    }

    @Transactional
    public void addWarning(String message)
    {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Warning);
        repository.save(log);
    }

    public void addException(RMXException e) {
        this.addException(e.html());
    }

    public static String toHtml(String string)
    {
        return string
                .replace("\n", "<br/>")
                .replace("COMPLETED", "<span style=\"color=green;\">COMPLETED</span>")
                .replace("FAILED", "<span style=\"color=red;\">FAILED</span>");
    }


    public void startDebugQueue(String... topics) throws IOException, TimeoutException {
        startDebugQueue(null, topics);
    }

    public void startDebugQueue(final Consumer consumer, String... topics) throws IOException, TimeoutException {
        List<String> argv = Arrays.asList("debug.#", "#.log", "#.error", "#.warning", "#.exception");
        for (String s : topics)
            argv.add(s);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(DEBUG_EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        if (argv.size() < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        for (String bindingKey : argv) {
            channel.queueBind(queueName, DEBUG_EXCHANGE_NAME, bindingKey);
        }

        print(" [WebBugger] Waiting for messages. To exit press CTRL+C");

        this.consumer = consumer != null ? consumer : this.defaultConsumer();
        channel.basicConsume(queueName, true, this.consumer);
        final LogService service = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (service != null) {
                    try {
                        service.closeServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private Consumer defaultConsumer() {
        final LogService thisInstance = this;
        final ObjectMapper mapper = new ObjectMapper();
        return new DefaultConsumer(this.channel) {
            //TODO should this be transactional?
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                String topic = envelope.getRoutingKey().toLowerCase();
                String log = "";
                Map map = null;
                if (message.startsWith("{") && message.endsWith("}"))
                    try {
                        map = mapper.readValue(message, new TypeReference<Map<String, String>>() {
                        });
                    } catch (JsonParseException e) {
                        print("Failed to parse as JSON: " + e.getMessage());
                    } finally {
                        if (map != null) {
                            log += "Via "+topic+":";
                            for (Object key : map.keySet()) {
                                log += "\n   --> " + key + ": " + map.get(key);
                            }
                        } else {
                            log += message + " (via "+topic+")";
                        }


                    }
                else
                    log = message + " (via "+topic+")";


                print(log);
                if (topic.contains("error") || topic.contains("exception"))
                    thisInstance.addException(log);
                else if (topic.contains("warning"))
                    thisInstance.addWarning(log);
                else
                    thisInstance.addLog(log);
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
    }

}
