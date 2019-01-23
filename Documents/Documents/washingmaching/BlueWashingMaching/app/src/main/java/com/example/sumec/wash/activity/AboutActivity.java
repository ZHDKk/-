package com.example.sumec.wash.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private ToolBarView toolBarView;
    private LinearLayout policyLl;

    @Override
    public int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
        policyLl = (LinearLayout) findViewById(R.id.policyLl);
        policyLl.setOnClickListener(this);
    }

    private void setToolbarView() {
        toolBarView.setTvDoneVisibility(View.GONE);
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTitleText("关于");
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
        switch (view.getId())
        {
            case R.id.policyLl:
                break;
        }
    }
}
