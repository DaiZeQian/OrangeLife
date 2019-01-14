package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.oneorange.base.BaseActivity;
import com.oneorange.fragment.ConversationFragment;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/5/17.
 */
public class ChatView extends BaseActivity {

    //data
    private String userId;
    private int chattype;
    private ConversationFragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatview);
        userId = getIntent().getExtras().getString("userId");
        chattype = getIntent().getExtras().getInt("chatType", EaseConstant.CHATTYPE_SINGLE);
        conversationFragment = new ConversationFragment();


        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chattype);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);
        conversationFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, conversationFragment).commit();
    }

    /**/
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (userId.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
}
