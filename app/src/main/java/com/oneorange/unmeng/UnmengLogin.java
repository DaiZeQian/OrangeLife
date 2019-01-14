package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;

import com.oneorange.utils.LogUtil;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.login.LoginListener;

/**
 * Created by admin on 2016/6/20.
 */
public class UnmengLogin {


    private String username;
    private String userid;
    private Handler handler;
    private Context context;
    private final int LOGIN_SUCCESS = 7;
    private final int LOGIN_FAILED = 8;
    private String iconUrl;


    public UnmengLogin(String username, String userid, Handler handler, Context context) {
        this.username = username;
        this.userid = userid;
        this.handler = handler;
        this.context = context;
    }

    public UnmengLogin(String username, String userid, Handler handler, Context context, String iconUrl) {
        this.username = username;
        this.userid = userid;
        this.handler = handler;
        this.context = context;
        this.iconUrl = iconUrl;
    }

    public void login() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context.getApplicationContext());
        CommUser user = new CommUser();
        user.name = username;
        user.id = userid;
        user.iconUrl = iconUrl;
        sdk.loginToUmengServerBySelfAccount(context, user, new LoginListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int i, CommUser commUser) {

                LogUtil.d("unmeng_login", i + "");
                if (ErrorCode.NO_ERROR == i) {
                    //在此处可以跳转到任何一个你想要的activity
                    handler.sendEmptyMessage(LOGIN_SUCCESS);
                } else {
                    handler.sendEmptyMessage(LOGIN_FAILED);
                }

            }
        });
    }
}
