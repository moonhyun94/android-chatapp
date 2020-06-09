package com.example.chat_app.models;

public class Friend {
    private String profile_pic_url;
    private String nickName;
    private String name;
    private String email;
    private String phoneNum;
    private String statusMsg;

    public Friend() {
    }

    public Friend(String profile_pic_url, String nickName, String name, String email, String phoneNum, String statusMsg) {
        this.profile_pic_url = profile_pic_url;
        this.nickName = nickName;
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
}
