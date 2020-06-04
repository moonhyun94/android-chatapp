package com.example.chat_app.models;

public class ChatRoom {
    private String roomName;
    private String lastMsg;
    private String participant;
    private String participantEmail;

    public ChatRoom() {
    }

    public ChatRoom(String roomName, String lastMsg, String participant, String participantEmail) {
        this.lastMsg = lastMsg;
        this.roomName = roomName;
        this.participant = participant;
        this.participantEmail = participantEmail;
    }

    public String getParticipantEmail() { return participantEmail; }

    public String getParticipant() { return participant; }

    public String getRoomName() { return roomName; }

    public String getLastMsg() {
        return lastMsg;
    }
}
