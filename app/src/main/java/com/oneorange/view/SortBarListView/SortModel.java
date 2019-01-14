package com.oneorange.view.SortBarListView;


public class SortModel {

    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private String num;
    private String groupId;
    private String chengid;
    private String image;
    private boolean isChoose;


    public String getChengid() {
        return chengid;
    }

    public void setChengid(String chengid) {
        this.chengid = chengid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SortModel() {
        this.isChoose = false;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
