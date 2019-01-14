package com.oneorange.entity;

/**
 * Created by admin on 2016/6/12.
 */
public class LoginInfo {

    private String phone;
    private String psd;
    private String code;

    public LoginInfo() {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsd() {
        return psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
