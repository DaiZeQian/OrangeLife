package com.oneorange.entity;

/**
 * Created by admin on 2016/6/14.
 */
public class RegistInfo {

    private String nickname; //昵称
    private String token;    //token
    private String avatar;   //头像
    private String hxname;   //环信帐号
    private String hxpwd;    //环信密码
    private String timestamp;//登录时间


    public RegistInfo() {
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHxname() {
        return hxname;
    }

    public void setHxname(String hxname) {
        this.hxname = hxname;
    }

    public String getHxpwd() {
        return hxpwd;
    }

    public void setHxpwd(String hxpwd) {
        this.hxpwd = hxpwd;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
