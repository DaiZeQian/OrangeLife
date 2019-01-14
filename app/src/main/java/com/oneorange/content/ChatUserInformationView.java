package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseActivity;
import com.oneorange.base.BaseFragmentAdapter;
import com.oneorange.controller.GetUserInfoController;
import com.oneorange.entity.OtherUserInfo;
import com.oneorange.fragment.PhotoFragment;
import com.oneorange.fragment.ThemeFragment;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUnmengUserInfo;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/6/7.
 */
public class ChatUserInformationView extends BaseActivity {

    private LinearLayout llPhoto;
    private TextView tvPhotonum;
    private TextView tvPhoto;
    private LinearLayout llTheme;
    private TextView tvThemeNum;
    private TextView tvTheme;
    private ViewPager viewpager;


    private TextView tvTitle;
    private ImageView ivMore;
    private RoundImageView ivIcon;
    private TextView tvNickname;
    private ImageView ivGender;
    private ImageView ivAuth;
    private TextView tvResidential;
    private TextView tvUserid;
    private TextView tvSign;
    private LinearLayout ll_addfriends;


    private void assignViews() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        llPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        tvPhotonum = (TextView) findViewById(R.id.tv_photonum);
        tvPhoto = (TextView) findViewById(R.id.tv_photo);
        llTheme = (LinearLayout) findViewById(R.id.ll_theme);
        tvThemeNum = (TextView) findViewById(R.id.tv_theme_num);
        tvTheme = (TextView) findViewById(R.id.tv_theme);


        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivMore = (ImageView) findViewById(R.id.iv_more);
        ivIcon = (RoundImageView) findViewById(R.id.iv_icon);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        ivGender = (ImageView) findViewById(R.id.iv_gender);
        ivAuth = (ImageView) findViewById(R.id.iv_auth);
        tvResidential = (TextView) findViewById(R.id.tv_residential);
        tvUserid = (TextView) findViewById(R.id.tv_userid);
        tvSign = (TextView) findViewById(R.id.tv_sign);
        ll_addfriends = (LinearLayout) findViewById(R.id.ll_addfriends);
    }

    //op
    private PhotoFragment photoFragment;
    private ThemeFragment themeFragment;
    private BaseFragmentAdapter fragmentAdapter;

    //data
    private String userId;
    private OtherUserInfo otherUserInfo;
    private boolean isLoad;
    private boolean issend;
    //data
    private final int ADD_SUCCESS = 2;
    private final int ADD_FALIED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_userinformation);
        assignViews();
        setOnClickListener();
        getData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ADD_SUCCESS:
                    issend = false;
                    ToastUtil.show(ChatUserInformationView.this, "发送成功", 200);
                    break;
                case ADD_FALIED:
                    issend = false;
                    ToastUtil.show(ChatUserInformationView.this, "发送失败", 200);
                    break;
                case 9:
                    String uid = (String) msg.obj;
                    initFragment(uid);
                    break;
                case 16:
                    OtherUserInfo userInfo = (OtherUserInfo) msg.obj;
                    setUserInfo(userInfo);
                    break;
            }
        }
    };

    private void getData() {
        userId = getIntent().getExtras().getString("userId");
        if (userId == null) {
            this.finish();
            return;
        }
        getUserInfo(userId);
        GetUnmengUserInfo getUnmengUserInfo = new GetUnmengUserInfo(userId, null, this, handler);
        getUnmengUserInfo.getUmUserInfo();
    }

    private void getUserInfo(String userId) {
        GetUserInfoController userInfoController = new GetUserInfoController(userId, this, handler);
        userInfoController.getOthreUserInfo();
    }

    public void setUserInfo(OtherUserInfo userInfo) {
        otherUserInfo = userInfo;
        tvNickname.setText(userInfo.getNickname() == "" ? "未设置" : userInfo.getNickname());
        if (userInfo.getGender().equals("女")) {
            ivGender.setImageResource(R.mipmap.icon_woman);
        } else {
            ivGender.setImageResource(R.mipmap.icon_man);
        }
        tvUserid.setText(userInfo.getCheng_id());
        tvSign.setText(userInfo.getSignature());
        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), ivIcon, getoptions());
        isLoad = true;
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
        ivMore.setOnClickListener(onClickListener);
        viewpager.addOnPageChangeListener(onPageChangeListener);
        llPhoto.setOnClickListener(onClickListener);
        llTheme.setOnClickListener(onClickListener);
        ll_addfriends.setOnClickListener(onClickListener);
    }

    private void initFragment(String uid) {
        photoFragment = new PhotoFragment(uid);
        themeFragment = new ThemeFragment(uid);
        fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragmentToAdapter(photoFragment);
        fragmentAdapter.addFragmentToAdapter(themeFragment);
        viewpager.setAdapter(fragmentAdapter);
        fragmentAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_more:
                    if (isLoad) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("otheruser", otherUserInfo);
                        openActivity(ChatUserInformationDView.class, bundle);
                    }
                    break;
                case R.id.ll_photo:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.ll_theme:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.ll_addfriends:
                    if (!issend) {
                        addFriend();
                        issend = true;
                    }
                    break;
            }
        }
    };

    private void addFriend() {
        try {
            EMClient.getInstance().contactManager().addContact(userId, "test");
            handler.sendEmptyMessage(ADD_SUCCESS);
        } catch (HyphenateException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(ADD_FALIED);
        }
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tvPhoto.setTextColor(getResources().getColor(R.color.theme));
                    tvTheme.setTextColor(getResources().getColor(R.color.text_l));
                    break;
                case 1:
                    tvTheme.setTextColor(getResources().getColor(R.color.theme));
                    tvPhoto.setTextColor(getResources().getColor(R.color.text_l));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
