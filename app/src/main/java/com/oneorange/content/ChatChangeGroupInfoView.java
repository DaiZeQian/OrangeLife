package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ToastUtil;

/**
 * Created by admin on 2016/6/16.
 */
public class ChatChangeGroupInfoView extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSave;
    private EditText evContent;

    //data
    private String verifyCode;
    private String groupid;
    private String content;

    private final int SAVE_SUCCESS = 3;
    private final int SAVE_FAILED = 4;
    private final int GROUP_RESULT = 7;

    private void assignViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);
        evContent = (EditText) findViewById(R.id.ev_content);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_changegroupinfo);
        assignViews();
        getData();
        setOnClickListener();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVE_SUCCESS:
                    ToastUtil.show(ChatChangeGroupInfoView.this, R.string.chat_group_change_success, 100);
                    Intent intent = new Intent(ChatChangeGroupInfoView.this, ChatGroupDetailView.class);
                    intent.putExtra("content", content);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case SAVE_FAILED:
                    ToastUtil.show(ChatChangeGroupInfoView.this, R.string.chat_group_change_error, 100);
                    break;
            }
        }
    };


    private void getData() {
        verifyCode = getIntent().getExtras().getString("verifycode");
        if (verifyCode == null) {
            finish();
            return;
        }
        groupid = getIntent().getExtras().getString("groupid");
        content = getIntent().getExtras().getString("content");
        switch (verifyCode) {
            case "0":
                tvTitle.setText("修改群名称");
                break;
            case "1":
                tvTitle.setText("修改群简介");
                break;
        }
        evContent.setText(content);
    }

    private void setOnClickListener() {
        tvSave.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_save:
                    content = evContent.getText().toString();
                    if (content.length() <= 0) {
                        ToastUtil.show(ChatChangeGroupInfoView.this, R.string.input_null, 100);
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                switch (verifyCode) {
                                    case "0":
                                        LogUtil.d("groupid", groupid);
                                        EMClient.getInstance().groupManager().changeGroupName(groupid, content);
                                        break;
                                    case "1":
                                        break;
                                }
                                handler.sendEmptyMessage(SAVE_SUCCESS);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                LogUtil.d("error", e.toString());
                                handler.sendEmptyMessage(SAVE_FAILED);
                            }
                        }
                    }).start();
                    break;
            }
        }
    };
}
