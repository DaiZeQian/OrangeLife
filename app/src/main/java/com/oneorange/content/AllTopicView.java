package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.oneorange.adapter.AlltopicAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUmengTopicList;
import com.umeng.comm.core.beans.Topic;

import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class AllTopicView extends BaseActivity {


    private GridView gv_topic;

    //op
    private AlltopicAdapter alltopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltopic);
        initView();
        getTopic();
    }


    private void initView() {
        gv_topic = (GridView) findViewById(R.id.gv_topic);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    List<Topic> topics = (List<Topic>) msg.obj;
                    setAlltopice(topics);
                    break;
            }
        }
    };

    private void getTopic() {
        GetUmengTopicList topicList = new GetUmengTopicList(this, handler);
        topicList.getALLTopic();
    }

    private void setAlltopice(List<Topic> topics) {
        alltopicAdapter = new AlltopicAdapter(this);
        gv_topic.setAdapter(alltopicAdapter);
        alltopicAdapter.setAdapterDatas(topics);
        alltopicAdapter.notifyDataSetChanged();
    }

}
