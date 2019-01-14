package com.oneorange.entity;

/**
 * Created by admin on 2016/6/13.
 * <p/>
 * 用户选择统一实体类
 */
public class UserChoose {

    private String content;
    private int resourse;

    private boolean isChoose;


    public UserChoose() {
    }

    public UserChoose(String content) {
        this.content = content;
        this.isChoose = false;
    }

    public UserChoose(String content, boolean isChoose) {
        this.content = content;
        this.isChoose = isChoose;
    }


    public UserChoose(String content, int resourse, boolean isChoose) {
        this.content = content;
        this.resourse = resourse;
        this.isChoose = isChoose;
    }

    public int getResourse() {
        return resourse;
    }

    public void setResourse(int resourse) {
        this.resourse = resourse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
