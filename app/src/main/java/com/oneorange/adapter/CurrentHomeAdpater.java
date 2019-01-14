package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/6/13.
 */
public class CurrentHomeAdpater extends BaseBaseAdpater<String> {
    public CurrentHomeAdpater(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_current_home, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        holdView.tv_name.setText(getItem(position));
        return convertView;
    }


    class HoldView {
        private TextView tv_name;

        public HoldView(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
