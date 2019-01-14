package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oneorange.content.ChatUserInformationView;
import com.oneorange.entity.OtherUserCodeInfo;
import com.oneorange.manager.HandlerCode;
import com.oneorange.manager.UrlConfig;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
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
public class GetUserInfoController {


    private ChatUserInformationView informationView;
    private String getuserUrl;
    private String userId;

    private Context context;
    private Handler handler;

    public GetUserInfoController(String userId, Context context, Handler handler) {
        this.userId = userId;
        this.context = context;
        this.handler = handler;
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
        LogUtil.d("user_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.postString().url(url).headers(reqHeader).content(postUserId(userId).toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_userinfo", s);
                OtherUserCodeInfo codeInfo = ParseUtils.getObject(s, OtherUserCodeInfo.class);

                if (codeInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                    return;
                } else {
                    switch (codeInfo.getCode()) {
                        case 200:
                            Message msg = new Message();
                            msg.what = HandlerCode.GETUSER_SUCCESS;
                            msg.obj = codeInfo.getData();
                            handler.sendMessage(msg);
                            break;
                    }
                }

            }
        });


    }

}
