package com.example.sumec.wash.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.utils.ScreenUtil;
import com.example.sumec.wash.utils.StatusBarUtil;
import com.example.sumec.wash.video.VideoBusiness;
import com.example.sumec.wash.video.VideoController;


public class VideoActivity extends BaseActivity {
    private String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" ;
    private RelativeLayout rl_video_container;
    private VideoView videoview;
    private VideoController id_video_controller;
    private VideoBusiness mVideoBusiness;
    private String mVideoUrl = "";
    private boolean isFullScreen;

    @Override
    public int getContentView() {
        return R.layout.activity_video;
    }

    @Override
    public void initView() {
        rl_video_container = (RelativeLayout) findViewById(R.id.rl_video_container);
        videoview = (VideoView) findViewById(R.id.id_videoview);
        id_video_controller = (VideoController) findViewById(R.id.id_video_controller);
        setVideoInfo();
    }

    private void setVideoInfo() {
        Uri uri= Uri.parse(videoUrl);
        mVideoBusiness = new VideoBusiness(this);
        //  mVideoBusiness.initVideo(videoview,id_video_controller,mVideoUrl);
        mVideoBusiness.initVideo(videoview,id_video_controller,uri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int screenWidth = ScreenUtil.getScreenWidth(this);
        setVideoContainerParam(screenWidth,screenWidth * 9 / 16);
    }

    private void setVideoContainerParam(int w,int h) {
        ViewGroup.LayoutParams params = rl_video_container.getLayoutParams();
        params.width = w;
        params.height = h;
        rl_video_container.setLayoutParams(params);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //全屏看视频
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
            setVideoContainerParam(matchParent,matchParent);
            if(StatusBarUtil.hasNavBar(this)){
                StatusBarUtil.hideBottomUIMenu(this);
            }
            isFullScreen = true;
            StatusBarUtil.fullscreen(true,this);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { //从视频全屏界面恢复竖屏

            if(StatusBarUtil.hasNavBar(this)){
                StatusBarUtil.showBottomUiMenu(this);
            }
            StatusBarUtil.fullscreen(false,this);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int screenWidth = ScreenUtil.getScreenWidth(this);
            setVideoContainerParam(screenWidth,screenWidth * 9 / 16);
            isFullScreen = false;
        }
    }

    @Override
    public void setWindowFeature() {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (isFullScreen) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                ImageView imageView = (ImageView) rl_video_container.findViewById(R.id.id_iv_video_expand);
//                imageView.setImageResource(R.drawable.zuidahua_2x);
//                return true;
//            } else {
                finish();
//            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
