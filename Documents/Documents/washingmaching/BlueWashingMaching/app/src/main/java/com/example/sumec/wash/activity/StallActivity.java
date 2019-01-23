package com.example.sumec.wash.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class StallActivity extends BaseActivity {
    private ToolBarView toolBarView;
    private LinearLayout softLl,generalLl,efficientLl,strongLl;
    private ImageView softImage,generalImage,efficentImage,strongImage;
    private TextView tvSoft,tvGeneral,tvEfficent,tvStrong;

    @Override
    public int getContentView() {
        return R.layout.activity_stall;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        softLl = (LinearLayout) findViewById(R.id.softLl);
        generalLl = (LinearLayout) findViewById(R.id.generalLl);
        efficientLl = (LinearLayout) findViewById(R.id.efficientLl);
        strongLl = (LinearLayout) findViewById(R.id.strongLl);
        softImage = (ImageView) findViewById(R.id.softImage);
        generalImage = (ImageView) findViewById(R.id.generalImage);
        efficentImage = (ImageView) findViewById(R.id.efficientImage);
        strongImage = (ImageView) findViewById(R.id.strongImage);
        tvSoft = (TextView) findViewById(R.id.softTv);
        tvGeneral = (TextView) findViewById(R.id.generalTv);
        tvEfficent = (TextView) findViewById(R.id.efficientTv);
        tvStrong = (TextView) findViewById(R.id.strongTv);
        setToolBarView();

    }

    private void setToolBarView() {
        toolBarView.setTitleText("佳孚无线自带水桶高压清洗机");
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setTvDoneVisibility(View.GONE);
    }

    @Override
    public void setWindowFeature() {

    }
}
