package com.oneorange.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUnmengFeedList;
import com.oneorange.unmeng.UnmengAdapter.FeedAdapter;
import com.umeng.comm.core.beans.FeedItem;

import java.util.List;

/**
 * Created by admin on 2016/6/13.
 */

@SuppressLint("ValidFragment")
public class ThemeFragment extends BaseFragment {

    private SwipeRefreshLayout swiperefresh;
    private ListView lv_feed;


    private String uid;

    public ThemeFragment(String uid) {
        this.uid = uid;
    }


    private FeedAdapter feedAdapter;

    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, null);
        initView(view);
        return view;
    }

    @Override
    public void laodData() {
        setOnclickListener();
        GetUnmengFeedList unmengFeedList = new GetUnmengFeedList(getActivity(), handler, uid);
        unmengFeedList.getUserFeed();
    }

    @Override
    public void resetData() {

    }

    private void initView(View view) {
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        lv_feed = (ListView) view.findViewById(R.id.lv_feed);
        feedAdapter = new FeedAdapter(getActivity(), null);
    }

    private void setOnclickListener() {
        swiperefresh.setOnRefreshListener(onRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swiperefresh.setRefreshing(false);
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    List<FeedItem> feedItem = (List<FeedItem>) msg.obj;
                    feedAdapter.setAdapterDatas(feedItem);
                    lv_feed.setAdapter(feedAdapter);
                    feedAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

}
