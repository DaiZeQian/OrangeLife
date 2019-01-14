package com.oneorange.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/15.
 */
public class GetFriendsCodeInfo {

    private int code;
    private List<GetFriendsInfo> data;


    public GetFriendsCodeInfo() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<GetFriendsInfo> getData() {
        return data;
    }

    public void setData(List<GetFriendsInfo> data) {
        this.data = data;
    }
}
