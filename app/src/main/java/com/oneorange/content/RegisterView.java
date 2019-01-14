package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oneorange.base.BaseActivity;
import com.oneorange.controller.LoginController;
import com.oneorange.controller.UserInfoController;
import com.oneorange.controller.UserPsdController;
import com.oneorange.entity.UserInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;

/**
 * Created by admin on 2016/5/17.
 */
public class RegisterView extends BaseActivity {


    private Button btnRegister;
    private TextView tv_title;
    private TextView tv_phone;//cheng id
    private EditText ev_psd;
    private EditText ev_rtpsd;

    private void assignViews() {
        btnRegister = (Button) findViewById(R.id.btn_register);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        ev_psd = (EditText) findViewById(R.id.ev_psd);
        ev_rtpsd = (EditText) findViewById(R.id.ev_rtpsd);
    }


    private int verify = 0;
    private String verifoyCode = "";
    private String phone;
    private String userid;

    private String psd;
    private String rtPsd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        assignViews();
        setOnClickListener();
        getData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    UserInfoController controller = new UserInfoController(handler);
                    controller.getUserInfo();
                    break;
                case 4:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    userid = userInfo.getCheng_id();
                    tv_phone.setText(userid);
                    break;
                case 12:
                    openActivityAndCloseThis(UserBasicInforView.class);
                    break;
            }
        }
    };


    private void getData() {
        try {

            verify = getIntent().getExtras().getInt("verify", 0);
            verifoyCode = getIntent().getExtras().getString("verifoyCode");
            phone = getIntent().getExtras().getString("phone");
            loginbycode(phone, verifoyCode, "code");
            switch (verify) {
                case 0:
                    tv_title.setText("注册");
                    break;
                case 1:
                    tv_title.setText("设置新密码");
                    break;
            }
        } catch (Exception e) {
            ToastUtil.show(this, R.string.system_error, 100);
            finish();
            return;
        }
    }


    private void setOnClickListener() {
        btnRegister.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    psd = ev_psd.getText().toString();
                    rtPsd = ev_rtpsd.getText().toString();
                    if (psd.length() < 6) {
                        ToastUtil.show(RegisterView.this, R.string.psd_error, 100);
                    } else if (!rtPsd.equals(psd)) {
                        ToastUtil.show(RegisterView.this, R.string.rtpsd_error, 100);
                    } else {
                        changepsdBytoken(psd);
                        // setPsd(phone, verifoyCode, psd);
                    }
                    break;
            }
        }
    };

    /**
     * 获取token
     *
     * @param name
     * @param code
     * @param type
     */
    private void loginbycode(String name, String code, String type) {
        LoginController controller = new LoginController(handler, name, code, this, type);
        controller.loginbycode();
    }

    private void changepsdBytoken(String psd) {
        UserPsdController psdController = new UserPsdController(psd, handler, this);
        psdController.changePsdBytoken();
    }



}
