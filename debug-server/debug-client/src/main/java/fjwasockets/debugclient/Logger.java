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

    default void logWarning(String message)
    {
        try {
            send(message, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void logException(String message)
    {
        try {
            send(message, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void logMessage(String message)
    {
        try {
            send(message, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void logWarning(Object object)
    {
        try {
            send(object, "debug.warning");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void logException(Object object)
    {
        try {
            send(object, "debug.error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void logMessage(Object object)
    {
        try {
            send(object, "debug.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}