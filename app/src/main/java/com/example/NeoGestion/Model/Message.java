package com.example.NeoGestion.Model;

public class Message {
    private String content;
    private boolean sentByUser;

    public Message(String content, boolean sentByUser) {
        this.content = content;
        this.sentByUser = sentByUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSentByUser() {
        return sentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        this.sentByUser = sentByUser;
    }
}
