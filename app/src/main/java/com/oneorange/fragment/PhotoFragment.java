package com.oneorange.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.oneorange.adapter.UserPhotoAdapter;
import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUnmengUserInfo;
import com.umeng.comm.core.beans.AlbumItem;

import java.util.List;

/**
 * Created by admin on 2016/6/13.
 */
@SuppressLint("ValidFragment")
public class PhotoFragment extends BaseFragment {


    private String uid;

    public PhotoFragment(String uid) {
        this.uid = uid;
    }


    private UserPhotoAdapter photoAdapter;
    private GridView gv_icon;


    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, null);
        init(view);
        return view;
    }

    @Override
    public void laodData() {
        GetUnmengUserInfo unmengUserInfo = new GetUnmengUserInfo(handler, uid, getActivity());
        unmengUserInfo.getUserPhoto();

    }

    @Override
    public void resetData() {

    }


    private void init(View view) {
        gv_icon = (GridView) view.findViewById(R.id.gv_icon);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    List<AlbumItem> albumItems = (List<AlbumItem>) msg.obj;
                    photoAdapter = new UserPhotoAdapter(getActivity(), getOptions());
                    photoAdapter.setAdapterDatas(albumItems);
                    gv_icon.setAdapter(photoAdapter);
                    photoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    private DisplayImageOptions getOptions() {
        DisplayImageOptions options = null;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
        return options;
    }

}
