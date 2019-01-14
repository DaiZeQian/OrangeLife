package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneorange.adapter.CreatFeedAdapter;
import com.oneorange.adapter.TakePhotoAdpater;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.unmeng.GetUmengTopicList;
import com.oneorange.unmeng.GetUnmengFeed;
import com.oneorange.unmeng.UnmengUpimage;
import com.oneorange.unmeng.unmengentity.CreatFeedInfo;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.NoScrollGridView;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;
import com.umeng.comm.core.beans.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/22.
 */
public class CreatFeedView extends BaseActivity {


    private EditText evInput;
    private NoScrollGridView gvPic;
    private NoScrollGridView gvTopic;
    private TextView tv_post;


    private RelativeLayout rl_bg;
    private LinearLayout ll_photo;
    private LinearLayout ll_local;
    private RelativeLayout rl_cancle;

    //op
    private TakePhotoAdpater photoAdpater;
    private CreatFeedAdapter topicAdapter;
    //data
    private ArrayList<String> photoLists;
    private List<ImageItem> imageItems;
    private Topic topic;
    private String content;
    private boolean isChoose;

    private void assignViews() {
        evInput = (EditText) findViewById(R.id.ev_input);
        gvPic = (NoScrollGridView) findViewById(R.id.gv_pic);
        gvTopic = (NoScrollGridView) findViewById(R.id.gv_topic);
        tv_post = (TextView) findViewById(R.id.tv_post);

        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_local = (LinearLayout) findViewById(R.id.ll_local);
        rl_cancle = (RelativeLayout) findViewById(R.id.rl_cancle);

        photoAdpater = new TakePhotoAdpater(this);
        imageItems = new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatfeed);
        assignViews();
        setPhoto();
        getTopic();
        setOnClickListener();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    List<Topic> topics = (List<Topic>) msg.obj;
                    List<CreatFeedInfo> feedInfos = new ArrayList<>();
                    for (int i = 0; i < topics.size(); i++) {
                        CreatFeedInfo feedInfo = new CreatFeedInfo(topics.get(i));
                        feedInfos.add(feedInfo);
                    }
                    setTopic(feedInfos);
                    break;
                case 7:
                    ImageItem imageIte = (ImageItem) msg.obj;
                    imageItems.add(imageIte);
                    postComment(imageItems);
                    break;
            }
        }
    };


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_post:
                    content = evInput.getText().toString();
                    for (int i = 0; i < topicAdapter.getCount(); i++) {
                        if (topicAdapter.getItem(i).isChoose()) {
                            isChoose = true;
                            topic = topicAdapter.getItem(i).getTopic();
                        }
                    }
                    if (content.length() <= 0) {
                        ToastUtil.show(CreatFeedView.this, R.string.input_null, 100);
                    } else if (!isChoose) {
                        ToastUtil.show(CreatFeedView.this, R.string.creat_feed_choosetopic, 100);
                    } else {
                        if (photoLists.size() > 1) {//有图片的时候先上传图片
                            UnmengUpimage unmengUpimage = new UnmengUpimage(photoLists.get(0), handler, CreatFeedView.this);
                            unmengUpimage.update();
                        } else {//没有图片的时候直接上传文件
                            postComment(null);
                        }
                    }
                    break;
            }
        }
    };

    public void postComment(List<ImageItem> imageItems) {

        FeedItem feedItem = new FeedItem();
        feedItem.title = "闲置物品测试2";
        feedItem.text = content;
        feedItem.type = 0;
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);
        feedItem.topics = topics;
        if (imageItems != null) {
            feedItem.imageUrls = imageItems;
        }
        GetUnmengFeed unmengFeed = new GetUnmengFeed(CreatFeedView.this, handler, feedItem);
        unmengFeed.creatFeed();


    }


    private void setOnClickListener() {
        tv_post.setOnClickListener(onClickListener);

        ll_photo.setOnClickListener(photoOnclickListener);
        ll_local.setOnClickListener(photoOnclickListener);
        rl_cancle.setOnClickListener(photoOnclickListener);
        rl_bg.setOnClickListener(photoOnclickListener);
    }

    private void setPhoto() {
        photoLists = new ArrayList<>();
        photoLists.add("1");
        photoAdpater = new TakePhotoAdpater(this);
        gvPic.setAdapter(photoAdpater);
        photoAdpater.setAdapterDatas(photoLists);
        photoAdpater.notifyDataSetChanged();
        gvPic.setOnItemClickListener(photoItemListener);
    }


    private void getTopic() {
        GetUmengTopicList topicList = new GetUmengTopicList(this, handler);
        topicList.getALLTopic();
    }

    private void setTopic(List<CreatFeedInfo> topics) {
        topicAdapter = new CreatFeedAdapter(this);
        gvTopic.setAdapter(topicAdapter);
        topicAdapter.setAdapterDatas(topics);
        topicAdapter.notifyDataSetChanged();
        gvTopic.setOnItemClickListener(onItemClickListener);
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < topicAdapter.getCount(); i++) {
                if (i == position) {
                    topicAdapter.getItem(i).setIsChoose(true);
                } else {
                    topicAdapter.getItem(i).setIsChoose(false);
                }
            }
            topicAdapter.notifyDataSetChanged();
        }
    };


    //照相

    private View.OnClickListener photoOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_photo:
                    Intent intent = new Intent(CreatFeedView.this, TakePhotoView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("photo", 1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.ll_local:
                    Intent intents = new Intent(CreatFeedView.this, TakePhotoView.class);
                    Bundle bundles = new Bundle();
                    bundles.putInt("photo", 2);
                    intents.putExtras(bundles);
                    startActivityForResult(intents, 1);
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.rl_cancle:
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.rl_bg:
                    rl_bg.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private AdapterView.OnItemClickListener photoItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (photoAdpater.getItem(position).equals("1")) {//无照片
                if (photoLists.size() <= 1) {
                    if (!rl_bg.isShown() || rl_bg.getVisibility() != View.VISIBLE) {
                        rl_bg.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.show(CreatFeedView.this, "最多添加1张", 200);
                }
            } else {
                photoLists.remove(position);
                photoAdpater.removeDataFromAdapter(position);
            }
            photoAdpater.notifyDataSetChanged();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            if (data != null) {
                String path = data.getStringExtra("path");
                if (path.equals("null")) {
                    return;
                }
                photoLists.add(0, path);
                photoAdpater.addDatatoAdapter(0, path);
                photoAdpater.notifyDataSetChanged();
            } else {
                ToastUtil.show(CreatFeedView.this, "系统错误，请重试", 200);
            }
        }
    }

}
