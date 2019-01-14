package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneorange.adapter.ChatSearchAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.SearchFriendsController;
import com.oneorange.entity.ChatSearchInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by admin on 2016/6/1.
 */
public class ChatSearchView extends BaseActivity {

    private EditText ev_find;
    private TextView tv_add;
    private ImageView iv_back;
    private ListView lv_search;
    private ImageView iv_search;
    //data
    private String userName;
    private final int ADD_SUCCESS = 2;
    private final int ADD_FALIED = 3;

    //op
    private ChatSearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search);
        initView();
        setOnClickListener();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 18:
                    List<ChatSearchInfo> searchInfos = (List<ChatSearchInfo>) msg.obj;
                    setSearchs(searchInfos);
                    break;
            }
        }
    };

    private void initView() {
        ev_find = (EditText) findViewById(R.id.ev_find);
        tv_add = (TextView) findViewById(R.id.tv_add);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_search = (ListView) findViewById(R.id.lv_search);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        searchAdapter = new ChatSearchAdapter(this);
    }

    private void setOnClickListener() {
        tv_add.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        lv_search.setOnItemClickListener(onItemClickListener);
    }

    public void setSearchs(List<ChatSearchInfo> list) {
        lv_search.setAdapter(searchAdapter);
        searchAdapter.setAdapterDatas(list);
        searchAdapter.notifyDataSetChanged();
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_add:
                    userName = ev_find.getText().toString();
                    if (userName.length() <= 0) {
                        ToastUtil.show(ChatSearchView.this, R.string.input_null, 100);
                    } else {
                        searchUser(userName);
                        iv_search.setVisibility(View.GONE);
                    }
                    break;
                case R.id.iv_back:
                    finish();
                    break;
            }
        }
    };

    private void searchUser(String keyword) {
        try {       //将中文字符转义
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchFriendsController searchConeroller = new SearchFriendsController(handler, this, keyword);
        searchConeroller.searchFriend();
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String type = searchAdapter.getItem(position).getType();
            Bundle bundle = new Bundle();
            bundle.putString("userId", searchAdapter.getItem(position).getCheng_id());
            switch (type) {
                case "0":
                    openActivityAndCloseThis(ChatUserInformationView.class, bundle);
                    break;
                case "1":
                    openActivityAndCloseThis(ChatGroupDetailView.class, bundle);
                    break;
            }
        }
    };
}
