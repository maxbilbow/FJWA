package com.pluralsight.messages;

import com.pluralsight.User;

import java.util.List;

/**
 * Created by kevinj.
 */
public class UserListMessage extends Message {

    public UserListMessage(List<User> users){
        this.setType(4);
        this.users = users;
    }
    public List<User> users;
}
