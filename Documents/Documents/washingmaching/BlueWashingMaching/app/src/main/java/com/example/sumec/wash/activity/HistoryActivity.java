package com.example.sumec.wash.activity;


import android.view.View;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class HistoryActivity extends BaseActivity {
    private ToolBarView toolBarView;
    private TextView tvLastTime,tvAllTimes,tvAllCharges;

    @Override
    public int getContentView() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
        tvLastTime = (TextView) findViewById(R.id.tvLastTime);
        tvAllTimes = (TextView) findViewById(R.id.tvAllTimes);
        tvAllCharges = (TextView) findViewById(R.id.tvCharges);
    }

    @Override
    public void setWindowFeature() {

    }

    private void setToolbarView() {
        toolBarView.setTvDoneVisibility(View.GONE);
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTitleText("历史");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
