package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.oneorange.base.BaseActivity;
import com.oneorange.base.BaseFragment;
import com.oneorange.base.BaseFragmentAdapter;
import com.oneorange.controller.LoginController;
import com.oneorange.controller.UserInfoController;
import com.oneorange.db.NewFriendsDao;
import com.oneorange.entity.UserInfo;
import com.oneorange.fragment.LoginByCodeFragment;
import com.oneorange.fragment.LoginByPsdFragment;
import com.oneorange.orangelife.HomeActivity;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.UnmengLogin;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.crud.DataSupport;

/**
 * Created by admin on 2016/5/17.
 */
public class LoginView extends BaseActivity implements BaseFragment.FragmentItemOnclickListener {

    private ViewPager viewpager;
    private TextView tv_regisiter;
    private TextView tv_psd;
    private TextView tv_code;


    //op
    private LoginByPsdFragment psdFragment;
    private LoginByCodeFragment codeFragment;
    private BaseFragmentAdapter fragmentAdapter;
    //
    private String userName;
    private String psd;
    private String userId;//橙id 全平台指定id
    private boolean autologin;


    private final int LOGIN_SUCCESS = 1;
    private final int LOGIN_FAILED = 2;

    private void assignViews() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_regisiter = (TextView) findViewById(R.id.tv_regisiter);
        tv_psd = (TextView) findViewById(R.id.tv_psd);
        tv_code = (TextView) findViewById(R.id.tv_login_code);
        psdFragment = new LoginByPsdFragment();
        codeFragment = new LoginByCodeFragment();
        fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(fragmentAdapter);
        fragmentAdapter.addFragmentToAdapter(psdFragment);
        fragmentAdapter.addFragmentToAdapter(codeFragment);
        fragmentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (OrangeLifeHelper.getInstance().isLoggedIn()) {
            autologin = true;
            openActivityAndCloseThis(HomeActivity.class);
            return;
        }*/
        setContentView(R.layout.activity_login);
        assignViews();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autologin) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case 4:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    LoginController hxController = new LoginController(userInfo.getCheng_id(), userInfo.getExtra_password(), LoginView.this, handler);
                    hxController.loginhx();
                    break;
                case 5:
                    UnmengLogin unmengLogin = new UnmengLogin(UserPrefenceManager.getInstance().getCurrentNickName(), UserPrefenceManager.getInstance().getHxid(), handler, LoginView.this);
                    unmengLogin.login();
                    break;
                case 6:
                    ToastUtil.show(LoginView.this, R.string.login_failed, 200);
                    break;
                case 7:
                    openActivity(HomeActivity.class);
                    break;
                case 8:
                    ToastUtil.show(LoginView.this, R.string.login_failed, 200);
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_regisiter:
                    Bundle bundle = new Bundle();
                    bundle.putInt("verify", 0);
                    openActivity(VerifyCodeView.class, bundle);
                    break;
                case R.id.tv_psd:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.tv_login_code:
                    viewpager.setCurrentItem(1);
                    break;
            }
        }
    };

    private void setOnClickListener() {
        tv_regisiter.setOnClickListener(onClickListener);
        viewpager.addOnPageChangeListener(onPageChangeListener);
        tv_psd.setOnClickListener(onClickListener);
        tv_code.setOnClickListener(onClickListener);
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tv_psd.setTextColor(getResources().getColor(R.color.theme));
                    tv_code.setTextColor(getResources().getColor(R.color.text_h));
                    break;
                case 1:
                    tv_code.setTextColor(getResources().getColor(R.color.theme));
                    tv_psd.setTextColor(getResources().getColor(R.color.text_h));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * login
     *
     * @param name
     * @param psd
     */
    private void login(String name, String psd) {
        DataSupport.deleteAll(NewFriendsDao.class);//登录之前清空数据中的数据
        EMClient.getInstance().login(name, psd, new EMCallBack() {
            @Override
            public void onSuccess() {
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                handler.sendEmptyMessage(LOGIN_SUCCESS);
            }

            @Override
            public void onError(final int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LoginView.this, i + "", 2000);
                    }
                });
                handler.sendEmptyMessage(LOGIN_FAILED);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    @Override
    public void onFragmentItemClick(String tag, int eventTag, Object data) {
        if (tag.equals(LoginByPsdFragment.class.getSimpleName())) {
            switch (eventTag) {
                case R.id.login_psd_login:
                    UserInfoController controller = new UserInfoController(handler);
                    controller.getUserInfo();
                    break;
            }
        } else if (tag.equals(LoginByCodeFragment.class.getSimpleName())) {
            switch (eventTag) {
                case R.id.login_code_login:
                    UserInfoController controller = new UserInfoController(handler);
                    controller.getUserInfo();
                    break;

            }
        }
    }


}
