package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.adapter.SortChooseAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.ChooseFriendController;
import com.oneorange.entity.GetFriendsInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.NetWorkUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.SortBarListView.CharacterParser;
import com.oneorange.view.SortBarListView.PinyinComparator;
import com.oneorange.view.SortBarListView.SideBar;
import com.oneorange.view.SortBarListView.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 2016/5/30.
 */
public class GroupCreateListView extends BaseActivity {

    private ListView lv_friends;
    private TextView tv_save;
    private TextView dialog;
    private SideBar sidebar;
    private SwipeRefreshLayout swiperefresh;


    //op
    private SortChooseAdapter chooseAdapter;
    //data
    private List<String> friends;
    private final int LIST_SUCCESS = 6;
    private final int LIST_FAILED = 7;
    private String verfiycode;//1  创建群组列表跳转    2群详情邀请好友  3群详情 删除好友
    private boolean isOwner;
    private String groupid;

    private final int INVITE_SUCCESS = 8;
    private final int INVITE_FAILED = 9;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<GetFriendsInfo> grouplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creat_list);
        initView();
        setOnClickListener();
        getData();
    }


    private void getData() {
        verfiycode = getIntent().getStringExtra("verifycode");
        if (verfiycode == null) {
            finish();
            return;
        }
        switch (verfiycode) {
            case "1":
                getFriends();
                break;
            case "2":
                grouplist = (List<GetFriendsInfo>) getIntent().getExtras().getSerializable("grouplist");
                isOwner = getIntent().getExtras().getBoolean("isowner", true);
                groupid = getIntent().getExtras().getString("groupid");
                LogUtil.d("grouplist------------------", grouplist.size() + "");
                for (int i = 0; i < grouplist.size(); i++) {
                    if (grouplist.get(i).getCheng_id().equals("0")) {
                        grouplist.remove(i);
                        i--;
                    }
                }
                setFriends(grouplist);
                break;
            case "3":
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LIST_SUCCESS:
                    getFriendList(friends);
                    break;
                case LIST_FAILED:
                    ToastUtil.show(GroupCreateListView.this, "好友列表获取失败,请刷新重试", 200);
                    break;
                case INVITE_SUCCESS:
                    ToastUtil.show(GroupCreateListView.this, R.string.chat_group_invite_success, 200);
                    finish();
                    break;
                case INVITE_FAILED:
                    ToastUtil.show(GroupCreateListView.this, R.string.chat_group_invite_failed, 200);
                    break;
                case 14:
                    List<GetFriendsInfo> list = (List<GetFriendsInfo>) msg.obj;
                    setFriends(list);
                    break;
            }
        }
    };


    private void initView() {
        lv_friends = (ListView) findViewById(R.id.lv_friends);
        tv_save = (TextView) findViewById(R.id.tv_save);
        dialog = (TextView) findViewById(R.id.dialog);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sidebar.setTextView(dialog);
    }

    private void setOnClickListener() {
        tv_save.setOnClickListener(onClickListener);
        swiperefresh.setOnRefreshListener(refreshListener);
    }

    private void getFriendList(List<String> list) {
        ChooseFriendController friendsController = new ChooseFriendController(list, this, handler);
        friendsController.getFriends();
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save:
                    switch (verfiycode) {
                        case "1":
                            setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
                            finish();
                            break;
                        case "2":
                            addMembersToGroup(getToBeAddMembers().toArray(new String[0]));
                            break;
                    }
                    break;
            }
        }
    };


    /**
     * 增加群成员
     *
     * @param newmembers
     */
    private void addMembersToGroup(final String[] newmembers) {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    // 创建者调用add方法
                    if (isOwner) {
                        EMClient.getInstance().groupManager().addUsersToGroup(groupid, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager().inviteUser(groupid, newmembers, null);
                    }
                    handler.sendEmptyMessage(INVITE_SUCCESS);
                } catch (final Exception e) {
                    handler.sendEmptyMessage(INVITE_SUCCESS);
                }
            }
        });
        NetWorkUtils.getDatafromNet(this, thread);
    }

    /**
     * 获取选中朋友列表
     *
     * @return
     */
    private List<String> getToBeAddMembers() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < SourceDateList.size(); i++) {
            if (SourceDateList.get(i).isChoose()) {
                list.add(SourceDateList.get(i).getName());
            }
        }
        return list;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swiperefresh.setRefreshing(false);
        }
    };


    private void getFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //sdk获取好友列表
                    friends = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    handler.sendEmptyMessage(LIST_SUCCESS);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(LIST_FAILED);
                }
            }
        }).start();
    }

    public void setFriends(List<GetFriendsInfo> list) {
        SourceDateList = filledData(list);
        Collections.sort(SourceDateList, pinyinComparator);
        chooseAdapter = new SortChooseAdapter(this, SourceDateList);
        lv_friends.setAdapter(chooseAdapter);
        chooseAdapter.notifyDataSetChanged();
        lv_friends.setOnItemClickListener(onItemClickListener);
    }


    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<GetFriendsInfo> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getNickname());
            sortModel.setImage(date.get(i).getImage());
            sortModel.setChengid(date.get(i).getCheng_id());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getNickname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (SourceDateList.get(position).isChoose()) {
                SourceDateList.get(position).setIsChoose(false);
            } else {
                SourceDateList.get(position).setIsChoose(true);
            }
            chooseAdapter.updateListView(SourceDateList);
        }
    };

}
