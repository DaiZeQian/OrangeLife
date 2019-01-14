package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.entity.ChatSearchInfo;
import com.oneorange.orangelife.R;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/6/15.
 */
public class ChatSearchAdapter extends BaseBaseAdpater<ChatSearchInfo> {
    private DisplayImageOptions options;
    private DisplayImageOptions groupoptions;

    public ChatSearchAdapter(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                        // .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
        groupoptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_group) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_group)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_group)  //设置图片加载/解码过程中错误时候显示的图片
                        // .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_chat_search, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        ChatSearchInfo info = getItem(position);
        holdView.tv_username.setText(info.getNickname());
        switch (info.getType()) {
            case "0"://个人
                holdView.tv_type.setText("来自个人");
                ImageLoader.getInstance().displayImage(info.getUrl()==null?"":info.getUrl(), holdView.iv_avatar, options);
                break;
            case "1"://群组
                holdView.tv_type.setText("来自群组");
                ImageLoader.getInstance().displayImage(info.getUrl()==null?"":info.getUrl(), holdView.iv_avatar, groupoptions);
                break;
        }

        return convertView;
    }


    class HoldView {
        private TextView tv_username;
        private TextView tv_type;
        private RoundImageView iv_avatar;

        public HoldView(View view) {
            tv_username = (TextView) view.findViewById(R.id.tv_username);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
        }
    }


}
