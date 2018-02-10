package com.example.administrator.chatapp.Db.domain;

/**
 * Created by Administrator on 2018/1/25.
 */

public class ChatInfo {
    private String name;
    private String contentChat;
    private String data;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentChat() {
        return contentChat;
    }

    public void setContentChat(String contentChat) {
        this.contentChat = contentChat;
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "name='" + name + '\'' +
                ", contentChat='" + contentChat + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
