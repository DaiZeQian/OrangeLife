package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.entity.GetFriendsInfo;
import com.oneorange.orangelife.R;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/6/3.
 */
public class ChatGroupDetailAdapter extends BaseBaseAdpater<GetFriendsInfo> {


    private DisplayImageOptions options;

    public ChatGroupDetailAdapter(Context context) {
        super(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_chat_group_detail, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }

        if (getItem(position).getCheng_id().equals("0")) {
            holdView.add_friend.setVisibility(View.VISIBLE);
            holdView.tv_name.setVisibility(View.INVISIBLE);
            holdView.iv_icon.setVisibility(View.GONE);
        } else {
            holdView.tv_name.setText(getItem(position).getNickname());
            ImageLoader.getInstance().displayImage(getItem(position).getImage(), holdView.iv_icon, options);
            holdView.add_friend.setVisibility(View.GONE);
            holdView.tv_name.setVisibility(View.VISIBLE);
            holdView.iv_icon.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class HoldView {
        private TextView tv_name;
        private RoundImageView iv_icon;
        private ImageView add_friend;
        private LinearLayout ll_user;

        public HoldView(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
            add_friend = (ImageView) view.findViewById(R.id.add_friend);
            ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
        }
    }

}
