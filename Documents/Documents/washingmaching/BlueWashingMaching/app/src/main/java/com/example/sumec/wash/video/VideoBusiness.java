/********************************************************
 * 视频业务类
 * 最后修改时间：2017/9/24
 *
 *********************************************************/
package com.example.sumec.wash.video;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.sumec.wash.R;


public class VideoBusiness implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{

    private Activity activity;
    private WakeLock mWakeLock;
    public VideoView mVideoView;
    private VideoController mController;

    /**播放状态枚举，有三种播放状态：空闲，正在准备*/
    private enum PLAYER_STATUS {
        IDLE, PREPARING,PAUSED, PREPARED,RESUMED,STOPPED
    }
    /**当前播放状态*/
    public  PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.IDLE;

    /**播放信息异步处理方法,用于更新进度*/
    /**事件标志*/
    private int mLastPos;

    public VideoBusiness(Activity activity){
        this.activity = activity;
        // 保持屏幕高亮
        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "Test");
    }

    public void finish(){
        this.activity.finish();
    }

    //初始化视频播放器 传视频地址
    public void initVideo(VideoView videoView, VideoController controller, String sourceUrl){

        this.mVideoView = videoView;
        this.mController = controller;
        mController.setVideoBusiness(this);
        Log.e("msg","设置播放地址 = "+sourceUrl);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setVideoPath(sourceUrl); //设置播放地址
    }

    //初始化视频播放器 传uri
    public void initVideo(VideoView videoView, VideoController controller, Uri uri){

        this.mVideoView = videoView;
        this.mController = controller;
        mController.setVideoBusiness(this);
        Log.e("msg","设置uri = "+uri.getPath());
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setVideoURI(uri); //设置uri
    }

    //开始播放
    public void startPlay(){
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
        if(null != mVideoView) {
            Log.e("msg", "播放");
            mVideoView.start();
            mPlayerStatus = PLAYER_STATUS.PREPARING;
            sendUIMessage();
        }
    }


    /**
     * 暂停播放
     */
    public void pause() {
        if (null != mWakeLock) {
            mWakeLock.release();
        }
        if(null != mVideoView && mVideoView.isPlaying()){
            removeUIMessage();
            mVideoView.pause();
            mPlayerStatus = PLAYER_STATUS.PAUSED;
            mLastPos = getCurrentTime();
        }
    }

    /**
     * 继续播放
     */
    public void resume(){
        if (null != mWakeLock) {
            mWakeLock.acquire();
        }
        if(null != mVideoView){
            //mVideoView.resume();
//            int currentTime = getCurrentTime();
//            String timeString = mController.getTimeString(currentTime);
//            Log.e("resume",timeString);
            mVideoView.seekTo(mLastPos);
            mVideoView.start();
            sendUIMessage();
            mPlayerStatus = PLAYER_STATUS.RESUMED;
        }
    }

    /**
     * 停止播放
     */
    public void stop() {

        if (null != mWakeLock) {
            mWakeLock.release();
        }
        if(null != mVideoView){
            mLastPos = getCurrentTime();
            mVideoView.stopPlayback();
            mPlayerStatus = PLAYER_STATUS.STOPPED;
        }
    }

    /**
     * 释放正在播放视频
     * @return
     */
    public boolean isPlaying(){
        return mVideoView!=null && mVideoView.isPlaying();
    }

    /**
     * 是否暂停
     */
    public boolean isPause(){
        return mPlayerStatus == PLAYER_STATUS.PAUSED;
    }

    /**
     * 释放资源
     */
    public void release(){
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
        if(null != mVideoView){
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("onCompletion","视频播放完了");
        mController.showLong();
        mController.setProgress(0);
        mLastPos = 0;
        mPlayerStatus = PLAYER_STATUS.IDLE;
        isSeekBarEnable = true;
        mController.showPauseBtn();
        mPlayerStatus = PLAYER_STATUS.PAUSED;
        removeUIMessage();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("onError","视频播放报错了");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e("onPrepared","视频准备好了");
        if (mPlayerStatus!= PLAYER_STATUS.PAUSED){
            int totalTime = getTotalTime();
            mController.setTotalTime(totalTime);
            mController.setProgress(0);
            mController.setMaxProgress(totalTime);
            mPlayerStatus = PLAYER_STATUS.PREPARED;
            sendUIMessage();
        }
    }

    /**
     * 进度条拖拽播放
     * @param time
     */
    public void seekToPlay(int time){
        int totalSecond = getTotalTime();
        int tempTime = time > totalSecond ? totalSecond : time;
        mVideoView.seekTo(tempTime);
        sendUIMessage();
    }


    //视频暂停播放 播放大按钮点击事件
    public void playVideo(ImageView id_btn_video_play, ImageView img){
        if(isPlaying()){
            pause();
            id_btn_video_play.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.video_pause);
            mPlayerStatus = PLAYER_STATUS.PAUSED;
            //mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
        }else if(isPause()){
            //mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 500);
            resume();
            id_btn_video_play.setVisibility(View.GONE);
            img.setImageResource(R.drawable.video_play);
            mPlayerStatus = PLAYER_STATUS.RESUMED;
        }else{
            img.setImageResource(R.drawable.video_play);
            id_btn_video_play.setVisibility(View.GONE);
            startPlay();
        }
    }

    private boolean isCurrentLandscape = false;  //是不是横屏
    //横竖屏切换按钮点击方法
    public void toggleScreenDir(View v){
        if (isCurrentLandscape) {// 如果当前是横屏，则切换为竖屏,然后把按钮为变为变大的图标
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if(v instanceof ImageView){
                ((ImageView)v).setImageResource(R.drawable.zuidahua_2x);
            }
        } else {// 如果当前是竖屏，则切换为横屏,然后把按钮为变为变小的图标
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if(v instanceof ImageView){
                ((ImageView)v).setImageResource(R.drawable.xiaohua_2x);
            }
        }
        isCurrentLandscape = !isCurrentLandscape;
    }

    public UIHandler mUIHandler = new UIHandler(Looper.getMainLooper());
    public final int UI_EVENT_UPDATE_CURRPOSITION = 1;  //更新进度信息
    public boolean isSeekBarEnable = true;

    class UIHandler extends Handler {
        public UIHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //更新进度及时间
                case UI_EVENT_UPDATE_CURRPOSITION:
                    if (isSeekBarEnable) {
                        int currentPosition = getCurrentTime();
                        //int currentPosition = mController.getProgress();
                        if(isPlaying()) {
                            mController.setProgress(currentPosition);
                            mUIHandler.sendEmptyMessageDelayed(
                                    UI_EVENT_UPDATE_CURRPOSITION, 1000);
                        }
                    }
                    break;
            }
        }
    }

    public void sendUIMessage(){
        removeUIMessage();
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    public void removeUIMessage(){
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
    }

    //获取视频总时间
    public int getTotalTime(){
        return mVideoView==null ? 0 : mVideoView.getDuration();
    }

    //获取视频当前时间
    public int getCurrentTime(){
        return mVideoView==null ? 0 : mVideoView.getCurrentPosition();
    }

}
