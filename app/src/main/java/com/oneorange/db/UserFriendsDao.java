package com.oneorange.db;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2016/6/15.
 */
public class UserFriendsDao extends DataSupport {

    private String userid;
    private String nickname;
    private String avatar;

    public UserFriendsDao() {
    }

    public UserFriendsDao(String userid, String nickname, String avatar) {
        this.userid = userid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
}
