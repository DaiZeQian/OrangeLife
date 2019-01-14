package com.oneorange.content;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.oneorange.adapter.ChatGroupDetailAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.controller.GetUserListController;
import com.oneorange.entity.GetFriendsInfo;
import com.oneorange.orangelife.R;
import com.oneorange.utils.NetWorkUtils;
import com.oneorange.utils.ToastUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.view.NoScrollGridView;

import java.util.List;

/**
 * Created by admin on 2016/6/3.
 */
public class ChatGroupDetailView extends BaseActivity {

    private NoScrollGridView gvGroup;
    private TextView tvGroupid;
    private TextView tvGroupname;
    private TextView tvOwner;
    private TextView tvGroupnum;
    private TextView tvGroupdes;
    private TextView tv_name;
    private ImageView iv_more;

    private RelativeLayout rl_groupname;
    private RelativeLayout rl_groupdes;


    private void assignViews() {
        gvGroup = (NoScrollGridView) findViewById(R.id.gv_group);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tvGroupid = (TextView) findViewById(R.id.tv_groupid);
        tvGroupname = (TextView) findViewById(R.id.tv_groupname);
        tvOwner = (TextView) findViewById(R.id.tv_owner);
        tvGroupnum = (TextView) findViewById(R.id.tv_groupnum);
        tvGroupdes = (TextView) findViewById(R.id.tv_groupdes);
        rl_groupname = (RelativeLayout) findViewById(R.id.rl_groupname);
        rl_groupdes = (RelativeLayout) findViewById(R.id.rl_groupdes);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        groupDetailAdapter = new ChatGroupDetailAdapter(this);
    }

    //op
    private ChatGroupDetailAdapter groupDetailAdapter;

    private String userName;


    private Dialog moreDialog;

    //data
    private String groupName;
    private String gruopOwner;
    private int groupNum;
    private String des;
    private List<String> groups;
    private String userid;
    private boolean isGroups;//是否是群组里的人
    private boolean isOwenr;//是否是群主
    private String groupid;


    //handler
    private final int GROUP_SUCCESS = 4;
    private final int GROUP_FAILED = 5;

