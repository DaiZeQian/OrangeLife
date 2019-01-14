package com.oneorange.unmeng.unmengentity;

import com.umeng.comm.core.beans.Topic;

/**
 * Created by admin on 2016/6/22.
 */
public class CreatFeedInfo {


    private Topic topic;
    private boolean isChoose;


    public CreatFeedInfo(Topic topic) {
        this.topic = topic;
        this.isChoose = false;
    }

    public CreatFeedInfo(Topic topic, boolean isChoose) {
        this.topic = topic;
        this.isChoose = isChoose;
    }

    public CreatFeedInfo() {
        this.isChoose = false;
    }


    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
