package com.oneorange.content;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.adapter.ReportAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.entity.OtherUserInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.RoundImageView;

import java.util.ArrayList;

/**
 * Created by admin on 2016/6/13.
 */
public class ChatUserInformationDView extends BaseActivity {

    private RelativeLayout rl_report;
    private RelativeLayout rl_bottom;
    private ListView lv_content;
    private RelativeLayout rl_cancle;


    private TextView tvTitle;
    private ImageView ivMore;
    private RoundImageView ivIcon;
    private TextView tvNickname;
    private ImageView ivGender;
    private ImageView ivAuth;
    private TextView tvResidential;
    private TextView tvUserid;
    private TextView tvSign;

    private void assignViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        ivIcon = (RoundImageView) findViewById(R.id.iv_icon);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        ivGender = (ImageView) findViewById(R.id.iv_gender);
        ivAuth = (ImageView) findViewById(R.id.iv_auth);
        tvResidential = (TextView) findViewById(R.id.tv_residential);
        tvUserid = (TextView) findViewById(R.id.tv_userid);
        tvSign = (TextView) findViewById(R.id.tv_sign);

        rl_report = (RelativeLayout) findViewById(R.id.rl_report);
        lv_content = (ListView) findViewById(R.id.lv_content);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        rl_cancle = (RelativeLayout) findViewById(R.id.rl_cancle);
        tvTitle.setVisibility(View.GONE);
        ivMore.setVisibility(View.GONE);
    }


    //op
    private ReportAdapter reportAdapter;
    //data
    private ArrayList<String> contents = new ArrayList<>();
    private OtherUserInfo otheruser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatuserinformationd);
        assignViews();
        setOnClickListener();
        getData();
        setReport();
    }

    private void getData() {
        otheruser = (OtherUserInfo) getIntent().getExtras().getSerializable("otheruser");
        if (otheruser == null) {
            ToastUtil.show(this, R.string.system_error, 100);
            finish();
            return;
        }
        setUserInfo(otheruser);
    }

    public void setUserInfo(OtherUserInfo userInfo) {
        tvNickname.setText(userInfo.getNickname() == "" ? "未设置" : userInfo.getNickname());
        if (userInfo.getGender().equals("女")) {
            ivGender.setImageResource(R.mipmap.icon_woman);
        } else {
            ivGender.setImageResource(R.mipmap.icon_man);
        }
        tvUserid.setText(userInfo.getCheng_id());
        tvSign.setText(userInfo.getSignature());
        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), ivIcon, getoptions());
    }

    private DisplayImageOptions getoptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
        return options;
    }

    private void setOnClickListener() {
        rl_report.setOnClickListener(onClickListener);
        rl_cancle.setOnClickListener(onClickListener);
        lv_content.setOnItemClickListener(onItemClickListener);
    }

    private void setReport() {
        contents.add("色情、暴力信息");
        contents.add("广告信息");
        contents.add("钓鱼、欺诈信息");
        contents.add("诽谤造谣信息");
        reportAdapter = new ReportAdapter(this);
        lv_content.setAdapter(reportAdapter);
        reportAdapter.setAdapterDatas(contents);
        reportAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_report:
                    if (rl_bottom.getVisibility() == View.GONE && !rl_bottom.isShown()) {
                        rl_bottom.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.rl_cancle:
                    rl_bottom.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rl_bottom.setVisibility(View.GONE);
        }
    };

}
