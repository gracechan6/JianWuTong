package com.jinwang.jianwutong.chat.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chenss on 2015/11/16.
 */
public class PushEntity {
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("text")
    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
