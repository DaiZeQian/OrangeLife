package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oneorange.content.GroupCreateListView;
import com.oneorange.entity.GetFriendsCodeInfo;
import com.oneorange.manager.HandlerCode;
import com.oneorange.manager.UrlConfig;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/15.
 */
public class ChooseFriendController {

    private GroupCreateListView createListView;
    private String getFriendUrl;
    private List<String> friends;

    private Context context;
    private Handler handler;


    public ChooseFriendController(List<String> friends, Context context, Handler handler) {
        this.friends = friends;
        this.context = context;
        this.handler = handler;
    }


    public JSONObject postFriends(List<String> list) {
        JSONObject jsonObject = new JSONObject();
        JSONArray postJson = new JSONArray();
        JSONObject postObject;
        try {
            for (int i = 0; i < list.size(); i++) {
                postObject = new JSONObject();
                postObject.put("name", list.get(i));
                postJson.put(postObject);
            }
            jsonObject.put("data", postJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void getFriends() {
        String url = UrlConfig.GETCONTACT_URL;
        LogUtil.d("friends_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.postString().url(url).headers(reqHeader).content(postFriends(friends).toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_friends", s);
                GetFriendsCodeInfo codeInfo = ParseUtils.getObject(s, GetFriendsCodeInfo.class);
                if (codeInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                } else {
                    switch (codeInfo.getCode()) {
                        case 200:
                            Message msg = new Message();
                            msg.what = HandlerCode.GETUSERS_URL_SUCCESS;
                            msg.obj = codeInfo.getData();
                            handler.sendMessage(msg);
                            break;

                    }
                }

            }
        });


    }




}
