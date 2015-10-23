package click.rmx.cmd.sockets;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static click.rmx.debug.Bugger.print;
/**
 * Created by bilbowm on 23/10/2015.
 */
public class RMXSocketClient extends WebSocketClient {
    public RMXSocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        print("Socket Open");
        this.send("This message was sent onOpen()");
    }

    @Override
    public void onMessage(String s) {
        print("Received Message: '" + s + "'");
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        print("Socket Closed");
    }

    @Override
    public void onError(Exception e) {
        print("ERROR: " + e.getMessage());
        e.printStackTrace();
    }

}
