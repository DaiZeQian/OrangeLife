package com.oneorange.content;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.EMCallBack;
import com.oneorange.base.BaseActivity;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.manager.OrangeLifeHelper;
import com.oneorange.orangelife.HomeActivity;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2016/6/7.
 */
public class SettingView extends BaseActivity {

    private LinearLayout ll_logout;
    private RelativeLayout rl_reset;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settting);
        initView();
        setOnClickListener();
    }

    private void initView() {
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        rl_reset = (RelativeLayout) findViewById(R.id.rl_reset);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void setOnClickListener() {
        ll_logout.setOnClickListener(onClickListener);
        iv_back.setOnClickListener(onClickListener);
        rl_reset.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_logout:
                    logout();
                    break;
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.rl_reset:
                    Bundle bundle = new Bundle();
                    bundle.putInt("verify", 1);
                    openActivity(VerifyCodeView.class, bundle);
                    break;
            }
        }
    };


    void logout() {
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        if(!isFinishing()){
            pd.show();
        }
        OrangeLifeHelper.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                DataSupport.deleteAll(NewFriendsDao.class);
                HomeActivity.instance.finish();
                pd.cancel();
                openActivityAndCloseThis(LoginView.class);
            }

            @Override
            public void onError(int i, String s) {
                pd.dismiss();
                ToastUtil.show(SettingView.this, "abcd", 200);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }

}
