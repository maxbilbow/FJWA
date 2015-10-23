package fjwasockets.controller;

import fjwasockets.messages.Message;
import fjwasockets.messages.OutputMessage;
import fjwasockets.sockjs.SocketSessionsCreator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.sockjs.transport.SockJsSession;

import java.time.Instant;
import java.util.Date;

/**
 * Created by bilbowm on 22/10/2015.
 */
@Controller
@RequestMapping("/")
public class DebugController {


    SocketSessionsCreator socketSessions = new SocketSessionsCreator();

    public DebugController() {
        new Thread(() -> {
            while (this != null) {

                Message m = new Message(94,Instant.now() + "Tick..." );
                try {
                    SockJsSession session = (SockJsSession) socketSessions.
                            connectToLocalUri("/debug");
                    session.sendMessage(new TextMessage(m.getMessage()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println(m.getMessage());
                try {

                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @MessageMapping("/debug/logs")
    @SendTo({"/topic/debug/logs"})
    public OutputMessage sendlog(Message message) {
        message.setMessage("LOG: " + message.getMessage());
        System.out.println(message.getMessage());
        return new OutputMessage(message, new Date());
    }

    @MessageMapping("/debug/errors")
    @SendTo({"/topic/debug/errors"})
    public OutputMessage senderror(Message message) {
        message.setMessage("ERR: " + message.getMessage());
        System.out.println(message.getMessage());
        return new OutputMessage(message, new Date());
    }
}
