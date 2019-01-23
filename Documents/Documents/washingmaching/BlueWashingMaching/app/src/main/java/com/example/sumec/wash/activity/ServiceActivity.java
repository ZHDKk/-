package com.example.sumec.wash.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class ServiceActivity extends BaseActivity implements View.OnClickListener {
    private ToolBarView toolBarView;
    private LinearLayout instructionsLl,serviceLl,videoLl;


    @Override
    public int getContentView() {
        return R.layout.activity_service;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
        instructionsLl = (LinearLayout) findViewById(R.id.instructionsLl);
        serviceLl = (LinearLayout) findViewById(R.id.serviceLl);
        videoLl = (LinearLayout) findViewById(R.id.videoLl);
        instructionsLl.setOnClickListener(this);
        serviceLl.setOnClickListener(this);
        videoLl.setOnClickListener(this);
    }

    private void setToolbarView() {
        toolBarView.setTvDoneVisibility(View.GONE);
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTitleText("产品说明书");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.instructionsLl:
                break;
            case R.id.serviceLl:
                break;
            case R.id.videoLl:
                break;
        }
    }
}
