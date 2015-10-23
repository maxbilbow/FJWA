package click.rmx.cmd;


import click.rmx.cmd.sockets.RMXSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import static click.rmx.debug.Bugger.print;
/**
 * Created by bilbowm on 23/10/2015.
 */
public class CmdSocketTool extends CmdBasic{
    public static void start(String url)
    {
        print("Conecting to socket: " + url);
        try {
            RMXSocketClient client = new RMXSocketClient(new URI(url));
            client.connect();
//            if (client.getConnection().isClosed())
//                client.connect();
//            client.send("THIS IS A MESSAGE");

            while(client.getConnection().isConnecting()){
                System.out.print(".");
            }
//            client.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
