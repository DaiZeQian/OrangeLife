package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oneorange.manager.UrlConfig;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/16.
 * 获取用户（所有用户）信息不同于过去个人用户信息控制类
 */
public class GetChatUserInfoController {


    private Handler handler;
    private String getuserUrl;
    private String userId;
    private Context context;
    private final int CHAT_USER_SUCCESS = 5;

    public GetChatUserInfoController(Handler handler, String userId, Context context) {
        this.handler = handler;
        this.userId = userId;
        this.context = context;
    }

    private JSONObject postUserId(String userId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("cheng_id", userId);
            jsonObject.put("data", postObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void getOthreUserInfo() {
        String url = UrlConfig.GETFRIENDINFO_URL;
        LogUtil.d("friend_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.postString().url(url).headers(reqHeader).content(postUserId(userId).toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                Message msg = new Message();
                msg.what = CHAT_USER_SUCCESS;
                msg.obj = s;
                handler.sendMessage(msg);
            }
        });


    }



}
