package com.oneorange.controller;

import com.oneorange.manager.UrlConfig;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by admin on 2016/6/28.
 */
public class UpdateImageController {//上传图片类

    private String path;


    public UpdateImageController(String path) {
        this.path = path;
    }


    public void updateImage() {
        String update_url = UrlConfig.UPDATEIMAGE_URL;
        LogUtil.d("update",update_url);
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("authorization", "Bearer" + " " + UserPrefenceManager.getInstance().getToken());
        OkHttpUtils.post().addFile("file", System.currentTimeMillis() + "jpg", file).headers(reqHeader).url(update_url).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int i) {
                LogUtil.d("error",e.toString());
            }

            @Override
            public void onResponse(String s, int i) {
                LogUtil.d("response_img", s);
            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }
        });
    }
}
