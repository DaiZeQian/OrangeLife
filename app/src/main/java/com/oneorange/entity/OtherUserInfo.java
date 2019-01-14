package com.oneorange.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/16.
 */
public class OtherUserInfo implements Serializable {

    private String name;
    private String cheng_id;
    private String nickname;
    private String gender;
    private String avatar;
    private String signature;


    public OtherUserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheng_id() {
        return cheng_id;
    }

    public void setCheng_id(String cheng_id) {
        this.cheng_id = cheng_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
