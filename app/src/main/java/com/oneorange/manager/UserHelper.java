package com.oneorange.manager;

/**
 * Created by admin on 2016/6/14.
 * <p/>
 * 用户信息的单例
 */
public class UserHelper {

    private String userId;
    private String token;

    private static UserHelper instance = null;


    public UserHelper() {
    }

    public synchronized static UserHelper getInstance() {
        if (instance == null) {
            instance = new UserHelper();
            instance.setUserId("");
            instance.setToken("");
        }
        return instance;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
