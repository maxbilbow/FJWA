package fjwasockets.debugclient;

import com.rabbitmq.client.AMQP;

import java.util.Date;

/**
 * Created by bilbowm on 27/10/2015.
 */
public interface Logger {

    /**
     *
     * @param message
     * @param routing
     * @throws Exception
     */
    void send(String message,  String... routing)
            throws Exception;

    default AMQP.BasicProperties defaultProperties()
    {
        return new AMQP.BasicProperties.Builder()
            .appId(getAppId())
            .timestamp(new Date())
            .build();
    }

    String getAppId();


    /**
     *
     * @param object
     * @param routing
     * @throws Exception
     */
    void send(Object object, String... routing) throws Exception;

    default boolean logWarning(String message)
    {
        try {
            send(message, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    default boolean logException(String message)
    {
        try {
            send(message, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    default boolean logMessage(String message)
    {
        try {
            send(message, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    default boolean logWarning(Object object)
    {
        try {
            send(object, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    default boolean logException(Object object)
    {
        try {
            send(object, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    default boolean logMessage(Object object)
    {
        try {
            send(object, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void setHost(String host);

    void setPort(Integer port);

    void setUri(String uri);

    void setVirtualHost(String virtualHost);

    void setUsername(String username);

    void setPassword(String password);
}