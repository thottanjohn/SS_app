package com.example.android.simpleblog;


import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BlogPost extends BlogPostId {

    private FirebaseFirestore firebaseFirestore;
    public String user_id, image_url, desc, image_thumb,user_name,user_image;
    public Date timestamp;
    int likes;

    public BlogPost() {}



    public BlogPost(String user_id, String image_url, String desc, String image_thumb, Date timestamp, int likes, String user_name, String user_image) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.timestamp = timestamp;
        this.likes=likes;
        this.user_name = user_name;

        this.user_image = user_image;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



}
