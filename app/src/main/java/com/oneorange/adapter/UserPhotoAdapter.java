package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;
import com.umeng.comm.core.beans.AlbumItem;
import com.umeng.comm.core.beans.ImageItem;

/**
 * Created by admin on 2016/6/22.
 */
public class UserPhotoAdapter extends BaseBaseAdpater<AlbumItem> {
    private DisplayImageOptions options;

    public UserPhotoAdapter(Context context, DisplayImageOptions options) {
        super(context);
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_feedphoto, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        AlbumItem albumItem = getItem(position);
        ImageItem imageItem = albumItem.cover;
        ImageLoader.getInstance().displayImage(imageItem.thumbnail == null ? "" : imageItem.thumbnail, holdView.iv_icon, options);
        return convertView;
    }


    class HoldView {
        private ImageView iv_icon;

        public HoldView(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }
}
