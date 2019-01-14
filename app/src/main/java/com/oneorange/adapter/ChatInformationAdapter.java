package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/6/3.
 */
public class ChatInformationAdapter extends BaseBaseAdpater<NewFriendsDao> {

    private View.OnClickListener rtOnClickListener;
    private View.OnClickListener clOnClickListener;

    public ChatInformationAdapter(Context context, View.OnClickListener rtOnClickListener, View.OnClickListener clOnClickListener) {
        super(context);
        this.rtOnClickListener = rtOnClickListener;
        this.clOnClickListener = clOnClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_chat_information, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        NewFriendsDao info = getItem(position);
        switch (info.getStatus()) {
            case "1":  //好友 被邀请
                holdView.tv_content.setText(info.getUsername() + "请求加你为好友");
                holdView.tv_reaseon.setText(info.getReason());
                holdView.ll_reaseon.setVisibility(View.VISIBLE);
                holdView.ll_add.setVisibility(View.VISIBLE);
                setOnclickListener(holdView.tv_rt, holdView.tv_cl, position);
                break;
            case "2":
                holdView.tv_content.setText(info.getUsername() + "拒绝了你的请求");
                holdView.ll_reaseon.setVisibility(View.GONE);
                holdView.ll_add.setVisibility(View.GONE);
                break;
            case "3":
                holdView.tv_content.setText(info.getUsername() + "同意了你的请求");
                holdView.ll_reaseon.setVisibility(View.GONE);
                holdView.ll_add.setVisibility(View.GONE);
                break;
            case "4":
                break;
            case "5":
                holdView.tv_content.setText("我同意了" + info.getUsername() + "的请求");
                holdView.ll_reaseon.setVisibility(View.GONE);
                holdView.ll_add.setVisibility(View.GONE);
                break;
            case "6":
                holdView.tv_content.setText("我拒绝了" + info.getUsername() + "的请求");
                holdView.ll_reaseon.setVisibility(View.GONE);
                holdView.ll_add.setVisibility(View.GONE);
                break;
        }


        return convertView;
    }


    class HoldView {
        private TextView tv_content;
        private LinearLayout ll_reaseon;
        private TextView tv_reaseon;
        private LinearLayout ll_add;
        private TextView tv_rt;
        private TextView tv_cl;


        public HoldView(View view) {
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            ll_reaseon = (LinearLayout) view.findViewById(R.id.ll_reaseon);
            tv_reaseon = (TextView) view.findViewById(R.id.tv_reaseon);
            ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
            tv_rt = (TextView) view.findViewById(R.id.tv_rt);
            tv_cl = (TextView) view.findViewById(R.id.tv_cl);
        }
    }


    public void setOnclickListener(TextView rt, TextView cl, int position) {
        rt.setTag(R.string.chat_agree, position);
        cl.setTag(R.string.chat_refuse, position);
        rt.setOnClickListener(rtOnClickListener);
        cl.setOnClickListener(clOnClickListener);


    }

}
