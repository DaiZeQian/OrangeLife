package com.oneorange.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/15.
 */
public class ChatSearchCodeInfo {

    private int code;
    private List<ChatSearchInfo> data;

    public ChatSearchCodeInfo() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ChatSearchInfo> getData() {
        return data;
    }

    public void setData(List<ChatSearchInfo> data) {
        this.data = data;
    }
}
