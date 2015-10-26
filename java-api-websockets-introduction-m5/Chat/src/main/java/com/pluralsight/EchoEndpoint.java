package com.pluralsight;

import javax.websocket.*;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class EchoEndpoint extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        session.addMessageHandler(new EchoMessageHandler(remoteEndpointBasic));
    }

    private static class EchoMessageHandler
            implements MessageHandler.Whole<String> {

        private final RemoteEndpoint.Basic remoteEndpointBasic;

        private EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic) {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }

        @Override
        public void onMessage(String message) {
            try {
                if (remoteEndpointBasic != null) {
                    remoteEndpointBasic.sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
