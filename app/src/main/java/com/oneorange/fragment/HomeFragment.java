package com.oneorange.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneorange.base.BaseFragment;
import com.oneorange.orangelife.R;

/**
 * Created by admin on 2016/5/17.
 */
public class HomeFragment extends BaseFragment {
    @Override
    public View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void laodData() {

    }

    @Override
    public void resetData() {

    }
}
