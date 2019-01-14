package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/6/14.
 */
public class TakePhotoAdpater extends BaseBaseAdpater<String> {
    public TakePhotoAdpater(Context context) {
        super(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.take_photo) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.take_photo)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.take_photo)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
    }

    private DisplayImageOptions options;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_take_photo, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        if (getItem(position).toString().equals("1")) {//1的时候为 无图
            ImageLoader.getInstance().displayImage("file://" + getItem(position).toString(), holdView.iv_photo, options);
            holdView.iv_delete.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage("file://" + getItem(position).toString(), holdView.iv_photo, options);
            holdView.iv_delete.setVisibility(View.VISIBLE);

        }
        return convertView;
    }

    class HoldView {
        private ImageView iv_photo;
        private ImageView iv_delete;

        public HoldView(View view) {
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }
}
