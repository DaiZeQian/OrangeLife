package com.oneorange.content;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oneorange.adapter.CurrentHomeAdpater;
import com.oneorange.adapter.OtherHomeAdapter;
import com.oneorange.base.BaseActivity;
import com.oneorange.orangelife.R;
import com.oneorange.view.NoScrollListView;

import java.util.ArrayList;

/**
 * Created by admin on 2016/6/13.
 */
public class HomeManagerView extends BaseActivity {


    private ImageView ivBack;
    private ImageView ivAdd;
    private NoScrollListView lvHome;
    private NoScrollListView lvOther;

    private void assignViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        lvHome = (NoScrollListView) findViewById(R.id.lv_home);
        lvOther = (NoScrollListView) findViewById(R.id.lv_other);
    }

    //op
    private CurrentHomeAdpater currentHomeAdpater;
    private OtherHomeAdapter otherHomeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        assignViews();
        setOnClickListener();
        setCurrentHome();
        setOtherHome();
    }

    private void setOnClickListener() {
        ivBack.setOnClickListener(onClickListener);
        ivAdd.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.iv_add:
                    openActivity(BindNewHomeView.class);
                    break;
            }
        }
    };

    private void setCurrentHome() {
        ArrayList<String> currents = new ArrayList<>();
        currents.add("资金新干线2号楼2单元101");
        currents.add("资金新干线2号楼2单元103");
        currentHomeAdpater = new CurrentHomeAdpater(this);
        lvHome.setAdapter(currentHomeAdpater);
        currentHomeAdpater.setAdapterDatas(currents);
        currentHomeAdpater.notifyDataSetChanged();
    }

    private void setOtherHome() {
        ArrayList<String> others = new ArrayList<>();
        others.add("资金新干线2号楼2单元102");
        others.add("资金新干线2号楼2单元105");
        otherHomeAdapter = new OtherHomeAdapter(this);
        lvOther.setAdapter(otherHomeAdapter);
        otherHomeAdapter.setAdapterDatas(others);
        otherHomeAdapter.notifyDataSetChanged();
    }

}
