package fjwa.model;

import click.rmx.debug.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by bilbowm on 21/10/2015.
 */
public class WebSocketDescTest {

    WebSocketDesc[] sockets;
    final String address = "echo.websocket.org";
    final String wsUri  = "ws://"  + address;
    final String wssUri = "wss://" + address;
    final String [] uris = {
            address,
            address + "/",
            "ws://"  + address,
            "ws://"  + address +"/",
            "wss://" + address,
            "wss://" + address +"/",
    };


    @Before
    public void setUp() throws Exception {
        sockets = new WebSocketDesc[uris.length];
        int i = 0;
        for (String uri : uris)
            sockets[i++] = new WebSocketDesc(uri);
    }

    @After
    public void tearDown() throws Exception {
        sockets = null;
    }

    @Test
    public void testDefuaultGetUri() throws Exception {
        int i = 0;
        final String expected = wsUri;
        for (WebSocketDesc socket : sockets) {
            //setup
            final String uri = socket.getUri();
            String failMessage =  "INSECURE socket uri was '"+uri+"' but expected '"+expected+"'" +
                    ". isSecure() == " + socket.isSecure() + "\n" +
                    " -> Given uri was: " + i +": " + uris[i++];

            //execute
            assertTrue( "Socket should be non-secure by default!", socket.isSecure() == false);
            assertTrue(failMessage, uri.equals(expected));

            //Display info
            Tests.note("NON-SECURE: " + uri);
        }
    }

    @Test
    public void getSecureUri() throws Exception {
        int i = 0;
        final String expected = wssUri;
        for (WebSocketDesc socket : sockets) {
            //setup
            socket.setSecure(true);
            final String uri = socket.getUri();
            String failMessage =  "SECURE socket uri was '"+uri+"' but expected '"+expected+"'" +
                    ". isSecure() == " + socket.isSecure() + "\n" +
                    " -> Given uri was: " + uris[i++];

            //execute
            assertTrue(failMessage, uri.equals(expected));

            //Display info
            Tests.note("SECURE: " + uri);
        }
    }
}