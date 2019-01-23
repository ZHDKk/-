package com.example.sumec.wash.activity;

import android.view.View;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class GameActivity extends BaseActivity {
private ToolBarView toolBarView;

    @Override
    public int getContentView() {
        return R.layout.activity_game;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
    }

    private void setToolbarView() {
        toolBarView.setTitleText("小游戏");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTvDoneVisibility(View.GONE);
    }

    @Override
    public void setWindowFeature() {

    }
}
