package com.oneorange.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/5/20.
 * 用户数据表
 */
public class UserInfo implements Serializable{


    private String name;
    private String nickname;
    private String avatar;
    private String gender;
    private String signature;
    //橙id
    private String cheng_id;
    private String extra_password;
    private String url;//新的头像字段 0628修改


    public UserInfo() {
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCheng_id() {
        return cheng_id;
    }

    public void setCheng_id(String cheng_id) {
        this.cheng_id = cheng_id;
    }

    public String getExtra_password() {
        return extra_password;
    }

    public void setExtra_password(String extra_password) {
        this.extra_password = extra_password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
