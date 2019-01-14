package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.ImageItem;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.ImageResponse;

import java.util.List;

/**
 * Created by admin on 2016/6/23.
 */
public class UnmengUpimage {


    private String path;
    private Handler handler;
    private List<String> paths;
    private Context context;
    private final int UPSUCCESS = 7;
    private int position;


    public UnmengUpimage(String path, Handler handler, Context context) {
        this.path = path;
        this.handler = handler;
        this.context = context;
    }




    public int getPosition() {
        return position;
    }

    public void update() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.uploadImage("file://" + path, new Listeners.SimpleFetchListener<ImageResponse>() {
            @Override
            public void onComplete(ImageResponse imageResponse) {
                if (imageResponse.errCode == ErrorCode.NO_ERROR) {
                    ImageItem imageItem = imageResponse.result;
                    Message msg = new Message();
                    msg.what = UPSUCCESS;
                    msg.obj = imageItem;
                    handler.sendMessage(msg);
                } else {

                }
            }
        });
    }


}
