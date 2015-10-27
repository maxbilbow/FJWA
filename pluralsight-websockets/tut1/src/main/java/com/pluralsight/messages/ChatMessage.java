package com.pluralsight.messages;

import java.util.Date;

public class ChatMessage extends Message {

    private String message;
    private String userName;
    private Date timeSent;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public Date getTimeSent() {
        return timeSent;
    }
}
