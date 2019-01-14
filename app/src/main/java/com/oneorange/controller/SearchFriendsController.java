package com.oneorange.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oneorange.content.ChatSearchView;
import com.oneorange.entity.ChatSearchCodeInfo;
import com.oneorange.entity.ChatSearchInfo;
import com.oneorange.manager.HandlerCode;
import com.oneorange.manager.UrlConfig;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/15.
 */
public class SearchFriendsController {


    private String searchFriendUrl;
    private ChatSearchView chatSearchView;
    private Context context;
    private Handler handler;
    private String keyword;


    public SearchFriendsController(String searchFriendUrl, ChatSearchView chatSearchView) {
        this.searchFriendUrl = searchFriendUrl;
        this.chatSearchView = chatSearchView;
    }

    public SearchFriendsController(Handler handler, Context context, String keyword) {
        this.handler = handler;
        this.context = context;
        this.keyword = keyword;
    }

    public void searchFriend() {
        String url = UrlConfig.SEARCHUSER_URL + "?keyword=" + keyword;
        LogUtil.d("search_url", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.get().url(url).headers(reqHeader).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                ChatSearchCodeInfo codeInfo = ParseUtils.getObject(s, ChatSearchCodeInfo.class);
                if (codeInfo == null) {
                    ToastUtil.show(context, R.string.service_error, 100);
                } else {
                    List<ChatSearchInfo> list = codeInfo.getData();
                    Message msg = new Message();
                    msg.what = HandlerCode.SEARCH_SUCCESS;
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            }
        });




    }

}
