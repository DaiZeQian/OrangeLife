package com.oneorange.entity;

/**
 * Created by admin on 2016/6/14.
 */
public class VerifyCodeInfo {

    private int code;
    private boolean isSuccess;
    private String msg;


    public VerifyCodeInfo() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
