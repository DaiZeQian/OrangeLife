package com.oneorange.content;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.oneorange.adapter.FeedPhotoAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUnmengFeed;
import com.oneorange.unmeng.UnmengAdapter.UnmengCommentAdapter;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.NoScrollGridView;
import com.oneorange.view.NoScrollListView;
import com.oneorange.view.RoundImageView;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Comment;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class FeedDetailView extends BaseActivity {

    private NoScrollListView nlv_comment;
    private RoundImageView ivIcon;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvContent;
    private ImageView ivLike;
    private TextView tvLikenum;
    private TextView tvCommentnum;
    private NoScrollGridView gv_icon;
    private LinearLayout ll_like;

    private RelativeLayout rl_send;
    private EditText ev_post;
    private LinearLayout ll_bottom;


    private void assignViews() {
        nlv_comment = (NoScrollListView) findViewById(R.id.nlv_comment);
        ivIcon = (RoundImageView) findViewById(R.id.iv_icon);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        tvLikenum = (TextView) findViewById(R.id.tv_likenum);
        tvCommentnum = (TextView) findViewById(R.id.tv_commentnum);
        rl_send = (RelativeLayout) findViewById(R.id.rl_send);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        ev_post = (EditText) findViewById(R.id.ev_post);

        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        unmengFeed = new GetUnmengFeed(this, feedid, handler);
        gv_icon = (NoScrollGridView) findViewById(R.id.gv_icon);
        photoAdapter = new FeedPhotoAdapter(this, getOption());
    }

    //OP
    private GetUnmengFeed unmengFeed;
    private UnmengCommentAdapter commentAdapter;
    private FeedPhotoAdapter photoAdapter;
    protected InputMethodManager inputManager;
    //data
    private String feedid;
    private String iconUrL;
    private String name;
    private String time;
    private String content;
    private int likenum;
    private int communtnum;
    private boolean isLike;
    private boolean isListComment;//判断是否是评论评论的 还是评论帖子的
    private String commentiid;

    private String postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeddetail);
        getData();
        assignViews();
        getFeedDetail();
        setOnClickListener();
    }

    private void getData() {
        feedid = getIntent().getExtras().getString("feedid");
        if (feedid == null) {
            finish();
            return;
        }
    }


    private void setOnClickListener() {
        rl_send.setOnClickListener(onClickListener);
        ll_like.setOnClickListener(onClickListener);
    }

    private void getFeedDetail() {
        unmengFeed.getFeedDetail();
    }

    private void setDetail(FeedItem feedItem) {
        CommUser user = feedItem.creator;
        name = user.name;
        iconUrL = user.iconUrl;
        time = feedItem.publishTime + "";
        content = feedItem.text;
        likenum = feedItem.likeCount;
        communtnum = feedItem.commentCount;
        isLike = feedItem.isLiked;

        tvName.setText(name);
        ImageLoader.getInstance().displayImage(iconUrL == null ? "" : iconUrL, ivIcon, getOption());
        tvTime.setText(DateUtils.getTimestampString(new Date(Long.decode(time))));
        tvContent.setText(content);
        tvLikenum.setText(likenum + "");
        tvCommentnum.setText(communtnum + "");
        if (isLike) {
            ivLike.setImageResource(R.mipmap.like_on);
        } else {
            ivLike.setImageResource(R.mipmap.like_off);
        }
        List<ImageItem> imageItems = feedItem.imageUrls;
        if (imageItems == null || imageItems.size() <= 0) {
            gv_icon.setVisibility(View.GONE);
        } else {
            gv_icon.setAdapter(photoAdapter);
            photoAdapter.setAdapterDatas(imageItems);
            photoAdapter.notifyDataSetChanged();
        }
    }


    private void setComment(List<Comment> comments) {
        commentAdapter = new UnmengCommentAdapter(this, editOncListener);
        nlv_comment.setAdapter(commentAdapter);
        commentAdapter.setAdapterDatas(comments);
        commentAdapter.notifyDataSetChanged();
    }

    private DisplayImageOptions getOption() {
        DisplayImageOptions options = null;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
        return options;
    }

    ;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    FeedItem feedItem = (FeedItem) msg.obj;
                    setDetail(feedItem);
                    unmengFeed.getFeedComment();
                    break;
                case 2:
                    List<Comment> comments = (List<Comment>) msg.obj;
                    LogUtil.d("comments", comments.toString());
                    setComment(comments);
                    break;
                case 3:
                    unmengFeed.getFeedComment();
                    break;
                case 5:
                    isLike = true;
                    ivLike.setImageResource(R.mipmap.like_on);
                    break;
                case 6:
                    isLike = false;
                    ivLike.setImageResource(R.mipmap.like_off);
                    break;
            }
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_send:
                    postContent = ev_post.getText().toString();
                    if (postContent.length() <= 0) {
                        ToastUtil.show(FeedDetailView.this, R.string.input_null, 100);
                    } else {
                        Comment comment = new Comment();
                        comment.text = postContent;
                        comment.feedId = feedid;
                        if (isListComment) {
                            comment.replyCommentId = commentiid;
                            isListComment = false;
                        }
                        GetUnmengFeed commentFeed = new GetUnmengFeed(FeedDetailView.this, handler, comment);
                        commentFeed.addFeedComment();
                        ev_post.setText("");
                        hideKeyboard();
                    }

                    break;
                case R.id.ll_like:
                    GetUnmengFeed unmengFeed = new GetUnmengFeed(FeedDetailView.this, feedid, handler);
                    if (isLike) {
                        unmengFeed.ulikeFeed();
                    } else {
                        unmengFeed.likeFeed();
                    }
                    break;
            }
        }
    };


    private View.OnClickListener editOncListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            isListComment = true;
            commentiid = commentAdapter.getItem(position).id;
            showKeyboard();
        }
    };

    protected void showKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) ev_post.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(ev_post, 0);
    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
