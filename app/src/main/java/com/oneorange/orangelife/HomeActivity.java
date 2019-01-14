package com.oneorange.orangelife;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.oneorange.base.BaseActivity;
import com.oneorange.base.BaseFragment;
import com.oneorange.content.AllTopicView;
import com.oneorange.content.ChatSearchView;
import com.oneorange.content.ChatView;
import com.oneorange.content.ContactView;
import com.oneorange.content.CreatFeedView;
import com.oneorange.content.FeedDetailView;
import com.oneorange.content.HomeManagerView;
import com.oneorange.content.LoginView;
import com.oneorange.content.SettingView;
import com.oneorange.content.UserInformationView;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.fragment.ChatFragment;
import com.oneorange.fragment.HomeFragment;
import com.oneorange.fragment.MyFragment;
import com.oneorange.fragment.SquareFragment;
import com.oneorange.manager.Constant;
import com.oneorange.manager.OrangeLifeHelper;
import com.oneorange.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.List;


public class HomeActivity extends BaseActivity implements View.OnClickListener, BaseFragment.FragmentItemOnclickListener {

    private FrameLayout framelayout;


    private LinearLayout llHome;
    private RelativeLayout rlChat;
    private TextView tvChatNum;//未读消息数量
    private LinearLayout llMy;
    private LinearLayout llSq;
    private ImageView ivCom;
    private TextView tvCom;
    private ImageView ivSq;
    private TextView tvSq;
    private ImageView ivMy;
    private TextView tvMy;
    private ImageView ivHome;
    private TextView tvHome;


    //op
    private HomeFragment homeFragment;
    private ChatFragment chatFragment;
    private SquareFragment sqFragment;
    private MyFragment myFragment;


    //data
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;


    private Dialog removeDialog;//移除对话框
    private Dialog conflictDialog;//下线对话框
    private boolean iscomflict;
    private boolean isremove;
    private int indexTable = 0;


    public static HomeActivity instance;

    private void assignViews() {
        instance = this;
        llHome = (LinearLayout) findViewById(R.id.ll_home);
        ivHome = (ImageView) findViewById(R.id.iv_home);
        tvHome = (TextView) findViewById(R.id.tv_home);
        rlChat = (RelativeLayout) findViewById(R.id.rl_chat);
        ivCom = (ImageView) findViewById(R.id.iv_com);
        tvCom = (TextView) findViewById(R.id.tv_com);
        tvChatNum = (TextView) findViewById(R.id.tv_chat_num);
        llSq = (LinearLayout) findViewById(R.id.ll_sq);
        ivSq = (ImageView) findViewById(R.id.iv_sq);
        tvSq = (TextView) findViewById(R.id.tv_sq);
        llMy = (LinearLayout) findViewById(R.id.ll_my);
        ivMy = (ImageView) findViewById(R.id.iv_my);
        tvMy = (TextView) findViewById(R.id.tv_my);
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        sqFragment = new SquareFragment();
        myFragment = new MyFragment();
        updateUnreadLabel();
        smartFragmentReplace(R.id.framelayout, homeFragment);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            OrangeLifeHelper.getInstance().logout(false, null);
            openActivityAndCloseThis(LoginView.class);
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            openActivityAndCloseThis(LoginView.class);
            return;
        }


