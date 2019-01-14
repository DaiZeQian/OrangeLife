package com.oneorange.content;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneorange.adapter.BasicAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.UserInfoController;
import com.oneorange.entity.UserChoose;
import com.oneorange.entity.UserInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/29.
 * 更改用户基本信息
 * 目前有
 * 1.昵称
 * 2.性别
 */

public class UserBasicInforView extends BaseActivity {

    private LinearLayout llNick;
    private EditText evNick;
    private TextView tvDelete;
    private NoScrollGridView gvGender;
    private TextView tv_save;

    //op
    private BasicAdapter basicAdapter;

    //data
    private UserInfo userInfo;
    private UserChoose userChoose;
    private List<UserChoose> userChooses = new ArrayList<>();
    private String nickName;
    private String gender;

    private void assignViews() {
        llNick = (LinearLayout) findViewById(R.id.ll_nick);
        evNick = (EditText) findViewById(R.id.ev_nick);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        gvGender = (NoScrollGridView) findViewById(R.id.gv_gender);
        tv_save = (TextView) findViewById(R.id.tv_save);
        evNick.addTextChangedListener(new MyTextWatcherUtil(10, evNick));
        basicAdapter = new BasicAdapter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbasicinfo);
        assignViews();
        setOnClickListener();
        initData();
    }

    private void initData() {
        userChoose = new UserChoose("我是帅哥", R.drawable.default_boy, true);
        userChooses.add(userChoose);
        userChoose = new UserChoose("我是美女", R.drawable.default_girl, false);
        userChooses.add(userChoose);
        userChoose = new UserChoose("我不告诉你", R.drawable.default_secrecy, false);
        userChooses.add(userChoose);

        basicAdapter.setAdapterDatas(userChooses);
        gvGender.setAdapter(basicAdapter);
        basicAdapter.notifyDataSetChanged();
        gvGender.setOnItemClickListener(onItemClickListener);
    }


    private void setOnClickListener() {
        tv_save.setOnClickListener(onClickListener);
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < basicAdapter.getCount(); i++) {
                if (i == position) {
                    basicAdapter.getItem(i).setIsChoose(true);
                } else {
                    basicAdapter.getItem(i).setIsChoose(false);
                }
            }
            basicAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save:
                    nickName = evNick.getText().toString();
                    if (nickName.length() <= 0) {
                        ToastUtil.show(UserBasicInforView.this, R.string.input_null, 100);
                    } else {
                        for (int i = 0; i < basicAdapter.getCount(); i++) {
                            if (basicAdapter.getItem(i).isChoose()) {
                                switch (i) {
                                    case 0:
                                        gender = "男";
                                        break;
                                    case 1:
                                        gender = "女";
                                        break;
                                    case 2:
                                        gender = "保密";
                                        break;
                                }
                            }
                        }
                        userInfo = new UserInfo();
                        userInfo.setNickname(nickName);
                        userInfo.setGender(gender);
                        UserInfoController controller = new UserInfoController(null, userInfo);
                        controller.setUserInfo();
                    }
                    break;
            }
        }
    };

}
