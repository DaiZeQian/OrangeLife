package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;
import com.umeng.comm.core.beans.Topic;

/**
 * Created by admin on 2016/6/21.
 */
public class AlltopicAdapter extends BaseBaseAdpater<Topic> {
    private DisplayImageOptions options;

    public AlltopicAdapter(Context context) {
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
            convertView = layoutInflater.inflate(R.layout.item_alltopic, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        Topic info = getItem(position);
        holdView.tv_num.setText(info.name);
        ImageLoader.getInstance().displayImage(info.icon, holdView.iv_bg, options);
        return convertView;
    }


    class HoldView {
        private ImageView iv_bg;
        private TextView tv_num;

        private HoldView(View view) {
            iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
        }
    }
}
