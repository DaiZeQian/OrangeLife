package com.oneorange.content;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.adapter.SortGroupAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.view.SortBarListView.CharacterParser;
import com.oneorange.view.SortBarListView.PinyinComparator;
import com.oneorange.view.SortBarListView.SideBar;
import com.oneorange.view.SortBarListView.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 2016/5/23.
 */
public class GroupListView extends BaseActivity {

    private ListView lv_groups;
    private SideBar sidebar;
    private TextView dialog;
    private ImageView iv_creat;

    private View viewHeader;
    private LinearLayout ll_creat_group;
    private ImageView iv_back;


    //op
    private SortGroupAdapter groupAdapter;
    //data
    private List<EMGroup> groups;
    private Thread thread;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> list;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initView();
        setOnClickListener();
        getGroups();
    }

    private void initView() {
        lv_groups = (ListView) findViewById(R.id.lv_groups);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        iv_creat = (ImageView) findViewById(R.id.iv_creat);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        viewHeader = getLayoutInflater().inflate(R.layout.group_header, null);
        ll_creat_group = (LinearLayout) viewHeader.findViewById(R.id.ll_creat_group);
        lv_groups.addHeaderView(viewHeader);
        sidebar.setTextView(dialog);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }


    private void setOnClickListener() {
        iv_creat.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        ll_creat_group.setOnClickListener(onClickListener);
        // 设置右侧触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = groupAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lv_groups.setSelection(position - 1);//由于有头布局
                }

            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_creat:
                    openActivity(ChatSearchView.class);
                    break;
                case R.id.ll_creat_group:
                    openActivity(GroupCreateView.class);
                    break;
                case R.id.iv_back:
                    finish();
                    break;
            }
        }
    };

    private void getGroups() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setGroups(groups);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 设置群组列表
     *
     * @param groups
     */
    private void setGroups(List<EMGroup> groups) {
        list = filledData(groups);
        // 根据a-z进行排序源数据
        Collections.sort(list, pinyinComparator);
        groupAdapter = new SortGroupAdapter(this, list);
        lv_groups.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
        lv_groups.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<EMGroup> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getGroupName());
            sortModel.setNum("(" + (date.get(i).getMembers().size() + 1) + "人)");
            sortModel.setGroupId(date.get(i).getGroupId());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getGroupName());
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
            Bundle bundle = new Bundle();
            bundle.putString("userId", list.get(position-1).getGroupId());
            bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
            openActivity(ChatView.class, bundle);
        }
    };

}
