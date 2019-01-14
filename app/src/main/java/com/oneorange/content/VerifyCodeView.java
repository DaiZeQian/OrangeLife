package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneorange.base.BaseActivity;
import com.oneorange.controller.LoginController;
import com.oneorange.orangelife.R;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.StringUtils;
import com.oneorange.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2016/6/12.
 */
public class VerifyCodeView extends BaseActivity {

    private ImageView iv_back;
    private Button btn_verify;
    private TextView tv_title;
    private EditText ev_phone;
    private EditText ev_code;
    private TextView tv_code;
    private CheckBox checkbox;

    //data
    private int time = 5;
    private Timer timer;

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ev_phone = (EditText) findViewById(R.id.ev_phone);
        ev_code = (EditText) findViewById(R.id.ev_code);
        tv_code = (TextView) findViewById(R.id.tv_code);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        checkbox.setOnCheckedChangeListener(changeListener);
        ev_phone.addTextChangedListener(new MyTextWatcherUtil(11, ev_phone));
        ev_code.addTextChangedListener(new MyTextWatcherUtil(4, ev_code));
    }

    //data
    private String phone;
    private String verifoyCode;
    private int verify = 0;  //0从login界面过来  1从settingview重设密码过来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        initView();
        getData();
        setOnClickListener();
    }

    @Override
    protected void onDestroy() {
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
                        tv_code.setText("剩余" + time + "秒");
                    } else {
                        if (timer != null) {
                            timer.cancel();
                        }
                        timer = null;
                        time = 5;
                        tv_code.setEnabled(true);
                        tv_code.setText("重新发送");
                    }
                    break;
                case 10://register_success   should get token
                    Bundle bundle = new Bundle();
                    bundle.putInt("verify", verify);
                    bundle.putString("verifoyCode", verifoyCode);
                    bundle.putString("phone", phone);
                    openActivityAndCloseThis(RegisterView.class, bundle);
                    break;
                case 11://register_falied
                    break;

            }

        }
    };


    private void getData() {
        try {
            verify = getIntent().getExtras().getInt("verify", 0);
            switch (verify) {
                case 0:
                    tv_title.setText("注册");
                    break;
                case 1:
                    tv_title.setText("重设密码");
                    break;
            }
        } catch (Exception e) {
            finish();
            return;
        }

    }

    private void setTimerTask() {
        tv_code.setEnabled(false);
        tv_code.setText("剩余" + time + "秒");
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

    private void setOnClickListener() {
        iv_back.setOnClickListener(onClickListener);
        btn_verify.setOnClickListener(onClickListener);
        tv_code.setOnClickListener(onClickListener);
    }

    CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                btn_verify.setBackgroundResource(R.drawable.bg_cl_theme);
            } else {
                btn_verify.setBackgroundResource(R.drawable.bg_cl_lightline);
            }
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.btn_verify:
                    phone = ev_phone.getText().toString();
                    verifoyCode = ev_code.getText().toString();
                    if (!StringUtils.isHandset(phone)) {
                        ToastUtil.show(VerifyCodeView.this, R.string.phone_error, 100);
                    } else if (verifoyCode.length() < 4) {
                        ToastUtil.show(VerifyCodeView.this, R.string.code_error, 100);
                    } else if (!checkbox.isChecked()) {
                        ToastUtil.show(VerifyCodeView.this, R.string.observe_error, 100);
                    } else {
                        LoginController controller = new LoginController(phone, verifoyCode, handler, VerifyCodeView.this);
                        controller.regisiter();
                    }
                    break;
                case R.id.tv_code:
                    phone = ev_phone.getText().toString();
                    if (!StringUtils.isHandset(phone)) {
                        ToastUtil.show(VerifyCodeView.this, R.string.phone_error, 100);
                    } else {
                        setTimerTask();
                    }
                    break;
            }
        }
    };

}
