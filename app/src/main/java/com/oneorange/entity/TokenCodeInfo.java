package com.oneorange.entity;

/**
 * Created by admin on 2016/6/28.
 */
public class TokenCodeInfo {
    private int code;
    private TokenInfo data;
    private String msg;


    public TokenCodeInfo() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public TokenInfo getData() {
        return data;
    }

    public void setData(TokenInfo data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
