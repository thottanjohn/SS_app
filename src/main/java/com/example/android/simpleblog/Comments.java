package com.example.android.simpleblog;

import android.text.format.DateFormat;

import java.util.Date;

public class Comments {

    private String message, user_id,user_name,user_image;
    private Date timestamp;

    public Comments(){

    }



    public Comments(String message, String user_id, Date timestamp, String user_name, String user_image) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.user_name =user_name;
        this.user_image =user_image;


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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }
    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}