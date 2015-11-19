package com.jinwang.jianwutong.chat.entity;

/**
 * Created by Chenss on 2015/10/27.
 */
public class ChatEntity {
    private Integer id;
    private String from;
    private String to;
    private String msg;
    private String time;

    public ChatEntity(){

    }


    public ChatEntity(int id,String from,String to, String msg, String time) {
        this.from = from;
        this.to = to;
        this.msg = msg;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
