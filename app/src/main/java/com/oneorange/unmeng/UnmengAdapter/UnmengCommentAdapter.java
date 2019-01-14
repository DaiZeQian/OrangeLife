package com.oneorange.unmeng.UnmengAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;
import com.oneorange.view.RoundImageView;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Comment;

/**
 * Created by admin on 2016/6/21.
 */
public class UnmengCommentAdapter extends BaseBaseAdpater<Comment> {

    private DisplayImageOptions options;
    private View.OnClickListener onClickListener;

    public UnmengCommentAdapter(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_feed_comment, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        Comment comment = getItem(position);
        CommUser ownUser = comment.creator;//发出该评论的用户
        holdView.tv_name.setText(ownUser.name);
        holdView.tv_time.setText(comment.createTime);
        holdView.tv_content.setText(comment.text);
        holdView.tv_likenum.setText(comment.likeCount + "");
        ImageLoader.getInstance().displayImage(ownUser.iconUrl == null ? "" : ownUser.iconUrl, holdView.iv_icon, options);
        Comment children = comment.childComment;
        if (children == null) {
            holdView.tv_reply.setVisibility(View.GONE);
        } else {
            String childUserfloor = children.floor + "";
            holdView.tv_reply.setVisibility(View.VISIBLE);
            holdView.tv_reply.setText("回复 @" + childUserfloor + "楼:");
        }
        if (onClickListener != null) {
            holdView.ll_comment.setTag(position);
            holdView.ll_comment.setOnClickListener(onClickListener);
        }

        return convertView;
    }


    class HoldView {
        private RoundImageView iv_icon;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_likenum;
        private TextView tv_commentnum;
        private TextView tv_reply;
        private LinearLayout ll_comment;


        public HoldView(View view) {
            iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_likenum = (TextView) view.findViewById(R.id.tv_likenum);
            tv_commentnum = (TextView) view.findViewById(R.id.tv_commentnum);
            tv_reply = (TextView) view.findViewById(R.id.tv_reply);
            ll_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
        }
    }
}
