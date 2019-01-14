package com.oneorange.content;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneorange.base.BaseActivity;
import com.oneorange.controller.UserInfoController;
import com.oneorange.entity.PostInfo;
import com.oneorange.entity.UserInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.volley.Response;
import com.oneorange.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/6/13.
 * 个人用户文本信息修改
 * key   useredit
 * 1.昵称       nick
 * 2.个性签名    sign
 * 3.地址       area
 */
public class UserTextEditView extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSave;
    private LinearLayout llNick;
    private EditText evNick;
    private TextView tvDelete;
    private LinearLayout llArea;
    private EditText evArea;
    private TextView tvAreaDelete;
    private LinearLayout llSign;
    private EditText evSing;
    private TextView tvSignDelete;

    private void assignViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);
        llNick = (LinearLayout) findViewById(R.id.ll_nick);
        evNick = (EditText) findViewById(R.id.ev_nick);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        llArea = (LinearLayout) findViewById(R.id.ll_area);
        evArea = (EditText) findViewById(R.id.ev_area);
        tvAreaDelete = (TextView) findViewById(R.id.tv_area_delete);
        llSign = (LinearLayout) findViewById(R.id.ll_sign);
        evSing = (EditText) findViewById(R.id.ev_sing);
        tvSignDelete = (TextView) findViewById(R.id.tv_sign_delete);


        evNick.addTextChangedListener(new MyTextWatcherUtil(10, evNick));
        evSing.addTextChangedListener(new MyTextWatcherUtil(150, evSing));
    }

    //op
    private UserInfo userInfo;
    //data
    private String value;
    private String nickname;
    private String area;
    private String signature;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertextedit);
        assignViews();
        getData();
        setOnClickListener();
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
    }

    private void getData() {
        value = getIntent().getExtras().getString("useredit");
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userinfo");
        if (value == null) {
            finish();
            return;
        }
        switch (value) {
            case "nick":
                tvTitle.setText("昵称");
                llNick.setVisibility(View.VISIBLE);
                break;
            case "sign":
                tvTitle.setText("签名");
                llSign.setVisibility(View.VISIBLE);
                break;
            case "area":
                tvTitle.setText("地区");
                llArea.setVisibility(View.VISIBLE);
                break;
        }
        nickname = userInfo.getNickname();
        signature = userInfo.getSignature();
        gender = userInfo.getGender();

        evNick.setText(nickname);
        evSing.setText(signature);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

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
                case R.id.tv_save:
                    switch (value) {
                        case "nick":
                            nickname = evNick.getText().toString();
                            if (nickname.length() <= 0) {
                                ToastUtil.show(UserTextEditView.this, R.string.input_null, 200);
                            } else {
                                userInfo.setNickname(nickname);
                                updateUserInfo(userInfo);
                            }
                            break;
                        case "sign":
                            signature = evSing.getText().toString();
                            if (signature.length() <= 0) {
                                ToastUtil.show(UserTextEditView.this, R.string.input_null, 200);
                            } else {
                                userInfo.setSignature(signature);
                                updateUserInfo(userInfo);
                            }
                            break;
                        case "area":
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * 提交数据
     *
     * @param nickname
     * @param signature
     * @param gender
     * @return
     */
    private JSONObject userPost(String nickname, String signature, String gender) {
        JSONObject jsonObject = new JSONObject();
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("nickname", nickname);
            postObject.put("signature", signature);
            postObject.put("gender", gender);
            jsonObject.put("data", postObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    private void updateUserInfo(UserInfo userInfo) {
        UserInfoController controller = new UserInfoController(handler, userInfo);
        controller.setUserInfo();

       /* String url = UrlConfig.UPDATEUSERINFO_URL;
        LogUtil.d("updateurl", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("token", UserPrefenceManager.getInstance().getToken());
        NetworkController controller = new NetworkController();
        controller.setNetRequestHeader(url, reqHeader);
        controller.setRequestPostParam(userPost(nickname, signature, gender));
        controller.startNetRequestPost(userListener, errorListener);*/
    }

    private Response.Listener<JSONObject> userListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            LogUtil.d("response", response.toString());
            PostInfo postInfo = ParseUtils.getObject(response.toString(), PostInfo.class);
            if (postInfo == null) {
                ToastUtil.show(UserTextEditView.this, R.string.service_error, 200);
            } else {
                switch (postInfo.getCode()) {
                    case 200:
                        finish();
                        break;
                }
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            LogUtil.e("error", error.toString());
            ToastUtil.show(UserTextEditView.this, R.string.service_error, 200);
        }
    };

}
