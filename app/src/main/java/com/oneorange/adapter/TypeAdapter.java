package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.entity.UserChoose;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/6/13.
 */
public class TypeAdapter extends BaseBaseAdpater<UserChoose> {
    public TypeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_type, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        UserChoose userChoose = getItem(position);
        if (userChoose.isChoose()) {
            holdView.tv_type.setTextColor(context.getResources().getColor(R.color.theme));
            holdView.tv_type.setBackgroundResource(R.drawable.bg_cl_theme_tran);
        } else {
            holdView.tv_type.setTextColor(context.getResources().getColor(R.color.text_s));
            holdView.tv_type.setBackgroundResource(R.drawable.bg_cl_lightline_tran);
        }
        holdView.tv_type.setText(userChoose.getContent());
        return convertView;
    }

    class HoldView {
        private TextView tv_type;

        public HoldView(View view) {
            tv_type = (TextView) view.findViewById(R.id.tv_type);
        }
    }
}
