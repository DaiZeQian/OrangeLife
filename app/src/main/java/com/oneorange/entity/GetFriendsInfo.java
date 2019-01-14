package com.oneorange.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/6/15.
 */
public class GetFriendsInfo implements Serializable{


    private String cheng_id;
    private String nickname;
    private String image;


    public GetFriendsInfo() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
