package com.pluralsight.coders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.messages.ChatMessage;
import com.pluralsight.messages.GetUsersMessage;
import com.pluralsight.messages.JoinMessage;
import com.pluralsight.messages.Message;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * Created by kevinj.
 */
public class MessageDecoder implements Decoder.Text<Message> {

    static final int JOIN_MESSAGE = 1;
    static final int CHAT_MESSAGE = 2;
    static final int GETUSERS_MESSAGE = 3;
    static final int USERSLIST_MESSAGE = 4;
    static final int GETMESSAGES_MESSAGE = 5;
    static final int MESSAGES_MESSAGE = 6;

    @Override
    public Message decode(String msg) throws DecodeException {
        Message message = null;

        if(willDecode(msg)){
            try {
                JsonObject obj = Json.createReader(new StringReader(msg)).readObject();
                ObjectMapper mapper = new ObjectMapper();

                int type = obj.getInt("type");

                switch (type) {
                    case JOIN_MESSAGE:
                        message = mapper.readValue(msg, JoinMessage.class);
                        break;

                    case CHAT_MESSAGE:
                        message = mapper.readValue(msg, ChatMessage.class);
                        break;

                    case GETUSERS_MESSAGE:
                        message = mapper.readValue(msg, GetUsersMessage.class);
                        break;
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        return message;
    }

    @Override
    public boolean willDecode(String msg) {
        try {
            Json.createReader((new StringReader(msg)));
            return true;
        } catch (JsonException e){
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
