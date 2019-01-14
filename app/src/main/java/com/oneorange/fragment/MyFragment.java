package com.oneorange.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.view.RoundImageView;

/**
 * Created by admin on 2016/5/17.
 */
public class MyFragment extends BaseFragment {

    private RelativeLayout rl_settting;//settting
    private LinearLayout ll_user;
    private RelativeLayout rl_home_manager;

    private TextView tv_nick;
    private TextView tv_userid;
    private RoundImageView iv_avatar;

    //data
    private String avatar;


    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        initView(view);
        setOnClickListener();
        return view;
    }

    @Override
    public void laodData() {

    }

    @Override
    public void resetData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        tv_nick.setText(UserPrefenceManager.getInstance().getCurrentNickName() == "" ? "未设置" : UserPrefenceManager.getInstance().getCurrentNickName());
        tv_userid.setText(UserPrefenceManager.getInstance().getHxid());
        avatar = UserPrefenceManager.getInstance().getCurrentAvatar();
        ImageLoader.getInstance().displayImage(avatar, iv_avatar, getoptions());
    }

    private void initView(View view) {
        rl_settting = (RelativeLayout) view.findViewById(R.id.rl_settting);
        ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
        rl_home_manager = (RelativeLayout) view.findViewById(R.id.rl_home_manager);
        tv_nick = (TextView) view.findViewById(R.id.tv_nick);
        tv_userid = (TextView) view.findViewById(R.id.tv_userid);
        iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
    }

    private void setOnClickListener() {
        rl_settting.setOnClickListener(onClickListener);
        ll_user.setOnClickListener(onClickListener);
        rl_home_manager.setOnClickListener(onClickListener);
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_settting:
                    callbackByFragmentId(R.id.my_settting);
                    break;
                case R.id.ll_user:
                    callbackByFragmentId(R.id.my_user_infor);
                    break;
                case R.id.rl_home_manager:
                    callbackByFragmentId(R.id.my_home_manager);
                    break;

            }
        }
    };


}
