package com.oneorange.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2016/5/30.
 */
public class NewFriendsDao extends DataSupport {

    private String userid;
    private String username;
    private String groupid;
    private String groupname;


    private String time;
    private String reason;
    private String status;
    private String isInviteFromMe;
    private String groupinviter;


    private int id;


    public NewFriendsDao() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsInviteFromMe() {
        return isInviteFromMe;
    }

    public void setIsInviteFromMe(String isInviteFromMe) {
        this.isInviteFromMe = isInviteFromMe;
    }

    public String getGroupinviter() {
        return groupinviter;
    }

    public void setGroupinviter(String groupinviter) {
        this.groupinviter = groupinviter;
    }

}
