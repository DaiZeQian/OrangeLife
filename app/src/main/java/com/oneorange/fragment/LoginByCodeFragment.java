package com.oneorange.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneorange.base.BaseFragment;
import com.oneorange.controller.LoginController;
import com.oneorange.orangelife.R;
import com.oneorange.utils.StringUtils;
import com.oneorange.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2016/6/12.
 */
public class LoginByCodeFragment extends BaseFragment {

    private EditText evPhone;
    private EditText evCode;
    private TextView tvCode;
    private Button btnLogin;
    private LinearLayout llWx;

    private void assignViews(View view) {
        evPhone = (EditText) view.findViewById(R.id.ev_phone);
        evCode = (EditText) view.findViewById(R.id.ev_code);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        llWx = (LinearLayout) view.findViewById(R.id.ll_wx);
    }

    //data
    private int time = 5;
    private Timer timer;
    //data
    private String phone;
    private String verifoyCode;


    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_code, null);
        assignViews(view);
        return view;
    }

    @Override
    public void laodData() {
        setOnClickListener();
    }

    @Override
    public void resetData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        tvCode.setText("剩余" + time + "秒");
                    } else {
                        if (timer != null) {
                            timer.cancel();
                        }
                        timer = null;
                        time = 5;
                        tvCode.setEnabled(true);
                        tvCode.setText("重新发送");
                    }
                    break;
                case 2:
                    callbackByFragmentId(R.id.login_code_login, null);
                    break;
            }
        }
    };

    private void setOnClickListener() {
        btnLogin.setOnClickListener(onClickListener);
        llWx.setOnClickListener(onClickListener);
        tvCode.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    phone = evPhone.getText().toString();
                    verifoyCode = evCode.getText().toString();
                    if (!StringUtils.isHandset(phone)) {
                        ToastUtil.show(getActivity(), R.string.phone_error, 100);
                    } else if (verifoyCode.length() < 4) {
                        ToastUtil.show(getActivity(), R.string.code_error, 100);
                    } else {
                        loginbycode(phone, verifoyCode, "code");
                    }
                    break;
                case R.id.ll_wx:
                    ToastUtil.show(getActivity(), R.string.Suspend_opening, 200);
                    break;
                case R.id.tv_code:
                    phone = evPhone.getText().toString();
                    if (!StringUtils.isHandset(phone)) {
                        ToastUtil.show(getActivity(), R.string.phone_error, 100);
                    } else {
                        getcode(phone);
                        setTimerTask();
                    }
                    break;
            }
        }
    };

    private void setTimerTask() {
        tvCode.setEnabled(false);
        tvCode.setText("剩余" + time + "秒");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time--;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 1000, 1000);
    }


    /**
     * 获取验证码
     *
     * @param phone 手机号
     */
    private void getcode(String phone) {
        LoginController controller = new LoginController(phone, null, getActivity());
        controller.getCode();
    }

    /**
     * 帐号登录
     *
     * @param name
     * @param code
     * @param type
     */

    private void loginbycode(String name, String code, String type) {
        LoginController controller = new LoginController(handler, name, code, getActivity(), type);
        controller.loginbycode();
    }

}
