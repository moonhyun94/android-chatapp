package com.example.chat_app.models;

import android.graphics.drawable.Drawable;

public class UserImage {
    private String imageName;
    private String imageUrl;

    public UserImage() {
    }

    public UserImage(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