    private final int GROUP_ADD = 6;
    private final int GROUP_RESULT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_detail);
        assignViews();
        setOnClickListener();
        getData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GROUP_SUCCESS:
                    EMGroup group = (EMGroup) msg.obj;
                    setGroupDetail(group);
                    break;
                case GROUP_FAILED:
                    ToastUtil.show(ChatGroupDetailView.this, "无法获取群组信息请稍候重试", 200);
                    finish();
                    break;
                case 1:
                    List<GetFriendsInfo> friendsInfos = (List<GetFriendsInfo>) msg.obj;
                    GetFriendsInfo friendsInfo = new GetFriendsInfo();
                    friendsInfo.setCheng_id("0");
                    friendsInfos.add(friendsInfo);
                    gvGroup.setAdapter(groupDetailAdapter);
                    groupDetailAdapter.setAdapterDatas(friendsInfos);
                    groupDetailAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void getData() {
        userName = getIntent().getExtras().getString("userId");
        if (userName == null) {
            this.finish();
            return;
        }
        getGroupDetail();
    }

    private void getGroupDetail() {
        //根据群组ID从服务器获取群组基本信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(userName);
                    Message msg = new Message();
                    msg.what = GROUP_SUCCESS;
                    msg.obj = group;
                    handler.sendMessage(msg);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(GROUP_FAILED);
                }
            }
        }).start();
    }


    private void setOnClickListener() {
        iv_more.setOnClickListener(onClickListener);
        rl_groupname.setOnClickListener(onClickListener);
        rl_groupdes.setOnClickListener(onClickListener);
        gvGroup.setOnItemClickListener(onItemClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_more:
                    moreDialog.show();
                    break;
                case R.id.rl_groupname:
                    Intent intent = new Intent(ChatGroupDetailView.this, ChatChangeGroupInfoView.class);
                    Bundle groupnameBundle = new Bundle();
                    groupnameBundle.putString("verifycode", "0");
                    groupnameBundle.putString("groupid", groupid);
                    groupnameBundle.putString("content", groupName);
                    intent.putExtras(groupnameBundle);
                    startActivityForResult(intent, 2);
                    break;
                case R.id.rl_groupdes:

                    break;
            }
        }
    };


    private void setGroupDetail(EMGroup group) {
        if (group == null) {
            return;
        }
        userid = UserPrefenceManager.getInstance().getHxid();
        groupName = group.getGroupName();
        gruopOwner = group.getOwner();
        des = group.getDescription();
        groupid = group.getGroupId();
        groupNum = group.getAffiliationsCount();
        groups = group.getMembers();
        tv_name.setText(groupName);
        tvGroupid.setText(userName);
        tvGroupname.setText(groupName);
        tvOwner.setText(gruopOwner);
        tvGroupnum.setText(groupNum + "");
        tvGroupdes.setText(des);
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).equals(userid)) {
                isGroups = true;
            }
        }
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
            isOwenr = true;
        }
        GetUserListController controller = new GetUserListController(handler, this, groups);
        controller.getUserList();
        creaMoreDialog(groupName, isGroups == true ? "退出该群" : "申请加入", isGroups == true ? R.mipmap.chat_group_quit : R.mipmap.chat_group_add);
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (groupDetailAdapter.getItem(position).getCheng_id().equals("0")) {
                Bundle bundle = new Bundle();
                List<GetFriendsInfo> friendsInfos = groupDetailAdapter.adapterDatas;
                bundle.putSerializable("grouplist", groupDetailAdapter.adapterDatas);
                bundle.putBoolean("isowner", isOwenr);
                bundle.putString("groupid", groupid);
                Intent intent = new Intent(ChatGroupDetailView.this, GroupCreateListView.class);
                intent.putExtras(bundle);
                intent.putExtra("verifycode", "2");
                startActivityForResult(intent, GROUP_ADD);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 2:
                    tv_name.setText(data.getStringExtra("content"));
                    tvGroupname.setText(data.getStringExtra("content"));
                    break;
                case 3:

                    break;
            }
        } else if (resultCode == GROUP_RESULT) {
            switch (requestCode) {
                case GROUP_ADD:
                    getGroupDetail();
                    break;
            }
        }
    }

    /**
     * 创建更多详情Dialog   //富含操作
     *
     * @param groupName
     * @return
     */
    public void creaMoreDialog(String groupName, String operation, int resourse) {
        moreDialog = new Dialog(this, R.style.loading_dialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_group_more, null);
        LinearLayout ll_dialog = (LinearLayout) view.findViewById(R.id.ll_dialog);
        TextView tv_dia_name = (TextView) view.findViewById(R.id.tv_dia_name);
        LinearLayout ll_operation = (LinearLayout) view.findViewById(R.id.ll_operation);
        TextView tv_operation = (TextView) view.findViewById(R.id.tv_operation);
        CheckBox cb_groupinformation = (CheckBox) view.findViewById(R.id.cb_groupinformation);
        LinearLayout ll_dissloution = (LinearLayout) view.findViewById(R.id.ll_dissloution);
        ImageView iv_operation = (ImageView) view.findViewById(R.id.iv_operation);
        tv_operation.setText(operation);
        iv_operation.setImageResource(resourse);
        tv_dia_name.setText(groupName);
        ll_operation.setOnClickListener(dialogListener);
        ll_dissloution.setOnClickListener(dialogListener);
        moreDialog.setCancelable(true);// 不可以用“返回键”取消
        moreDialog.setContentView(ll_dialog, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }

    private Thread thread;

    private View.OnClickListener dialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_operation:
                    if (isGroups) {
                        setGroupOpeation(1);
                    } else {
                        setGroupOpeation(2);
                    }
                    break;
                case R.id.ll_dissloution:
                    if (isOwenr) {

                    } else {
                        ToastUtil.show(ChatGroupDetailView.this, R.string.chat_group_owenr_error, 100);
                    }
                    break;
            }
        }
    };

    private void setGroupOpeation(final int code) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (code) {
                        case 1:
                            EMClient.getInstance().groupManager().leaveGroup(groupid);//退出群组
                            ChatGroupDetailView.this.finish();
                            break;
                        case 2:
                            EMClient.getInstance().groupManager().applyJoinToGroup(groupid, "加入");//需异步处理
                            break;
                        case 3:
                            EMClient.getInstance().groupManager().destroyGroup(groupid);//需异步处理  解散群组
                            break;

                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
        NetWorkUtils.getDatafromNet(this, thread);
    }

}
