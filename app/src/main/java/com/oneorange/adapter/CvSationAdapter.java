package com.oneorange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.db.UserFriendsDao;
import com.oneorange.orangelife.R;
import com.oneorange.view.RoundImageView;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/5/23.
 * <p>
 * 消息列表
 */
public class CvSationAdapter extends BaseBaseAdpater<EMConversation> {

    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;

    public CvSationAdapter(Context context) {
        super(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
    }

    private DisplayImageOptions options;

    @Override
    public int getItemViewType(int position) {
        EMConversation conversation = getItem(position);
        if (conversation.getType() == EMConversation.EMConversationType.Chat) {
            return TYPE_1;
        } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            return TYPE_2;
        } else {
            return TYPE_3;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        HoldViewType1 type1 = null;
        HoldViewType2 type2 = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_1://个人
                    convertView = layoutInflater.inflate(R.layout.item_conversation_single, null);
                    type1 = new HoldViewType1(convertView);
                    convertView.setTag(type1);
                    break;
                case TYPE_2://群
                    convertView = layoutInflater.inflate(R.layout.item_conversation_group, null);
                    type2 = new HoldViewType2(convertView);
                    convertView.setTag(type2);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    type1 = (HoldViewType1) convertView.getTag();
                    break;
                case TYPE_2:
                    type2 = (HoldViewType2) convertView.getTag();
                    break;
            }
        }
        EMConversation conversation = getItem(position);
        String username = conversation.getUserName();//用户名称 或者群聊名称
        int unreadMsgCount = conversation.getUnreadMsgCount();//未读数量
        // 把最后一条消息的内容作为item的message内容
        EMMessage lastMessage = conversation.getLastMessage();
        long lastTime = lastMessage.getMsgTime();//最后的信息的时间
        switch (type) {
            case TYPE_1://个人聊天
                List<UserFriendsDao> friendsDaos = DataSupport.where("userid = ?", username).find(UserFriendsDao.class);
                if (friendsDaos.size() > 0) {
                    ImageLoader.getInstance().displayImage(friendsDaos.get(0).getAvatar(), type1.iv_icon, options);
                } else {
                    type1.iv_icon.setImageResource(R.drawable.default_boy);
                }
                type1.tv_nickname.setText(conversation.getExtField() == null ? username : conversation.getExtField());
                type1.tv_content.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, (this.context))), TextView.BufferType.SPANNABLE);
                type1.tv_time.setText(DateUtils.getTimestampString(new Date(lastTime)));
                if (unreadMsgCount > 0) {
                    type1.tv_num.setText("+" + unreadMsgCount + "");
                    type1.tv_num.setVisibility(View.VISIBLE);
                } else {
                    type1.tv_num.setVisibility(View.INVISIBLE);
                }
                break;
            case TYPE_2://群聊信息
                EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                type2.tv_group_name.setText(group != null ? group.getGroupName() : username);
                type2.tv_group_content.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, (this.context))), TextView.BufferType.SPANNABLE);
                type2.tv_group_time.setText(DateUtils.getTimestampString(new Date(lastTime)));
                String lastName = conversation.getLastMessage().getFrom();
                type2.tv_last_user.setText(conversation.getLastMessage().getStringAttribute("nickName", lastName));
                if (unreadMsgCount > 0) {
                    type2.tv_group_num.setText("+" + unreadMsgCount + "");
                    type2.tv_group_num.setVisibility(View.VISIBLE);
                } else {
                    type2.tv_group_num.setVisibility(View.INVISIBLE);
                }

                ImageLoader.getInstance().displayImage(conversation.getLastMessage().getStringAttribute("headUrl", ""), type2.iv_group_latsicon, options);
                break;

        }
        return convertView;
    }


    class HoldViewType1 {

        private TextView tv_nickname;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_num;
        private RoundImageView iv_icon;


        public HoldViewType1(View view) {

            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
            iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
        }
    }

    class HoldViewType2 {

        private TextView tv_group_name;
        private TextView tv_group_num;
        private TextView tv_last_user;
        private TextView tv_group_content;
        private TextView tv_group_time;
        private RoundImageView iv_group_latsicon;


        public HoldViewType2(View view) {
            tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
            tv_group_num = (TextView) view.findViewById(R.id.tv_group_num);
            tv_last_user = (TextView) view.findViewById(R.id.tv_last_user);
            tv_group_content = (TextView) view.findViewById(R.id.tv_group_content);
            tv_group_time = (TextView) view.findViewById(R.id.tv_group_time);
            iv_group_latsicon = (RoundImageView) view.findViewById(R.id.iv_group_latsicon);
        }
    }

}
