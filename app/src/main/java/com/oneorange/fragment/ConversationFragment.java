package com.oneorange.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.oneorange.content.ChatGroupDetailView;
import com.oneorange.content.ChatUserInformationView;
import com.oneorange.controller.GetChatUserInfoController;
import com.oneorange.db.UserFriendsDao;
import com.oneorange.entity.OtherUserCodeInfo;
import com.oneorange.manager.UserFriendsHelper;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;

/**
 * Created by admin on 2016/5/23.
 * <p/>
 * 聊天页面
 */
public class ConversationFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentListener {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:
                    String jsontostring = (String) msg.obj;
                    //  conversation.setExtField(jsontostring);
                    OtherUserCodeInfo codeInfo = ParseUtils.getObject(jsontostring, OtherUserCodeInfo.class);
                    if (codeInfo == null) {
                        ToastUtil.show(getActivity(), R.string.service_error, 100);
                        return;
                    } else {
                        switch (codeInfo.getCode()) {
                            case 200:
                                titleBar.setTitle(codeInfo.getData().getNickname());
                                conversation.setExtField(codeInfo.getData().getNickname());
                                UserFriendsDao friendsDao = new UserFriendsDao();
                                friendsDao.setUserid(toChatUsername);
                                friendsDao.setAvatar(codeInfo.getData().getAvatar());
                                friendsDao.setNickname(codeInfo.getData().getNickname());
                                UserFriendsHelper.getInstance().saveFriendInfo(friendsDao);
                                break;
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void setUpView() {
        super.setUpView();
        setChatFragmentListener(this);

        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_GROUP) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", toChatUsername);
                    Intent intent = new Intent(getActivity(), ChatGroupDetailView.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", toChatUsername);
                    Intent intent = new Intent(getActivity(), ChatUserInformationView.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            GetChatUserInfoController controller = new GetChatUserInfoController(handler, toChatUsername, getActivity());
            controller.getOthreUserInfo();
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        message.setAttribute("headUrl", UserPrefenceManager.getInstance().getCurrentAvatar());
        message.setAttribute("nickName", UserPrefenceManager.getInstance().getCurrentNickName());
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", username);
        Intent intent = new Intent(getActivity(), ChatUserInformationView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }


}
