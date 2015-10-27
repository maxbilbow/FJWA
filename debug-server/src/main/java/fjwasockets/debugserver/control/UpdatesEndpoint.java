package fjwasockets.debugserver.control;

import fjwasockets.debugserver.service.LogService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import static click.rmx.debug.Bugger.print;

/**
 * Created by bilbowm on 27/10/2015.
 */
@ServerEndpoint(value = "/updates")
public class UpdatesEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        LogService service = LogService.getInstance();
        if (service == null) {
           throw new NullPointerException("LogService Was Not initialized");
        }

        print("CONNECTED");
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        service.addSubscriber(remoteEndpointBasic);


    }

    @OnMessage
    public String echo(String message) {
        return message + " (from your server)";
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        print("DISCONNECTED");
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        LogService.getInstance().removeSubscriber(remoteEndpointBasic);

    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        error.printStackTrace();
        print(error);
    }


}