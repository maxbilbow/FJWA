package com.pluralsight;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/websocket/echoa")
public class EchoEndpointAnnotation {

    @OnOpen
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
