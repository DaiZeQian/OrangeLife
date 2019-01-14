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
import com.oneorange.entity.LoginInfo;
import com.oneorange.entity.RegistCodeInfo;
import com.oneorange.entity.RegistInfo;
import com.oneorange.manager.UrlConfig;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.NetworkController;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.StringUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.volley.Response;
import com.oneorange.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/6/12.
 */
public class LoginByPsdFragment extends BaseFragment {

    private EditText evPhone;
    private EditText evPsd;
    private TextView tvForget;
    private Button btnLogin;
    private LinearLayout llWx;

    //data
    private String phone;
    private String psd;

    private void assignViews(View view) {
        evPhone = (EditText) view.findViewById(R.id.ev_phone);
        evPsd = (EditText) view.findViewById(R.id.ev_psd);
        tvForget = (TextView) view.findViewById(R.id.tv_forget);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        llWx = (LinearLayout) view.findViewById(R.id.ll_wx);
        evPhone.addTextChangedListener(new MyTextWatcherUtil(11, evPhone));
        evPsd.addTextChangedListener(new MyTextWatcherUtil(16, evPsd));
    }

    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_psd, null);
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    callbackByFragmentId(R.id.login_psd_login, null);
                    break;
            }
        }
    };

    private void setOnClickListener() {
        btnLogin.setOnClickListener(onClickListener);
        llWx.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wx:
                    ToastUtil.show(getActivity(), "暂未开通", 200);
                    break;
                case R.id.btn_login:
                    phone = evPhone.getText().toString();
                    psd = evPsd.getText().toString();
                    if (!StringUtils.isHandset(phone)) {
                        ToastUtil.show(getActivity(), R.string.phone_error, 100);
                    } else if (psd.length() < 6) {
                        ToastUtil.show(getActivity(), R.string.psd_error, 100);
                    } else {
                        loginbypsd(phone, psd, "password");
                    }
                    break;
            }
        }
    };

    /**
     * 密码登录
     *
     * @param name
     * @param psd
     * @param type
     */
    private void loginbypsd(String name, String psd, String type) {
        LoginController controller = new LoginController(handler, name, psd, getActivity(), type);
        controller.loginbycode();
    }


    private JSONObject postJson(String name, String password) {
        JSONObject jsonObject = new JSONObject();
        JSONObject postJson = new JSONObject();
        try {
            postJson.put("name", name);
            postJson.put("password", password);
            jsonObject.put("data", postJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void loginbypsd(String name, String password) {
        String url = UrlConfig.LOGINBYPSD_URL;
        LogUtil.d("loginurl", url);
        NetworkController controller = new NetworkController();
        controller.setNetRequestHeader(url, null);
        controller.setRequestPostParam(postJson(name, password));
        controller.startNetRequestPost(loginListener, errorListener);
    }

    private Response.Listener<JSONObject> loginListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            LogUtil.d("response", response.toString());
            RegistCodeInfo codeInfo = ParseUtils.getObject(response.toString(), RegistCodeInfo.class);
            if (codeInfo == null) {
                ToastUtil.show(getActivity(), R.string.service_error, 200);
                return;
            } else {
                int code = codeInfo.getCode();
                switch (code) {
                    case 200:
                        RegistInfo registInfo = codeInfo.getData();
                        UserPrefenceManager.getInstance().setUserInfo(registInfo.getToken(), registInfo.getNickname(), registInfo.getAvatar(), phone, psd);
                        UserPrefenceManager.getInstance().setHxid(registInfo.getHxname());

                        LoginInfo loginInfo = new LoginInfo();
                        loginInfo.setPhone(registInfo.getHxname());
                        loginInfo.setPsd(registInfo.getHxpwd());
                        callbackByFragmentId(R.id.login_psd_login, loginInfo);
                        break;
                }
            }


        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            LogUtil.e("error", error.toString());
        }
    };

}
