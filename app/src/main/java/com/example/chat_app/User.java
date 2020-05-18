package com.example.chat_app;

public class User {
    private final String userName;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;

    public User(String userName, String name, String email, String password, String phoneNumber) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
