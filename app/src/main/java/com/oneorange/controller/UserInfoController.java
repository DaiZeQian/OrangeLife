package com.oneorange.controller;

import android.os.Handler;
import android.os.Message;

import com.oneorange.entity.UserCodeInfo;
import com.oneorange.entity.UserInfo;
import com.oneorange.manager.UrlConfig;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/15.
 */
public class UserInfoController {

    private Handler handler;
    private UserInfo userInfo;

    private final int USERINFO_SUCCESS = 4;


    public UserInfoController(Handler handler, UserInfo userInfo) {
        this.handler = handler;
        this.userInfo = userInfo;
    }

    public UserInfoController(Handler handler) {
        this.handler = handler;
    }

    public void getUserInfo() {
        String getuser_url = UrlConfig.GETUSERINFO_URL;
        LogUtil.d("getuser_url", getuser_url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.get().url(getuser_url).headers(reqHeader).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_user", s);
                UserCodeInfo codeInfo = ParseUtils.getObject(s, UserCodeInfo.class);
                if (codeInfo != null) {
                    switch (codeInfo.getCode()) {
                        case 200:
                            UserInfo userInfo = codeInfo.getData();
                            UserPrefenceManager.getInstance().setHxid(userInfo.getCheng_id());
                            UserPrefenceManager.getInstance().setCurrentAvatar(userInfo.getUrl());
                            UserPrefenceManager.getInstance().setCurrentNickName(userInfo.getNickname());
                            UserPrefenceManager.getInstance().setUserGender(userInfo.getGender());
                            UserPrefenceManager.getInstance().setExtrapsd(userInfo.getExtra_password());
                            UserPrefenceManager.getInstance().setCurrentNickName(userInfo.getNickname());
                            Message msg = new Message();
                            msg.what = USERINFO_SUCCESS;
                            msg.obj = userInfo;
                            handler.sendMessage(msg);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 提交数据
     *
     * @return
     */
    private JSONObject userPost() {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("nickname", userInfo.getNickname() == null ? "" : userInfo.getNickname());
            postObject.put("signature", userInfo.getSignature() == null ? "" : userInfo.getSignature());
            postObject.put("gender", userInfo.getGender() == null ? "" : userInfo.getGender());
            postObject.put("avatar", userInfo.getUrl() == null ? "" : userInfo.getUrl());
            jsonObject.put("data", postObject);
            LogUtil.d("jsonobject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }


    public void setUserInfo() {
        String updateuser_url = UrlConfig.UPDATEUSERINFO_URL;
        LogUtil.d("updateuser_url", updateuser_url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        LogUtil.d("header", reqHeader.toString());
        OkHttpUtils.postString().url(updateuser_url).headers(reqHeader).content(userPost().toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_setuser", s);
            }
        });
    }
}
