package com.jinwang.jianwutong.chat.entity;

/**
 * Created by Chenss on 2015/10/22.
 */
public class MsgEntity {
    private Integer id;
    private String name;
    private String latestMsg;
    private int unread;
    private String latestTime;
    private String branch;

    public MsgEntity() {
    }

    public MsgEntity(Integer id, String name, String latestMsg, String latestTime, int unread,String branch) {
        this.branch = branch;
        this.id = id;
        this.latestMsg = latestMsg;
        this.latestTime = latestTime;
        this.name = name;
        this.unread = unread;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
