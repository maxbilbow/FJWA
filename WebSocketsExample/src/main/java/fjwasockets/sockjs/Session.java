package fjwasockets.sockjs;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.sockjs.transport.SockJsServiceConfig;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

import java.util.Map;

/**
 * Created by bilbowm on 22/10/2015.
 */
public class Session extends WebSocketServerSockJsSession {
    public Session(String id, SockJsServiceConfig config, WebSocketHandler handler, Map<String, Object> attributes) {
        super(id, config, handler, attributes);
    }
}
