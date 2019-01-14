package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.UpdateImageController;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;

/**
 * Created by admin on 2016/6/28.
 */
public class UserAvatarView extends BaseActivity {

    private ImageView iv_icon;
    private TextView tv_update;


    private RelativeLayout rl_bg;
    private LinearLayout ll_photo;
    private LinearLayout ll_local;
    private RelativeLayout rl_cancle;

    //data
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_avatar);
        initView();
        setOnClickListener();
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


    private void initView() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_update = (TextView) findViewById(R.id.tv_update);


        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_local = (LinearLayout) findViewById(R.id.ll_local);
        rl_cancle = (RelativeLayout) findViewById(R.id.rl_cancle);
    }


    private void setOnClickListener() {
        iv_icon.setOnClickListener(onClickListener);
        tv_update.setOnClickListener(onClickListener);

        ll_photo.setOnClickListener(photoOnclickListener);
        ll_local.setOnClickListener(photoOnclickListener);
        rl_cancle.setOnClickListener(photoOnclickListener);
        rl_bg.setOnClickListener(photoOnclickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_icon:
                    if (!rl_bg.isShown() || rl_bg.getVisibility() != View.VISIBLE) {
                        rl_bg.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.tv_update:
                    UpdateImageController controller = new UpdateImageController(path);
                    controller.updateImage();
                    break;
            }
        }
    };


    //照相

    private View.OnClickListener photoOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_photo:
                    Intent intent = new Intent(UserAvatarView.this, TakePhotoView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("photo", 1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.ll_local:
                    Intent intents = new Intent(UserAvatarView.this, TakePhotoView.class);
                    Bundle bundles = new Bundle();
                    bundles.putInt("photo", 2);
                    intents.putExtras(bundles);
                    startActivityForResult(intents, 1);
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.rl_cancle:
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.rl_bg:
                    rl_bg.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            if (data != null) {
                path = data.getStringExtra("path");
                if (path.equals("null")) {
                    return;
                }
                ImageLoader.getInstance().displayImage("file://" + path, iv_icon, getoptions());
            } else {
                ToastUtil.show(UserAvatarView.this, "系统错误，请重试", 200);
            }
        }
    }
}
