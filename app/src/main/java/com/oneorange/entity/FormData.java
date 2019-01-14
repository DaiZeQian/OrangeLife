package com.oneorange.entity;

/**
 * Created by admin on 2016/6/28.
 */
public class FormData {

    private String name;
    private String value;


    public FormData(String name, String value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
