package fjwa.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fjwa.controller.BoomController;
import fjwa.model.Bomb;
import fjwa.repository.BombRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import java.util.List;

/**
 * Created by bilbowm on 26/10/2015.
 */
public class BombEndpoint extends Endpoint {
    @Autowired
    private BombRepository bombRepository;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();
        session.addMessageHandler(
                new EchoMessageHandler(remoteEndpointBasic,this)
        );
    }

    public static class EchoMessageHandler implements MessageHandler.Whole<String> {


        private final RemoteEndpoint.Basic remoteEndpointBasic;
        private final BombEndpoint bombEndpoint;


        private EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic, BombEndpoint bombEndpoint)
        {
            this.bombEndpoint = bombEndpoint;
            this.remoteEndpointBasic = remoteEndpointBasic;
        }
        @Override
        public void onMessage(String message) {
            try {
            if (remoteEndpointBasic != null)
                switch (message) {
                    case "findAll":
                        BoomController controller = BoomController.getInstance();
                        if (controller != null) {
                            List bombs = controller.getRepository().findAll();//.updateBombs();
                            String response = "ERROR: Failed to retrieve bomb list.";
                            if (bombs != null)
                                response = bombs.size() + " bombs found";
                            remoteEndpointBasic.sendText(encodeList(bombs));

                        }
                        else {
                            remoteEndpointBasic.sendText("controller was null!");
                        }
                        break;
                    default:
                        remoteEndpointBasic.sendText("No response for '" + message + "'");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String encodeList(List<Bomb> list)
    {
        ObjectMapper mapper = new ObjectMapper();
        String response = "Found " + list.size() + " bombs but failed to write as JSON";

        try {
            response = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
}