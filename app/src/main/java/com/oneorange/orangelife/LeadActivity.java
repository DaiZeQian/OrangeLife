package com.oneorange.orangelife;

import android.os.Bundle;

import com.oneorange.base.BaseActivity;
import com.oneorange.content.LoginView;


public class LeadActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        openActivityAndCloseThis(LoginView.class);
    }
}
