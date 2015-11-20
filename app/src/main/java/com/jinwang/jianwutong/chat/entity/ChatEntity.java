package com.jinwang.jianwutong.chat.entity;

/**
 * Created by Chenss on 2015/10/27.
 */
public class ChatEntity {
    private Integer id;
    private String sender;
    private String receiver;
    private String msg;
    private String time;

    public ChatEntity(){

    }


    public ChatEntity(int id,String sender,String to, String msg, String time) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.time = time;
    }

    public ChatEntity(String sender,String receiver, String msg, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
