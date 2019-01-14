package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.entity.UserChoose;
import com.oneorange.orangelife.R;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/6/29.
 */
public class BasicAdapter extends BaseBaseAdpater<UserChoose> {
    public BasicAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_basic_info, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        UserChoose userChoose = getItem(position);
        holdView.iv_avatar.setImageResource(userChoose.getResourse());
        holdView.tv_name.setText(userChoose.getContent());
        if (userChoose.isChoose()) {
            holdView.iv_avatar.setmBorderOutsideColor(0xffa2d66d);
            holdView.tv_name.setTextColor(context.getResources().getColor(R.color.hint_three));
        } else {
            holdView.iv_avatar.setmBorderOutsideColor(0x00ffffff);
            holdView.tv_name.setTextColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }


    class HoldView {
        private TextView tv_name;
        private RoundImageView iv_avatar;

        public HoldView(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
        }

    }
}
