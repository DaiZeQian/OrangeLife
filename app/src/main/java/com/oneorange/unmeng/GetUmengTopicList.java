package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.TopicResponse;

import java.util.List;

/**
 * Created by admin on 2016/6/20.
 */
public class GetUmengTopicList {

    private Context context;
    private Handler handler;

    public GetUmengTopicList(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void getALLTopic() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchTopics(new Listeners.FetchListener<TopicResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(TopicResponse topicResponse) {
                List<Topic> topics = topicResponse.result;
                Message msg = new Message();
                msg.what = 3;
                msg.obj = topics;
                handler.sendMessage(msg);
            }
        });
    }
}

