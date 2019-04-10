package com.example.sumec.wash.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.view.ToolBarView;

public class VideoListActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout videoLl1, videoLl2, videoLl3, videoLl4;
    private ToolBarView toolBarView;

    @Override
    public int getContentView() {
        return R.layout.activity_video_list;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        videoLl1 = findViewById(R.id.videoLl1);
        videoLl1.setOnClickListener(this);
        videoLl2 = findViewById(R.id.videoLl2);
        videoLl2.setOnClickListener(this);
        videoLl3 = findViewById(R.id.videoLl3);
        videoLl3.setOnClickListener(this);
        videoLl4 = findViewById(R.id.videoLl4);
        videoLl4.setOnClickListener(this);
        setToolBarView();
    }

    private void setToolBarView() {
        toolBarView.setTitleText("视频列表");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.videoLl1:
                startActivityParam(this, VideoActivity.class, "videoUrl", "https://cloud.video.taobao.com//play/u/4106529795/p/1/e/6/t/1/222385028360.mp4");
                break;
            case R.id.videoLl2:
                startActivityParam(this, VideoActivity.class, "videoUrl", "https://cloud.video.taobao.com//play/u/4106529795/p/1/e/6/t/1/222574139323.mp4");
                break;
            case R.id.videoLl3:
                startActivityParam(this, VideoActivity.class, "videoUrl", "https://cloud.video.taobao.com//play/u/4106529795/p/1/e/6/t/1/221733345005.mp4");
                break;
            case R.id.videoLl4:
                startActivityParam(this, VideoActivity.class, "videoUrl", "https://cloud.video.taobao.com//play/u/4106529795/p/1/e/6/t/1/222384936611.mp4");
                break;
        }
    }
}
