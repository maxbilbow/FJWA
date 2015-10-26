package com.pluralsight.messages;

import java.util.List;

/**
 * Created by kevinj.
 */
public class ChatMessagesMessage extends Message {
    public List<ChatMessage> messages;

    public ChatMessagesMessage(List<ChatMessage> messages) {
        this.setType(6);
        this.messages = messages;
    }
}
