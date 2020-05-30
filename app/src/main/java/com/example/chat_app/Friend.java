package com.example.chat_app;

public class Friend {
    private int id;
    private String name;
    private String statusMsg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Friend() {
    }

    public Friend(int id, String name, String statusMsg) {
        this.id = id;
        this.name = name;
        this.statusMsg = statusMsg;
    }
}
