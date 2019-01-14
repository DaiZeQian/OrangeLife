package com.oneorange.entity;

/**
 * Created by admin on 2016/6/15.
 */
public class ChatSearchInfo {

    private String cheng_id;//个人的时候为个人id  群组的时候为群id
    private String nickname;
    private String url;
    private String type;//type0 个人   ，1 群组


    public ChatSearchInfo() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
