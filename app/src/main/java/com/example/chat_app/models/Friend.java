package com.example.chat_app.models;

public class Friend {
    private int profile_pic;
    private String userName;
    private String name;
    private String email;
    private String phoneNum;
    private String statusMsg;

    public Friend() {
    }

    public Friend(int profile_pic, String userName, String name, String email, String phoneNum, String statusMsg) {
        this.profile_pic = profile_pic;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
}
