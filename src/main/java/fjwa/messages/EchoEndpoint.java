package fjwa.messages;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by bilbowm on 26/10/2015.
 */
public class EchoEndpoint extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        session.addMessageHandler(
                new EchoMessageHandler(remoteEndpointBasic)
        );
    }

    private static class EchoMessageHandler implements MessageHandler.Whole<String> {


        private final RemoteEndpoint.Basic remoteEndpointBasic;

        private EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic)
        {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }
        @Override
        public void onMessage(String message) {
            if (remoteEndpointBasic != null)
                try {
                    remoteEndpointBasic.sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}