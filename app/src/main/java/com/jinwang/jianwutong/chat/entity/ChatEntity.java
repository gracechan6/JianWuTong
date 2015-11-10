package com.jinwang.jianwutong.chat.entity;

/**
 * Created by Chenss on 2015/10/27.
 */
public class ChatEntity {
    private String name;
    private String text;
    private String time;


    public ChatEntity(String name, String text, String time) {
        this.name = name;
        this.text = text;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
