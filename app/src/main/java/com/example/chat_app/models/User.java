package com.example.chat_app.models;

public class User {
    private int profile_pic;
    private String userName;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String statusMsg;

    public User() {
    }

    public User(int profile_pic , String userName, String name, String email, String password, String phoneNumber, String statusMsg) {
        this.profile_pic = profile_pic;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.statusMsg = statusMsg;
    }

    public int getProfile_pic() {
        return profile_pic;
    }

    public String getUserName() {
        return userName;
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
