package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedsResponse;

import java.util.List;

/**
 * Created by admin on 2016/6/20.
 */
public class GetUnmengFeedList {

    private Context context;
    private String topicId;
    private Handler handler;
    private final int FEED_SUCCESS = 4;
    private String uid;
    private final int USERFEED_SUCCESS = 11;


    public GetUnmengFeedList(Context context, String topicId, Handler handler) {
        this.context = context;
        this.topicId = topicId;
        this.handler = handler;
    }

    public GetUnmengFeedList(Context context, Handler handler, String uid) {
        this.context = context;
        this.handler = handler;
        this.uid = uid;
    }

    public void getTopicFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchTopicFeed(topicId, new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {
                List<FeedItem> feedItems = feedsResponse.result;
                Message msg = new Message();
                msg.what = FEED_SUCCESS;
                msg.obj = feedItems;
                handler.sendMessage(msg);
            }
        });
    }


    public void getfetchRealTimeFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchRealTimeFeed(new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {
                List<FeedItem> feedItems = feedsResponse.result;
                Message msg = new Message();
                msg.what = FEED_SUCCESS;
                msg.obj = feedItems;
                handler.sendMessage(msg);
            }
        });
    }


    public void getUserFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchUserTimeLine(uid, new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {
                List<FeedItem> feedItems = feedsResponse.result;
                Message msg = new Message();
                msg.what = USERFEED_SUCCESS;
                msg.obj = feedItems;
                handler.sendMessage(msg);

            }
        });
    }
}
