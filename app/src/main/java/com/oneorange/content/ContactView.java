package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.adapter.SortAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.GetFriendsController;
import com.oneorange.entity.GetFriendsInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.SortBarListView.CharacterParser;
import com.oneorange.view.SortBarListView.PinyinComparator;
import com.oneorange.view.SortBarListView.SideBar;
import com.oneorange.view.SortBarListView.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 2016/5/17.
 * <p/>
 * 通讯录 好友以及群组列表
 * 联系人
 */
public class ContactView extends BaseActivity {


    private ListView lv_contact;

    private LinearLayout ll_newfriend;
    private LinearLayout ll_group;

    private View viewHeader;//list的头布局

    private SideBar sidebar;
    private TextView dialog;
    private ImageView iv_back;
    private ImageView iv_add;

    //op
    private SortAdapter adapter;


    //data
    private List<String> friends;
    private Thread thread;
    private final int LIST_SUCCESS = 6;
    private final int LIST_FAILED = 7;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactview);
        initView();
        getContactList();
        setOnclickListener();


    }


    private void getFriendList(List<String> list) {
        GetFriendsController friendsController = new GetFriendsController(this, handler, list);
        friendsController.getFriends();
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
                    ToastUtil.show(ContactView.this, "联系人获取失败", 200);
                    break;
                case 14:
                    List<GetFriendsInfo> contacts = (List<GetFriendsInfo>) msg.obj;
                    setFriends(contacts);
                    break;
            }
        }
    };

    private void initView() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        viewHeader = getLayoutInflater().inflate(R.layout.contact_header, null);
        ll_newfriend = (LinearLayout) viewHeader.findViewById(R.id.ll_newfriend);
        ll_group = (LinearLayout) viewHeader.findViewById(R.id.ll_group);
        sidebar.setTextView(dialog);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        lv_contact.addHeaderView(viewHeader, null, false);
        pinyinComparator = new PinyinComparator();
    }

    private void setOnclickListener() {
        ll_newfriend.setOnClickListener(onClickListener);
        ll_group.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        iv_add.setOnClickListener(onClickListener);
        // 设置右侧触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_contact.setSelection(position - 1);//由于有头布局
                }

            }
        });
    }


    public void setFriends(List<GetFriendsInfo> friends) {
        SourceDateList = filledData(friends);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        lv_contact.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_contact.setOnItemClickListener(onItemClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_newfriend:
                    openActivity(ChatInfomationView.class);
                    break;
                case R.id.ll_group:
                    openActivity(GroupListView.class);
                    break;
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.iv_add:
                    openActivity(ChatSearchView.class);
                    break;
            }
        }
    };

    //获取联系人列表
    private void getContactList() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friends = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    handler.sendEmptyMessage(LIST_SUCCESS);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(LIST_FAILED);
                }
            }
        });
        thread.start();
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putString("userId", SourceDateList.get(position - 1).getChengid());
            bundle.putInt("chattype", EaseConstant.CHATTYPE_SINGLE);
            openActivity(ChatView.class, bundle);
        }
    };

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


}
