package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oneorange.base.BaseActivity;
import com.oneorange.controller.UserPsdController;
import com.oneorange.orangelife.R;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.ToastUtil;

/**
 * Created by admin on 2016/6/30.
 * <p/>
 * 重设密码页面
 */
public class ResetPsdView extends BaseActivity {

    private TextView tvTitle;
    private EditText evOldpsd;
    private TextView tvForget;
    private EditText evPsd;
    private EditText evRtpsd;
    private Button btnRt;


    private void assignViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        evOldpsd = (EditText) findViewById(R.id.ev_oldpsd);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        evPsd = (EditText) findViewById(R.id.ev_psd);
        evRtpsd = (EditText) findViewById(R.id.ev_rtpsd);
        btnRt = (Button) findViewById(R.id.btn_rt);
        evOldpsd.addTextChangedListener(new MyTextWatcherUtil(12, evOldpsd));
        evPsd.addTextChangedListener(new MyTextWatcherUtil(12, evPsd));
        evRtpsd.addTextChangedListener(new MyTextWatcherUtil(12, evRtpsd));
    }

    //data
    private String oldpsd;
    private String newpsd;
    private String rtpsd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpsd);
        assignViews();
        setOnClickListener();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    ToastUtil.show(ResetPsdView.this, R.string.change_success, 100);
                    break;
            }
        }
    };


    private void setOnClickListener() {
        tvForget.setOnClickListener(onClickListener);
        btnRt.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_forget:

                    break;
                case R.id.btn_rt:
                    oldpsd = evOldpsd.getText().toString();
                    newpsd = evPsd.getText().toString();
                    rtpsd = evRtpsd.getText().toString();
                    if (oldpsd.length() < 6 || newpsd.length() < 6 || rtpsd.length() < 6) {
                        ToastUtil.show(ResetPsdView.this, R.string.input_error, 100);
                    } else {
                        UserPsdController controller = new UserPsdController(oldpsd, newpsd, rtpsd, handler, ResetPsdView.this);
                        controller.resetPsd();
                    }
                    break;
            }
        }
    };
}
