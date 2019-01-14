package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.UserInfoController;
import com.oneorange.entity.UserInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/6/7.
 */
public class UserInformationView extends BaseActivity {

    private TextView tvChengid;
    private RelativeLayout rlHead;
    private RoundImageView ivHead;
    private RelativeLayout rlNick;
    private TextView tvNick;
    private RelativeLayout rlCommunity;
    private TextView tvCommunity;
    private RelativeLayout sex;
    private TextView tvSex;
    private RelativeLayout rlArea;
    private TextView tvArea;
    private RelativeLayout rlSign;
    private TextView tvSign;
    private ImageView iv_back;


    private void assignViews() {
        tvChengid = (TextView) findViewById(R.id.tv_chengid);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        ivHead = (RoundImageView) findViewById(R.id.iv_head);
        rlNick = (RelativeLayout) findViewById(R.id.rl_nick);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        rlCommunity = (RelativeLayout) findViewById(R.id.rl_community);
        tvCommunity = (TextView) findViewById(R.id.tv_community);
        sex = (RelativeLayout) findViewById(R.id.sex);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        rlArea = (RelativeLayout) findViewById(R.id.rl_area);
        tvArea = (TextView) findViewById(R.id.tv_area);
        rlSign = (RelativeLayout) findViewById(R.id.rl_sign);
        tvSign = (TextView) findViewById(R.id.tv_sign);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }


    //op
    private Bundle editBundle;
    private UserInfo userInfo;

    //data
    private String nickName;
    private String gender;
    private String chengid;
    private String avatar;
    private String sign;
    private String area;
    private String community;
    private String name;//目前为手机号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);
        assignViews();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 4:
                    userInfo = (UserInfo) msg.obj;
                    setUserInfo(userInfo);
                    break;
            }
        }
    };


    private void setOnClickListener() {
        rlNick.setOnClickListener(onClickListener);
        rlArea.setOnClickListener(onClickListener);
        rlSign.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        rlCommunity.setOnClickListener(onClickListener);
        sex.setOnClickListener(onClickListener);
        rlHead.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.rl_nick:
                    editBundle = new Bundle();
                    editBundle.putString("useredit", "nick");
                    editBundle.putSerializable("userinfo", userInfo);
                    openActivity(UserTextEditView.class, editBundle);
                    break;
                case R.id.rl_area:
                    editBundle = new Bundle();
                    editBundle.putString("useredit", "area");
                    editBundle.putSerializable("userinfo", userInfo);
                    openActivity(UserTextEditView.class, editBundle);
                    break;
                case R.id.rl_sign:
                    editBundle = new Bundle();
                    editBundle.putString("useredit", "sign");
                    editBundle.putSerializable("userinfo", userInfo);
                    openActivity(UserTextEditView.class, editBundle);
                    break;
                case R.id.rl_community:
                    editBundle = new Bundle();
                    editBundle.putString("useredit", "community");
                    editBundle.putSerializable("userinfo", userInfo);
                    openActivity(UserChooseEditView.class, editBundle);
                    break;
                case R.id.sex:
                    editBundle = new Bundle();
                    editBundle.putString("useredit", "sex");
                    editBundle.putSerializable("userinfo", userInfo);
                    openActivity(UserChooseEditView.class, editBundle);
                    break;
                case R.id.rl_head:
                    openActivity(UserAvatarView.class);
                    break;
            }
        }
    };

    private void setUserInfo(UserInfo userInfo) {
        nickName = userInfo.getNickname();
        gender = userInfo.getGender();
        chengid = userInfo.getCheng_id();
        avatar = userInfo.getUrl();
        sign = userInfo.getSignature();
        name = userInfo.getName();
        UserPrefenceManager.getInstance().setCurrentAvatar(avatar);
        UserPrefenceManager.getInstance().setCurrentNickName(nickName);
        UserPrefenceManager.getInstance().setHxid(chengid);

        tvChengid.setText(chengid);
        tvNick.setText(nickName);
        tvSex.setText(gender);
        tvSign.setText(sign);
        ImageLoader.getInstance().displayImage(avatar, ivHead, getoptions());
    }

    private DisplayImageOptions getoptions() {
        DisplayImageOptions options = null;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
        return options;
    }

    private void getUserInfo() {
        UserInfoController controller = new UserInfoController(handler);
        controller.getUserInfo();
    }


}
