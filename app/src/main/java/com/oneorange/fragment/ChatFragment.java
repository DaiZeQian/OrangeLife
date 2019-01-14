package com.oneorange.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.oneorange.adapter.CvSationAdapter;
import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/17.
 * <p>
 * 消息列表
 */
public class ChatFragment extends BaseFragment {

    private ListView lv_chat;
    private ImageView iv_contact;
    private ImageView iv_search;

    private final int MSG_REFRESH = 2;


    //op
    private CvSationAdapter cvSationAdapter;


    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        initView(view);
        return view;
    }



    @Override
    public void laodData() {
        setOnClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void resetData() {
        setConverSation(loadConversationList());
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH:
                    cvSationAdapter.setAdapterDatas(loadConversationList());
                    cvSationAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    private void initView(View view) {
        iv_contact = (ImageView) view.findViewById(R.id.iv_contact);
        lv_chat = (ListView) view.findViewById(R.id.lv_chat);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);

        cvSationAdapter = new CvSationAdapter(getActivity());
        lv_chat.setAdapter(cvSationAdapter);
    }


    private void setOnClickListener() {
        iv_contact.setOnClickListener(onClickListener);
        iv_search.setOnClickListener(onClickListener);
        lv_chat.setOnItemClickListener(onItemClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_contact:
                    callbackByFragmentId(R.id.chat_ct, null);
                    break;
                case R.id.iv_search:
                    callbackByFragmentId(R.id.chat_search, null);
                    break;
            }
        }
    };

    private void setConverSation(List<EMConversation> emConversations) {
        cvSationAdapter.setAdapterDatas(emConversations);
        cvSationAdapter.notifyDataSetChanged();

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EMConversation conversation = (EMConversation) parent.getAdapter().getItem(position);
            callbackByFragmentId(R.id.chat_single, conversation);
        }
    };


    /**
     * 获取会话列表
     *
     * @param /context
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        LogUtil.d("listsize", list.size() + "");

        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param /usernames
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    //刷新
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

}
