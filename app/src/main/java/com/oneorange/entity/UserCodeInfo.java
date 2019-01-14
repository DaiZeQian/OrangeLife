package com.oneorange.entity;

/**
 * Created by admin on 2016/5/20.
 * 用户数据表
 */
public class UserCodeInfo {

    private int code;
    private UserInfo data;
    private String msg;


    public UserCodeInfo() {
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
