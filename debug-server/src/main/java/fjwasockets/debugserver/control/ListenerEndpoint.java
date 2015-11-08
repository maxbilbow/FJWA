package fjwasockets.debugserver.control;

import fjwasockets.debugserver.coders.LogDecoder;
import fjwasockets.debugserver.coders.LogEncoder;
import fjwasockets.debugserver.model.Log;
import fjwasockets.debugserver.model.LogType;
import fjwasockets.debugserver.service.LogService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by bilbowm on 05/11/2015.
 */
@ServerEndpoint(value = "/listener/{topic}",
                decoders = LogDecoder.class,
                encoders = LogEncoder.class)
public class ListenerEndpoint {
    private Session session;

    private final LogService service;

    public ListenerEndpoint()
    {
        service = LogService.getInstance();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig,  @PathParam("topic") String topic) {
        if (service == null) {
            throw new NullPointerException("LogService Was Not initialized");
        }

        this.session = session;
        Log log = service.addLog("Client connected: " + session.getId());
        service.save(log);
        service.notifySubscribers(log);
    }



    @OnMessage
    public void onMessage(Log log, @PathParam("topic") String topic) {
        if (log.getLogType()==null) {
            if (topic.toLowerCase().contains("error") ||
                    topic.toLowerCase().contains("exception"))
                log.setLogType(LogType.Exception);
            else if (topic.toLowerCase().contains("warning"))
                log.setLogType(LogType.Warning);
            else
                log.setLogType(LogType.Message);
        }
        service.save(log);
        service.notifySubscribers(log);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason, @PathParam("topic") String topic)
    {
        Log log = service.addLog("Client disconnected: " + session.getId());
        service.save(log);
        service.notifySubscribers(log);
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("topic") String topic)
    {
        error.printStackTrace();
        Log log = service.addException("Connection Error: " + error.toString());
        service.save(log);
        service.notifySubscribers(log);
    }

    private void broadcast(Log log, String topic)
    {
        service.notifySubscribers(log);
    }
}