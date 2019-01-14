package com.oneorange.content;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneorange.adapter.UserChooseAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.entity.PostInfo;
import com.oneorange.entity.UserChoose;
import com.oneorange.entity.UserInfo;
import com.oneorange.manager.UrlConfig;
import com.oneorange.orangelife.R;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.NetworkController;
import com.oneorange.utils.ParseUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.volley.Response;
import com.oneorange.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2016/6/13.
 * <p>
 * 用户个人信息选择性修改
 * key  useredit
 * 1.性别      sex
 * 2.社交信息   community
 */
public class UserChooseEditView extends BaseActivity {

    private ImageView ivBack;
    private TextView tvSave;
    private TextView tv_title;
    private ListView lvContent;

    //op
    private UserChooseAdapter chooseAdapter;


    //
    private UserChoose userChoose;
    private ArrayList<UserChoose> userChooses;
    private String value;

    private UserInfo userInfo;
    private String sex;
    private String community;
    private String context;


    private void assignViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSave = (TextView) findViewById(R.id.tv_save);
        lvContent = (ListView) findViewById(R.id.lv_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchooseedit);
        assignViews();
        getData();
        setOnClickListener();
    }


    private void getData() {
        value = getIntent().getExtras().getString("useredit");
        userInfo = (UserInfo) getIntent().getExtras().getSerializable("userinfo");
        if (value == null) {
            finish();
            return;
        }
        sex = userInfo.getGender();
        switch (value) {
            case "sex":
                tv_title.setText("性别");
                userChooses = new ArrayList<>();
                userChoose = new UserChoose("男");
                userChooses.add(userChoose);
                userChoose = new UserChoose("女");
                userChooses.add(userChoose);
                userChoose = new UserChoose("保密");
                userChooses.add(userChoose);
                for (int i = 0; i < userChooses.size(); i++) {
                    if (userChooses.get(i).getContent().equals(sex)) {
                        userChooses.get(i).setIsChoose(true);
                    }
                }
                break;
            case "community":
                tv_title.setText("社交信息");
                userChooses = new ArrayList<>();
                userChoose = new UserChoose("保密，任何人都不能看到");
                userChooses.add(userChoose);
                userChoose = new UserChoose("对邻里显示，邻里可见");
                userChooses.add(userChoose);
                userChoose = new UserChoose("社区中显示，发帖或回复是可以看到你的昵称");
                userChooses.add(userChoose);
                userChoose = new UserChoose("公开，任何人都能看到", true);
                userChooses.add(userChoose);
                break;
        }
        chooseAdapter = new UserChooseAdapter(this);
        lvContent.setAdapter(chooseAdapter);
        chooseAdapter.setAdapterDatas(userChooses);
        chooseAdapter.notifyDataSetChanged();


    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
        lvContent.setOnItemClickListener(onItemClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.tv_save:
                    for (int i = 0; i < userChooses.size(); i++) {
                        if (chooseAdapter.getItem(i).isChoose()) {
                            context = chooseAdapter.getItem(i).getContent();
                        }
                    }
                    switch (value) {
                        case "sex":
                            updateUserInfo("gender", context);
                            break;
                        case "community":
                            break;
                    }
                    break;
            }
        }
    };


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < userChooses.size(); i++) {
                if (i == position) {
                    chooseAdapter.getItem(i).setIsChoose(true);
                } else {
                    chooseAdapter.getItem(i).setIsChoose(false);
                }
            }
            chooseAdapter.notifyDataSetChanged();
        }
    };


    private JSONObject userinfoJSON(String name, String context) {
        JSONObject jsonObject = new JSONObject();
        JSONObject postJson = new JSONObject();
        try {
            postJson.put(name, context);
            jsonObject.put("data", postJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void updateUserInfo(String name, String context) {
        String url = UrlConfig.UPDATEUSERINFO_URL;
        LogUtil.d("updateUrl", url);
        HashMap<String, String> reqHeader = new HashMap<String, String>();
        reqHeader.put("token", UserPrefenceManager.getInstance().getToken());
        NetworkController controller = new NetworkController();
        controller.setNetRequestHeader(url, reqHeader);
        controller.setRequestPostParam(userinfoJSON(name, context));

        controller.startNetRequestPost(userListener, errorListener);
    }


    private Response.Listener<JSONObject> userListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            LogUtil.d("response", response.toString());
            PostInfo postInfo = ParseUtils.getObject(response.toString(), PostInfo.class);
            if (postInfo == null) {
                ToastUtil.show(UserChooseEditView.this, R.string.service_error, 200);
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
            ToastUtil.show(UserChooseEditView.this, R.string.service_error, 200);
        }
    };

}
