package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.utils.MyTextWatcherUtil;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.NoScrollGridView;

/**
 * Created by admin on 2016/5/30.
 */
public class GroupCreateView extends BaseActivity {

    private EditText evGroupName;
    private EditText evContent;
    private NoScrollGridView gvGroups;
    private RelativeLayout rl_choose;
    private TextView tv_creat;

    //data

    private final int CHOOSE_FRINED = 1;
    private String groupName;
    private String dec;
    private final int CREAT_SUCCESS = 1;
    private final int CREAT_FAILED = 2;
    private String[] allMembers;


    private void assignViews() {
        evGroupName = (EditText) findViewById(R.id.ev_group_name);
        evContent = (EditText) findViewById(R.id.ev_content);
        gvGroups = (NoScrollGridView) findViewById(R.id.gv_groups);
        rl_choose = (RelativeLayout) findViewById(R.id.rl_choose);
        tv_creat = (TextView) findViewById(R.id.tv_creat);
        evGroupName.addTextChangedListener(new MyTextWatcherUtil(20, evGroupName));
        evContent.addTextChangedListener(new MyTextWatcherUtil(150, evContent));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creat);
        assignViews();
        setOnClickListener();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CREAT_SUCCESS:
                    GroupCreateView.this.finish();
                    ToastUtil.show(GroupCreateView.this, "创建成功", 200);
                    break;
                case CREAT_FAILED:
                    ToastUtil.show(GroupCreateView.this, "创建失败，请稍后重试", 200);
                    break;
            }
        }
    };


    private void setOnClickListener() {
        rl_choose.setOnClickListener(onClickListener);
        tv_creat.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_choose:
                    startActivityForResult(new Intent(GroupCreateView.this, GroupCreateListView.class).putExtra("verifycode", "1"), CHOOSE_FRINED);
                    break;
                case R.id.tv_creat:
                    groupName = evGroupName.getText().toString();
                    dec = evContent.getText().toString();
                    if (groupName.length() <= 0) {
                        ToastUtil.show(GroupCreateView.this, R.string.input_null, 100);
                    } else if (dec.length() <= 0) {
                        ToastUtil.show(GroupCreateView.this, R.string.input_null, 100);
                    } else if (allMembers == null || allMembers.length <= 0) {
                        ToastUtil.show(GroupCreateView.this, R.string.creat_group_null, 100);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                creatGroup(200, groupName, dec, allMembers, "");
                            }
                        }).start();
                    }
                    break;
            }
        }
    };


    /**
     * 创建群组
     *
     * @param groupName  群组名称
     * @param desc       群组简介
     * @param allMembers 群组初始成员，如果只有自己传null即可
     * @param reason     邀请成员加入的reason
     * @param //option   群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link /EMGroupStyle}
     * @return 创建好的group
     * @throws /HyphenateException EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
     *                             EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
     *                             EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
     *                             EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
     *                             目前只创建 个人私有群
     */
    private void creatGroup(int max, String groupName, String desc, String[] allMembers, String reason) {
        EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
        option.maxUsers = max;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
        reason = EMClient.getInstance().getCurrentUser() + "邀请加入群:" + groupName;
        try {
            EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
            handler.sendEmptyMessage(CREAT_SUCCESS);
        } catch (HyphenateException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(CREAT_FAILED);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FRINED && resultCode == RESULT_OK && data != null) {//获取邀请加入群的好友列表
            allMembers = data.getStringArrayExtra("newmembers");
        }
    }
}
