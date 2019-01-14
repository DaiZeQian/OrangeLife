package com.oneorange.unmeng.UnmengAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.oneorange.adapter.FeedPhotoAdapter;
import com.oneorange.base.BaseBaseAdpater;
import com.oneorange.orangelife.R;
import com.oneorange.view.NoScrollGridView;
import com.oneorange.view.RoundImageView;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/6/21.
 */
public class FeedAdapter extends BaseBaseAdpater<FeedItem> {

    private DisplayImageOptions options;

    private View.OnClickListener onClickListener;

    public FeedAdapter(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_boy) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_boy)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_boy)  //设置图片加载/解码过程中错误时候显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//是否缓存在sd卡
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_feed, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        FeedItem feedItem = getItem(position);
        CommUser commUser = feedItem.creator;
        holdView.tv_name.setText(commUser.name);
        holdView.tv_content.setText(feedItem.text);
        holdView.tv_time.setText(DateUtils.getTimestampString(new Date(Long.decode(feedItem.publishTime))));
        holdView.tv_likenum.setText(feedItem.likeCount + "");
        holdView.tv_commentnum.setText(feedItem.commentCount + "");
        if (feedItem.isLiked) {
            holdView.iv_like.setImageResource(R.mipmap.like_on);
        } else {
            holdView.iv_like.setImageResource(R.mipmap.like_off);
        }
        if (onClickListener != null) {
            holdView.ll_like.setTag(position);
            holdView.ll_like.setOnClickListener(onClickListener);
            holdView.rl_comment.setTag(position);
            holdView.rl_comment.setOnClickListener(onClickListener);
        }

        ImageLoader.getInstance().displayImage(commUser.iconUrl == null ? "" : commUser.iconUrl, holdView.iv_icon, options);
        List<ImageItem> imageItems = feedItem.imageUrls;
        if (imageItems == null || imageItems.size() <= 0) {
            holdView.gv_icon.setVisibility(View.GONE);
        } else {
            holdView.gv_icon.setVisibility(View.VISIBLE);
            FeedPhotoAdapter photoAdapter = new FeedPhotoAdapter(context, options);
            photoAdapter.setAdapterDatas(imageItems);
            holdView.gv_icon.setAdapter(photoAdapter);
            photoAdapter.notifyDataSetChanged();

        }
        return convertView;
    }

    class HoldView {
        private TextView tv_name;
        private RoundImageView iv_icon;
        private TextView tv_time;
        private TextView tv_content;
        private ImageView iv_like;
        private TextView tv_likenum;
        private TextView tv_commentnum;
        private TextView tv_comment;
        private NoScrollGridView gv_icon;
        private LinearLayout ll_like;
        private RelativeLayout rl_comment;


        public HoldView(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            tv_likenum = (TextView) view.findViewById(R.id.tv_likenum);
            tv_commentnum = (TextView) view.findViewById(R.id.tv_commentnum);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            gv_icon = (NoScrollGridView) view.findViewById(R.id.gv_icon);
            ll_like = (LinearLayout) view.findViewById(R.id.ll_like);
            rl_comment = (RelativeLayout) view.findViewById(R.id.rl_comment);
        }
    }


}
