package com.example.android.simpleblog;

import android.text.format.DateFormat;

import java.util.Date;

public class Comments {

    private String message, user_id,username,userimage;
    private Date timestamp;

    public Comments(){

    }



    public Comments(String message, String user_id, Date timestamp, String username, String userimage) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.username =username;
        this.userimage =userimage;


    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        String dateString = null;
        try {
            long millisecond = timestamp.getTime();
            dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();

        } catch (Exception e) {


        }
        return dateString;

    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_image() {
        return userimage;
    }
    public void setUser_image(String user_image) {
        this.userimage = userimage;
    }
}