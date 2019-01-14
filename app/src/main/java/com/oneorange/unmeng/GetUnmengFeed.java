package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ToastUtil;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.Comment;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.CommentResponse;
import com.umeng.comm.core.nets.responses.FeedItemResponse;
import com.umeng.comm.core.nets.responses.SimpleResponse;

import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class GetUnmengFeed {

    private Context context;
    private String feedid;
    private Handler handler;

    private Comment comment;

    private FeedItem feedItem;


    private int position;

    private final int FEEDDETAIL_SUCCESS = 1;
    private final int FEECOMMENT_SUCCESS = 2;
    private final int FEEDADD_SUCCESS = 3;
    private final int LIKEFEED_SUCCESS = 5;
    private final int ULIKEFEED_SUCCESS = 6;

    public GetUnmengFeed(Context context, String feedid, Handler handler) {
        this.context = context;
        this.feedid = feedid;
        this.handler = handler;
    }

    public GetUnmengFeed(Context context, Handler handler, Comment comment) {
        this.context = context;
        this.handler = handler;
        this.comment = comment;
    }

    public GetUnmengFeed(Context context, Handler handler, FeedItem feedItem) {
        this.context = context;
        this.handler = handler;
        this.feedItem = feedItem;
    }


    public void getFeedDetail() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchFeedWithId(feedid, new Listeners.FetchListener<FeedItemResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedItemResponse feedItemResponse) {
                FeedItem feedItem = feedItemResponse.result;
                Message msg = new Message();
                msg.what = FEEDDETAIL_SUCCESS;
                msg.obj = feedItem;
                handler.sendMessage(msg);
            }
        });
    }


    public void getFeedComment() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchFeedComments(feedid, Comment.CommentOrder.ASC, new Listeners.SimpleFetchListener<CommentResponse>() {
            @Override
            public void onComplete(CommentResponse commentResponse) {
                List<Comment> comments = commentResponse.result;
                Message msg = new Message();
                msg.what = FEECOMMENT_SUCCESS;
                msg.obj = comments;
                handler.sendMessage(msg);
            }
        });
    }


    public void addFeedComment() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.postComment(comment, new Listeners.FetchListener<SimpleResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SimpleResponse simpleResponse) {
                if (simpleResponse.errCode == ErrorCode.NO_ERROR) {
                    handler.sendEmptyMessage(FEEDADD_SUCCESS);
                } else {
                    ToastUtil.show(context, R.string.service_error, 100);
                }

            }
        });
    }

    public void creatFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.postFeed(feedItem, new Listeners.SimpleFetchListener<FeedItemResponse>() {
            @Override
            public void onComplete(FeedItemResponse feedItemResponse) {
                FeedItem feed = feedItemResponse.result;
                LogUtil.d("feed", feed.toString());
            }
        });
    }




    public void likeFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.postLike(feedid, new Listeners.SimpleFetchListener<SimpleResponse>() {
            @Override
            public void onComplete(SimpleResponse simpleResponse) {
                if (simpleResponse.errCode == ErrorCode.NO_ERROR) {
                    handler.sendEmptyMessage(LIKEFEED_SUCCESS);
                } else {

                }
            }
        });
    }

    public void ulikeFeed() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.postUnLike(feedid, new Listeners.SimpleFetchListener<SimpleResponse>() {
            @Override
            public void onComplete(SimpleResponse simpleResponse) {
                if (simpleResponse.errCode == ErrorCode.NO_ERROR) {
                    handler.sendEmptyMessage(ULIKEFEED_SUCCESS);
                } else {

                }
            }
        });
    }
}
