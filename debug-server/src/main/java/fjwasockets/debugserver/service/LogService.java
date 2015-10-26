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

    @Resource//(type = LogRepository.class)
    private LogRepository repository;

    public boolean isActive()
    {
        return channel != null && connection != null
                && channel.isOpen() && connection.isOpen();
    }

    public boolean closeServer() throws RMXException {
        boolean chClosed = false, cnnClosed = false;
        String errors = "";
        if (this.channel != null) {
            try {
                this.channel.close();
            } catch (Exception e) {
                e.printStackTrace();
                errors += e.getMessage();
            }
            if (channel != null && !channel.isOpen()) {
                this.channel = null;
                chClosed = true;
            }
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (IOException e) {
                e.printStackTrace();
                errors += "\n" + e.getMessage();
            }
            if (connection != null && !connection.isOpen()) {
                this.connection = null;
                cnnClosed = true;
            }
        }
        if (!errors.isEmpty())
            throw RMXException.unexpected("Failed to close server with exceptions: \n" + errors);
        return chClosed && cnnClosed;
    }

    public Log addLog(String message) {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Message);
        return log;
    }

    public Log addException(String message)
    {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Exception);
        return log;
    }

    public Log addWarning(String message)
    {
        Log log = new Log();
//        log.setTimeStamp(Instant.now().toEpochMilli());
        log.setMessage(message);
        log.setLogType(LogType.Warning);
        return log;
    }

    public Log addException(RMXException e) {
        return this.addException(e.html());
    }

    public static String toHtml(String string)
    {
        return string
                .replace("\n", "<br/>")
                .replace("COMPLETED", "<span style=\"color=green;\">COMPLETED</span>")
                .replace("FAILED", "<span style=\"color=red;\">FAILED</span>");
    }


    public void startDebugQueue(String... topics) throws Exception {
        startDebugQueue(null, topics);
    }

    public void startDebugQueue(Consumer consumer, String... topics) throws IOException, TimeoutException {
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

        if (consumer == null)
            consumer = this.defaultConsumer();
        channel.basicConsume(queueName, true, consumer);
        final LogService service = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (service != null) {
                    try {
                        service.closeServer();
                    } catch (Exception e) {
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
                try {
                    if (topic.contains("error") || topic.contains("exception"))
                        save(thisInstance.addException(log));
                    else if (topic.contains("warning"))
                        save(thisInstance.addWarning(log));
                    else
                        save(thisInstance.addLog(log));
//                channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Transactional
    public void save(Log log)
    {
        repository.save(log);
    }

}
