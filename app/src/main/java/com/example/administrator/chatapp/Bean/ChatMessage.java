package com.example.administrator.chatapp.Bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/11.
 */

public class ChatMessage {
    public String name;
    public String msg;
    public Date date;
    public Type type;

    public ChatMessage() {
    }

    public ChatMessage(String msg, Type type) {
        this.msg = msg;
        this.type = type;
    }

    public ChatMessage(String msg, Date date, Type type) {
        this.msg = msg;
        this.date = date;
        this.type = type;
    }

    public enum Type{
        OUT,IN
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
