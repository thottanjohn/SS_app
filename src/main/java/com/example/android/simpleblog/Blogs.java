package com.example.android.simpleblog;

public class Blogs {
    private String Title;


    private String Description;
    private String ImageUrl;
    private int likes;

    public Blogs(){

    }

    public Blogs(String title, String description, String imageUrl,int likes) {
        Title = title;
        Description = description;
        ImageUrl = imageUrl;


    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {


        this.likes = likes;
    }

}
