package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.entity.PostInfo;
import com.oneorange.entity.TokenCodeInfo;
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
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/27.
 */
public class LoginController {

    private String username;
    private String password;
    private String code;

    private Handler handler;
    private Context context;

    private String type;


    private final int LOGINBYPSD_SUCCESS = 1;
    private final int LOGINBYCODE_SUCCESS = 2;

    private final int LOGINHX_SECCESS = 5;
    private final int LOGINHX_FAILED = 6;


    public LoginController(String username, String password, Context context, Handler handler) {
        this.username = username;
        this.password = password;
        this.context = context;
        this.handler = handler;
    }


    public LoginController(String username, String code, Handler handler, Context context) {
        this.username = username;
        this.code = code;
        this.handler = handler;
        this.context = context;
    }


    public LoginController(String username, Handler handler, Context context) {
        this.username = username;
        this.handler = handler;
        this.context = context;
    }

    public LoginController(Handler handler, String username, String password, Context context, String type) {
        this.handler = handler;
        this.username = username;
        this.password = password;
        this.context = context;
        this.type = type;
    }

    public void getCode() {
        String getcodeurl = UrlConfig.GETCODE_URL + "?telephone=" + username;
        OkHttpUtils.get().url(getcodeurl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response", s);
            }
        });
    }


    public void loginbycode() {
        String code_url = UrlConfig.REGISITER_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id", "f3d259ddd3ed8ff3843839b");
        params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        params.put("type", type);
        OkHttpUtils.post().url(code_url).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtil.d("error", e.toString());
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response", s);
                TokenCodeInfo tokenCodeInfo = ParseUtils.getObject(s, TokenCodeInfo.class);
                if (tokenCodeInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                    return;
                }
                switch (tokenCodeInfo.getCode()) {
                    case 200:
                        UserPrefenceManager.getInstance().setToken(tokenCodeInfo.getData().getAccess_token());
                        UserPrefenceManager.getInstance().setRefeshToken(tokenCodeInfo.getData().getRefresh_token());
                        handler.sendEmptyMessage(LOGINBYCODE_SUCCESS);
                        break;
                }


            }
        });
    }


    public void loginhx() {
        DataSupport.deleteAll(NewFriendsDao.class);//登录之前清空数据中的数据
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                handler.sendEmptyMessage(LOGINHX_SECCESS);
            }

            @Override
            public void onError(final int i, String s) {
                handler.sendEmptyMessage(LOGINHX_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private JSONObject register() {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("name", username);
            postObject.put("code", code);
            jsonObject.put("data", postObject);
            LogUtil.d("jsonobject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public void regisiter() {
        String register_url = UrlConfig.REGISITRER_URL;
        LogUtil.d("register_url", register_url);
        OkHttpUtils.postString().url(register_url).content(register().toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_register", s);
                PostInfo postInfo = ParseUtils.getObject(s, PostInfo.class);
                if (postInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                } else {
                    switch (postInfo.getCode()) {
                        case 200:
                            handler.sendEmptyMessage(HandlerCode.REGISTER_SUCCESS);
                            break;
                        case 400:
                            handler.sendEmptyMessage(HandlerCode.REGISTER_FAILED);
                            break;
                    }
                }

            }
        });
    }

}
