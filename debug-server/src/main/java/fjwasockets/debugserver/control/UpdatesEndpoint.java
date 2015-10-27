package fjwasockets.debugserver.control;

import click.rmx.debug.Bugger;
import fjwasockets.debugserver.service.LogService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 27/10/2015.
 */
@ServerEndpoint(value = "/updates")
public class UpdatesEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        LogService service = LogService.getInstance();
        if (service == null) {
           throw new NullPointerException("LogService Was Not initialized");
        }

        this.session = session;

        print("CONNECTED");
//        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        service.addClient(this);
    }

    @OnMessage
    public String echo(String message) {
        return message + " (from your server)";
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        print("DISCONNECTED");
//        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        LogService.getInstance().removeClient(this);

    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        error.printStackTrace();
        print(error);
//        LogService.getInstance().removeSubscriber(session.getBasicRemote());
    }

    public void broadcast(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message, Bugger::print); //TODO: Check Async didnt break anything
    }


}