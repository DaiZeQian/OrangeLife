package com.oneorange.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUmengTopicList;
import com.oneorange.unmeng.GetUnmengFeed;
import com.oneorange.unmeng.GetUnmengFeedList;
import com.oneorange.unmeng.UnmengAdapter.FeedAdapter;
import com.oneorange.unmeng.UnmengAdapter.TopicAdapter;
import com.oneorange.view.HorizontalListView;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;

import java.util.List;

/**
 * Created by admin on 2016/5/17.
 */
public class SquareFragment extends BaseFragment {

    private ListView lv_feed;
    private HorizontalListView hlv_topic;
    private TextView tv_more;
    private ImageView iv_add;
    //op
    private TopicAdapter topicAdapter;
    private FeedAdapter feedAdapter;

    private int likeposition;


    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_squaret, null);
        initView(view);
        setOnClickListener();
        return view;
    }

    private void initView(View view) {
        lv_feed = (ListView) view.findViewById(R.id.lv_feed);
        hlv_topic = (HorizontalListView) view.findViewById(R.id.hlv_topic);
        tv_more = (TextView) view.findViewById(R.id.tv_more);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
    }

    private void setOnClickListener() {
        tv_more.setOnClickListener(onClickListener);
        iv_add.setOnClickListener(onClickListener);
    }


    @Override
    public void laodData() {
        getTopic();
    }

    @Override
    public void resetData() {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    List<Topic> list = (List<Topic>) msg.obj;
                    setTopic(list);
                    getFeed(list.get(0).id);
                    break;
                case 4:
                    List<FeedItem> feedItems = (List<FeedItem>) msg.obj;
                    setFeed(feedItems);
                    break;
                case 5:
                    feedAdapter.getItem(likeposition).isLiked = true;
                    feedAdapter.getItem(likeposition).likeCount = feedAdapter.getItem(likeposition).likeCount + 1;
                    feedAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    feedAdapter.getItem(likeposition).isLiked = false;
                    feedAdapter.getItem(likeposition).likeCount = feedAdapter.getItem(likeposition).likeCount - 1;
                    feedAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };


    private void getTopic() {
        GetUmengTopicList umengTopicList = new GetUmengTopicList(getActivity(), handler);
        umengTopicList.getALLTopic();
    }

    private void setTopic(List<Topic> topics) {
        topicAdapter = new TopicAdapter(getActivity());
        hlv_topic.setAdapter(topicAdapter);
        topicAdapter.setAdapterDatas(topics);
        topicAdapter.notifyDataSetChanged();
    }

    private void getFeed(String topicid) {
        GetUnmengFeedList unmengFeedList = new GetUnmengFeedList(getActivity(), topicid, handler);
        unmengFeedList.getfetchRealTimeFeed();
    }

    private void setFeed(List<FeedItem> feedItems) {
        feedAdapter = new FeedAdapter(getActivity(), likeListener);
        lv_feed.setAdapter(feedAdapter);
        feedAdapter.setAdapterDatas(feedItems);
        feedAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    break;
                case R.id.tv_more:
                    callbackByFragmentId(R.id.square_topic_more);
                    break;
                case R.id.iv_add:
                    callbackByFragmentId(R.id.square_add);
                    break;
            }
        }
    };


    private View.OnClickListener likeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int postition = (int) v.getTag();
            likeposition = postition;
            switch (v.getId()) {
                case R.id.ll_like:
                    GetUnmengFeed unmengFeed = new GetUnmengFeed(getActivity(), feedAdapter.getItem(postition).id, handler);
                    if (feedAdapter.getItem(postition).isLiked == true) {
                        unmengFeed.ulikeFeed();
                    } else {
                        unmengFeed.likeFeed();
                    }
                    break;
                case R.id.rl_comment:
                    FeedItem item = feedAdapter.getItem(postition);
                    callbackByFragmentId(R.id.square_topic_feed, item.id);
                    break;
            }

        }
    };


}
