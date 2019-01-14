package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;

import com.oneorange.entity.PostInfo;
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
 * Created by admin on 2016/6/29.
 */
public class UserPsdController {

    private String oldPsd;
    private String newPsd;
    private String rtPsd;

    private Handler handler;
    private Context context;


    public UserPsdController(String newPsd, Handler handler, Context context) {
        this.newPsd = newPsd;
        this.handler = handler;
        this.context = context;
    }


    public UserPsdController(String oldPsd, String newPsd, String rtPsd, Handler handler, Context context) {
        this.oldPsd = oldPsd;
        this.newPsd = newPsd;
        this.rtPsd = rtPsd;
        this.handler = handler;
        this.context = context;
    }

    private JSONObject psdJson() {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();

        try {
            postObject.put("password", newPsd);
            jsonObject.put("data", postObject);
            LogUtil.d("jsonobject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void changePsdBytoken() {
        String url = UrlConfig.CHANGEPSDBYTOKEN_ULR;
        LogUtil.d("changepsd_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        LogUtil.d("header", reqHeader.toString());
        OkHttpUtils.postString().url(url).headers(reqHeader).content(psdJson().toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_changepsd", s);
                PostInfo postInfo = ParseUtils.getObject(s, PostInfo.class);
                if (postInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                    return;
                }
                switch (postInfo.getCode()) {
                    case 200:
                        handler.sendEmptyMessage(HandlerCode.CHANGEPSD_TOKEN_SUCCESS);
                        break;
                    case 400:
                        ToastUtil.show(context, postInfo.getMsg(), 100);
                        break;
                }
            }
        });

    }

    private JSONObject oldPostJson() {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();

        try {
            postObject.put("password", oldPsd);
            postObject.put("newpwd", newPsd);
            postObject.put("commitpwd", rtPsd);
            jsonObject.put("data", postObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public void resetPsd() {
        String url = UrlConfig.CHANGEPSDMYOLD_URL;
        LogUtil.d("changepsd_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.postString().url(url).headers(reqHeader).content(oldPostJson().toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_changepsd", s);
                PostInfo postInfo = ParseUtils.getObject(s, PostInfo.class);
                if (postInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                    return;
                }
                switch (postInfo.getCode()) {
                    case 200:
                        handler.sendEmptyMessage(HandlerCode.CHANGEPSD_TOKEN_SUCCESS);
                        break;
                    case 400:
                        ToastUtil.show(context, postInfo.getMsg(), 100);
                        break;
                }
            }
        });
    }

}
