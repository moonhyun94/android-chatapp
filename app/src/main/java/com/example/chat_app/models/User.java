package com.example.chat_app.models;

public class User {
    private String profile_pic_url;
    private String nickName;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String statusMsg;

    public User() {
    }

    public User(String profile_pic_url, String nickName, String name, String email, String password, String phoneNumber, String statusMsg) {
        this.profile_pic_url = profile_pic_url;
        this.nickName = nickName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.statusMsg = statusMsg;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public String getNickName() {
        return nickName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
}
