package com.pluralsight;

import com.pluralsight.coders.MessageDecoder;
import com.pluralsight.coders.MessageEncoder;
import com.pluralsight.messages.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value="/websocket/chat/{room-name}",
                decoders=MessageDecoder.class,
                encoders=MessageEncoder.class)
public class ChatEndpoint {

    private static Dictionary<String, List<ChatEndpoint>> clients = new Hashtable<>();
    private static Dictionary<String, List<User>> users = new Hashtable<>();
    private static Dictionary<String, List<ChatMessage>> messages = new Hashtable<>();
    Session session;

    @OnError
    public void onError(Session session, Throwable t, @PathParam("room-name") String roomName) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.printStackTrace();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig _, @PathParam("room-name") String roomName) {
        this.session = session;
        List<ChatEndpoint> chatEndpoints = clients.get(roomName);
        if (chatEndpoints == null) {
            chatEndpoints = new CopyOnWriteArrayList<ChatEndpoint>();
            clients.put(roomName, chatEndpoints);
        }
        chatEndpoints.add(this);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason, @PathParam("room-name") String roomName) throws Exception {
        List<ChatEndpoint> chatEndpoints = clients.get(roomName);
        if (chatEndpoints == null) {
            throw new Exception("Expected a valid room");
        }
        chatEndpoints.remove(this);
    }

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();


    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, boolean complete, @PathParam("room-name") String roomName) {
        try {
            buffer.write(byteBuffer.array());
            if (complete) {

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("c:\\temp\\image.jpg");
                    fos.write(buffer.toByteArray());
                } finally {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                }
                for (ChatEndpoint client : clients.get(roomName)) {
                    final ByteBuffer sendData = ByteBuffer.allocate(buffer.toByteArray().length);
                    sendData.put(buffer.toByteArray());
                    sendData.rewind();
                    client.session.getAsyncRemote().sendBinary(sendData, new SendHandler() {
                        @Override
                        public void onResult(SendResult sendResult) {
                            System.out.println(sendResult.isOK());
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnMessage
    public void onMessage(Message message, @PathParam("room-name") String roomName) {
        if(message instanceof JoinMessage){
            processMessage((JoinMessage)message, roomName);
        }  else if (message instanceof ChatMessage) {
            processMessage((ChatMessage) message, roomName);
        } else if (message instanceof GetUsersMessage) {
            processMessage((GetUsersMessage) message, roomName);
        } else {
            System.out.println("Unknown message");
        }
    }

    private void processMessage(JoinMessage message, String roomName) {
        User user = new User();
        user.setName(message.getName());
        List<User> usersList = users.get(roomName);
        if (usersList == null) {
            usersList = new CopyOnWriteArrayList<User>();
            users.put(roomName, usersList);
        }
        usersList.add(user);
        broadcast(message, roomName);
    }

    private void processMessage(ChatMessage message, String roomName) {
        List<ChatMessage> messagesList = messages.get(roomName);
        if (messagesList == null) {
            messagesList = new CopyOnWriteArrayList<ChatMessage>();
            messages.put(roomName, messagesList);
        }
        messagesList.add(message);
        broadcast(message, roomName);
    }

    private void processMessage(GetUsersMessage message, String roomName) {
        try {
            List<User> users = ChatEndpoint.users.get(roomName) == null
                    ? new CopyOnWriteArrayList<User>()
                    : ChatEndpoint.users.get(roomName);
            session.getBasicRemote().sendObject(new UserListMessage(users));
            List<ChatMessage> messagesList = messages.get(roomName);
            if (messagesList == null) {
                messagesList = new CopyOnWriteArrayList<ChatMessage>();
                messages.put(roomName, messagesList);
            }
            session.getBasicRemote().sendObject(new ChatMessagesMessage(ChatEndpoint.messages.get(roomName)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(Message message, String roomName) {
        for (ChatEndpoint client : clients.get(roomName)) {
            try {
                client.session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                clients.remove(this);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // do nothing
                }
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}