        setContentView(R.layout.activity_home);
        assignViews();


        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !iscomflict) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isremove) {
            showRemoveDialog();
        }

        setonClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!iscomflict && !isremove) {
            updateUnreadLabel();
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false && !iscomflict)) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isremove) {
            showRemoveDialog();
        }
    }

    private void setonClickListener() {
        llHome.setOnClickListener(this);
        rlChat.setOnClickListener(this);
        llSq.setOnClickListener(this);
        llMy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                indexTable = 0;
                setTextColor(tvHome);
                ivHome.setImageResource(R.mipmap.icon_home_on);
                smartFragmentReplace(R.id.framelayout, homeFragment);
                break;
            case R.id.rl_chat:
                indexTable = 1;
                setTextColor(tvCom);
                ivCom.setImageResource(R.mipmap.icon_community_on);
                smartFragmentReplace(R.id.framelayout, chatFragment);
                break;
            case R.id.ll_sq:
                indexTable = 2;
                setTextColor(tvSq);
                ivSq.setImageResource(R.mipmap.icon_square_on);
                smartFragmentReplace(R.id.framelayout, sqFragment);
                break;
            case R.id.ll_my:
                indexTable = 3;
                setTextColor(tvMy);
                ivMy.setImageResource(R.mipmap.icon_my_on);
                smartFragmentReplace(R.id.framelayout, myFragment);
                break;
        }
    }

    private void setTextColor(TextView tv) {
        tvHome.setTextColor(getResources().getColor(R.color.white));
        tvCom.setTextColor(getResources().getColor(R.color.white));
        tvSq.setTextColor(getResources().getColor(R.color.white));
        tvMy.setTextColor(getResources().getColor(R.color.white));
        tv.setTextColor(getResources().getColor(R.color.theme));
        ivHome.setImageResource(R.mipmap.icon_home_off);
        ivCom.setImageResource(R.mipmap.icon_community_off);
        ivSq.setImageResource(R.mipmap.icon_square_off);
        ivMy.setImageResource(R.mipmap.icon_my_off);
    }


    private EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {
                OrangeLifeHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refrushUI();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void refrushUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUnreadLabel();
                if (indexTable == 1) {
                    if (chatFragment != null) {
                        chatFragment.refresh();
                    }
                }
            }
        });
    }


    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {

        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            tvChatNum.setText(String.valueOf(count));
            tvChatNum.setVisibility(View.VISIBLE);
        } else {
            tvChatNum.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)//抛出聊天室的消息
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }


    //用户被移除提示
    private void showRemoveDialog() {
        isremove = true;
        OrangeLifeHelper.getInstance().logout(false, null);
        removeDialog = removeDialog(this);
        removeDialog.show();
    }

    public Dialog removeDialog(Context context) {
        Dialog rmoveDialog = new Dialog(context, R.style.loading_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_remove, null);
        LinearLayout ll_dialog = (LinearLayout) view.findViewById(R.id.ll_dialog);
        Button btn_right = (Button) view.findViewById(R.id.btn_right);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDialog.dismiss();
                removeDialog = null;
                DataSupport.deleteAll(NewFriendsDao.class);//清空数据中的数据
                openActivityAndCloseThis(LoginView.class);
            }
        });
        removeDialog.setCancelable(false);// 不可以用“返回键”取消
        removeDialog.setContentView(ll_dialog, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return rmoveDialog;
    }

    //用户异地登录提示
    private void showConflictDialog() {
        iscomflict = true;
        OrangeLifeHelper.getInstance().logout(false, null);
        conflictDialog = new Dialog(this, R.style.loading_dialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_conflict, null);
        LinearLayout ll_dialog = (LinearLayout) view.findViewById(R.id.ll_dialog);
        Button btn_right = (Button) view.findViewById(R.id.btn_right);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conflictDialog.dismiss();
                conflictDialog = null;
                DataSupport.deleteAll(NewFriendsDao.class);//清空数据中的数据
                Intent intent = new Intent(HomeActivity.this, LoginView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        conflictDialog.setCancelable(false);// 不可以用“返回键”取消
        conflictDialog.setContentView(ll_dialog, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        conflictDialog.show();
    }

    //返回后移到后台处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentItemClick(String tag, int eventTag, Object data) {
        if (tag.equals(ChatFragment.class.getSimpleName())) {
            switch (eventTag) {
                case R.id.chat_single:
                    if (data != null) {
                        EMConversation conversation = (EMConversation) data;
                        Bundle chatBundle = new Bundle();
                        if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                            chatBundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                            chatBundle.putString("userId", conversation.getUserName());
                            openActivity(ChatView.class, chatBundle);
                        } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                            chatBundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                            chatBundle.putString("userId", conversation.getUserName());
                            openActivity(ChatView.class, chatBundle);
                        } else {
                            ToastUtil.show(HomeActivity.this, "不知道这是啥", 200);
                        }
                    }
                    break;
                case R.id.chat_ct:
                    isNetWorkOpenActivity(ContactView.class);
                    break;
                case R.id.chat_search:
                    openActivity(ChatSearchView.class);
                    break;
            }
        } else if (tag.equals(MyFragment.class.getSimpleName())) {
            switch (eventTag) {
                case R.id.my_settting:
                    openActivity(SettingView.class);
                    break;
                case R.id.my_user_infor:
                    openActivity(UserInformationView.class);
                    break;
                case R.id.my_home_manager:
                    openActivity(HomeManagerView.class);
                    break;

            }
        } else if (tag.equals(SquareFragment.class.getSimpleName())) {
            switch (eventTag) {
                case R.id.square_topic_more:
                    openActivity(AllTopicView.class);
                    break;
                case R.id.square_topic_feed:
                    String feedid = (String) data;
                    Bundle feedidBundle = new Bundle();
                    feedidBundle.putString("feedid", feedid);
                    openActivity(FeedDetailView.class, feedidBundle);
                    break;
                case R.id.square_add:
                    openActivity(CreatFeedView.class);
                    break;
            }
        }
    }

}
