package com.oneorange.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oneorange.adapter.TakePhotoAdpater;
import com.oneorange.adapter.TypeAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.entity.UserChoose;
import com.oneorange.orangelife.R;
import com.oneorange.utils.ToastUtil;
import com.oneorange.view.NHorizontalListView;
import com.oneorange.view.NoScrollGridView;

import java.util.ArrayList;

/**
 * Created by admin on 2016/6/13.
 */
public class BindNewHomeView extends BaseActivity {

    private NoScrollGridView gv_type;
    private NHorizontalListView lv_photo;

    private RelativeLayout rl_bg;
    private LinearLayout ll_photo;
    private LinearLayout ll_local;
    private RelativeLayout rl_cancle;

    //op
    private TypeAdapter typeAdapter;

    private TakePhotoAdpater photoAdpater;

    //data
    private final int RESULT_CODE = 3;
    private final int REQUEST_CODE_PICK_IMAGE = 4;
    private final int REQUEST_CODE_CAPTURE_CAMEIA = 5;
    private ArrayList<UserChoose> userChooses;
    private UserChoose userChoose;
    private ArrayList<String> photoLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_home);
        initView();
        setType();
        setPhoto();
        setOnclickLienter();
    }


    private void initView() {
        gv_type = (NoScrollGridView) findViewById(R.id.gv_type);
        lv_photo = (NHorizontalListView) findViewById(R.id.lv_photo);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        ll_local = (LinearLayout) findViewById(R.id.ll_local);
        rl_cancle = (RelativeLayout) findViewById(R.id.rl_cancle);
    }

    private void setOnclickLienter() {
        gv_type.setOnItemClickListener(onItemClickListener);
        lv_photo.setOnItemClickListener(photoItemListener);

        ll_photo.setOnClickListener(photoOnclickListener);
        ll_local.setOnClickListener(photoOnclickListener);
        rl_cancle.setOnClickListener(photoOnclickListener);
        rl_bg.setOnClickListener(photoOnclickListener);
    }

    private void setType() {
        userChooses = new ArrayList<>();
        userChoose = new UserChoose("房屋", true);
        userChooses.add(userChoose);
        userChoose = new UserChoose("仓库");
        userChooses.add(userChoose);
        userChoose = new UserChoose("车位");
        userChooses.add(userChoose);
        userChoose = new UserChoose("店铺");
        userChooses.add(userChoose);
        typeAdapter = new TypeAdapter(this);
        gv_type.setAdapter(typeAdapter);
        typeAdapter.setAdapterDatas(userChooses);
        typeAdapter.notifyDataSetChanged();

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < userChooses.size(); i++) {
                if (i == position) {
                    typeAdapter.getItem(i).setIsChoose(true);
                } else {
                    typeAdapter.getItem(i).setIsChoose(false);
                }
            }
            typeAdapter.notifyDataSetChanged();
        }

    };


    //照相

    private View.OnClickListener photoOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_photo:
                    Intent intent = new Intent(BindNewHomeView.this, TakePhotoView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("photo", 1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    rl_bg.setVisibility(View.GONE);
                    break;
                case R.id.ll_local:
                    Intent intents = new Intent(BindNewHomeView.this, TakePhotoView.class);
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


    private void setPhoto() {
        photoLists = new ArrayList<>();
        photoLists.add("1");
        photoAdpater = new TakePhotoAdpater(this);
        lv_photo.setAdapter(photoAdpater);
        photoAdpater.setAdapterDatas(photoLists);
        photoAdpater.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener photoItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (photoAdpater.getItem(position).equals("1")) {//无照片
                if (photoLists.size() <= 4) {
                    if (!rl_bg.isShown() || rl_bg.getVisibility() != View.VISIBLE) {
                        rl_bg.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.show(BindNewHomeView.this, "最多添加4张", 200);
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
                photoAdpater.setAdapterDatas(photoLists);
                photoAdpater.notifyDataSetChanged();
            } else {
                ToastUtil.show(BindNewHomeView.this, "系统错误，请重试", 200);
            }
        }
    }

}
