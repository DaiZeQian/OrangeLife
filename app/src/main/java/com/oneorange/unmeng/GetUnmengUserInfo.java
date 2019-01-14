package com.oneorange.unmeng;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.AlbumItem;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.AlbumResponse;
import com.umeng.comm.core.nets.responses.ProfileResponse;

import java.util.List;

/**
 * Created by admin on 2016/6/24.
 */
public class GetUnmengUserInfo {

    private String sourceUid;
    private String source;
    private Context context;
    private Handler handler;
    private String uid;


    private final int USERINFO_SUCCESS = 9;
    private final int PHOTO_SEUCCESS = 10;

    public GetUnmengUserInfo(String sourceUid, String source, Context context, Handler handler) {
        this.sourceUid = sourceUid;
        this.source = source;
        this.context = context;
        this.handler = handler;
    }

    public GetUnmengUserInfo(Handler handler, String uid, Context context) {
        this.handler = handler;
        this.uid = uid;
        this.context = context;
    }

    public void getUmUserInfo() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchUserProfile(sourceUid, source, new Listeners.FetchListener<ProfileResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(ProfileResponse profileResponse) {
                if (profileResponse.errCode == ErrorCode.NO_ERROR) {
                    CommUser commUser = profileResponse.result;
                    Message msg = new Message();
                    msg.what = USERINFO_SUCCESS;
                    msg.obj = commUser.id;
                    handler.sendMessage(msg);

                }

            }
        });
    }


    public void getUserPhoto() {
        CommunitySDK sdk = CommunityFactory.getCommSDK(context);
        sdk.fetchAlbums(uid, new Listeners.FetchListener<AlbumResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(AlbumResponse albumResponse) {
                List<AlbumItem> albumItems = albumResponse.result;
                Message msg = new Message();
                msg.what = PHOTO_SEUCCESS;
                msg.obj = albumItems;
                handler.sendMessage(msg);
            }
        });
    }
}
