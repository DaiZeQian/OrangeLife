package com.oneorange.content;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.adapter.ChatInformationAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.manager.Constant;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2016/5/30.
 * 聊天新消息列表
 */
public class ChatInfomationView extends BaseActivity {

    private SwipeRefreshLayout swiperefresh;
    private ListView lv_information;
    private ImageView iv_back;
    //op
    private ChatInformationAdapter chatInformationAdapter;
    //data
    private List<NewFriendsDao> friendsDaos;

    private final int ACCEPT_SUCCESS = 4;
    private final int ACCEPT_REFUSE = 6;
    private final int ACCEPT_FAILED = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_information);
        initView();
        setOnclickListen();
        setInformation();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ACCEPT_SUCCESS:
                    int position = (int) msg.obj;
                    ContentValues values = new ContentValues();
                    values.put("status", Constant.AGREED);
                    DataSupport.update(NewFriendsDao.class, values, chatInformationAdapter.getItem(position).getId());
                    chatInformationAdapter.getItem(position).setStatus(Constant.AGREED);
                    chatInformationAdapter.notifyDataSetChanged();
                    break;
                case ACCEPT_REFUSE:
                    int postion_refuse = (int) msg.obj;
                    ContentValues revalues = new ContentValues();
                    revalues.put("status", Constant.REFUSED);
                    DataSupport.update(NewFriendsDao.class, revalues, chatInformationAdapter.getItem(postion_refuse).getId());
                    chatInformationAdapter.getItem(postion_refuse).setStatus(Constant.REFUSED);
                    chatInformationAdapter.notifyDataSetChanged();
                    break;
                case ACCEPT_FAILED:
                    ToastUtil.show(ChatInfomationView.this, "请求失败,请稍候重试", 200);
                    break;
            }
        }
    };


    private void initView() {
        lv_information = (ListView) findViewById(R.id.lv_information);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        iv_back = (ImageView) findViewById(R.id.iv_back);

    }

    private void setOnclickListen() {
        swiperefresh.setOnRefreshListener(refreshListener);
        iv_back.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swiperefresh.setRefreshing(false);
        }
    };


    private void setInformation() {
        friendsDaos = DataSupport.findAll(NewFriendsDao.class);
        chatInformationAdapter = new ChatInformationAdapter(this, rtOnclickListener, clOnclickListener);
        lv_information.setAdapter(chatInformationAdapter);
        chatInformationAdapter.setAdapterDatas(friendsDaos);
        chatInformationAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener rtOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = (int) v.getTag(R.string.chat_agree);
            final NewFriendsDao newFriendsDao = chatInformationAdapter.getItem(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (newFriendsDao.getStatus().equals(Constant.BEINVITEED)) {
                            EMClient.getInstance().contactManager().acceptInvitation(newFriendsDao.getUsername());
                        }
                        Message msg = new Message();
                        msg.what = ACCEPT_SUCCESS;
                        msg.obj = position;
                        handler.sendMessage(msg);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(ACCEPT_FAILED);
                    }
                }
            }).start();
        }
    };


    private View.OnClickListener clOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = (int) v.getTag(R.string.chat_refuse);
            final NewFriendsDao newFriendsDao = chatInformationAdapter.getItem(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (newFriendsDao.getStatus().equals(Constant.BEINVITEED)) {
                            EMClient.getInstance().contactManager().declineInvitation(newFriendsDao.getUsername());
                        }
                        Message msg = new Message();
                        msg.what = ACCEPT_REFUSE;
                        msg.obj = position;
                        handler.sendMessage(msg);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(ACCEPT_FAILED);
                    }
                }
            }).start();

        }
    };


}
