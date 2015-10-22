package fjwasockets.sockjs;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.transport.SockJsServiceConfig;
import org.springframework.web.socket.sockjs.transport.SockJsSession;
import org.springframework.web.socket.sockjs.transport.SockJsSessionFactory;
import org.springframework.web.socket.sockjs.transport.handler.DefaultSockJsService;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

import javax.websocket.ClientEndpoint;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.net.URI;
import java.util.Map;


/**
 * Created by bilbowm on 22/10/2015.
 */
@ClientEndpoint
public class SocketSessionsCreator extends Endpoint implements WebSocketHandler {

    private final SockJsSessionFactory sessionFactory;

    public SocketSessionsCreator() {
        SockJsServiceConfig serviceConfig = new DefaultSockJsService(new ThreadPoolTaskScheduler());
        sessionFactory = new SockJsSessionFactory() {
            @Override
            public SockJsSession createSession(String sessionId, WebSocketHandler handler, Map<String, Object> attributes) {
                return new WebSocketServerSockJsSession(sessionId, serviceConfig, handler, attributes);
            }
        };
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.err.println(" >>>> >>>> >>> >> SESSION OPEN");
    }

    public Object connectToUri(String url) throws Exception {
        URI uri = new URI(url);
        SockJsSession session = sessionFactory.createSession("fjwa", this, null);
//        Session session = socketFactory.connectToServer(this,uri);
//        if (session.isOpen()){
//            System.out.println("Session is open");
//        } else {
//            System.err.println("Session is closed");
//        }

        return session;
    }

    public Object connectToLocalUri(String uri) throws Exception {
        return connectToUri("http://localhost:8079/spring-ng-chat" + uri);
    }

    private static void print(Object o) {
        System.err.println(o);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        print("<>><><>ESTABLISED<><><><><><><>");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        print("<>><><>HANDLEMESSAGE<><><><><><><>");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        print("<>><><>handleTransportError<><><><><><><>");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        print("<>><><>afterConnectionClosed<><><><><><><>");
    }

    @Override
    public boolean supportsPartialMessages() {
        print("<>><><>supportsPartialMessages<><><><><><><>");
        return false;
    }
}
