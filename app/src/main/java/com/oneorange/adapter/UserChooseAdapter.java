package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.entity.UserChoose;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/6/13.
 */
public class UserChooseAdapter extends BaseBaseAdpater<UserChoose> {
    public UserChooseAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_userchoose, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        UserChoose userChoose = getItem(position);
        if (userChoose.isChoose()) {
            holdView.iv_choose.setImageResource(R.mipmap.choose_on);
        } else {
            holdView.iv_choose.setImageResource(R.mipmap.choose_off);
        }
        holdView.tv_content.setText(userChoose.getContent());
        return convertView;
    }

    class HoldView {
        private ImageView iv_choose;
        private TextView tv_content;

        public HoldView(View view) {
            iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
