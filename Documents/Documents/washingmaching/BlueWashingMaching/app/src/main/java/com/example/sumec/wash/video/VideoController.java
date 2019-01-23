package com.example.sumec.wash.video;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.sumec.wash.R;
import com.example.sumec.wash.utils.DensityUtil;
import com.example.sumec.wash.utils.ScreenUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoController extends RelativeLayout implements View.OnTouchListener,View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    private Context mContext;
    private View mContainer;
    private VideoBusiness videoBusiness;
    /** 表示当前视频控制面板是否展示*/
    public boolean isShow = true;
    private Handler hideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isShow) {
                hideController();
            }
        }
    };
    /** 视频功能控制底边栏*/
    private LinearLayout mMediaController;

    /***************手势相关*********************/
    private int GESTURE_FLAG = 0;//1调节进度,2调节音量,3调节亮度
    private FrameLayout mProgressIndicator;
    private ProgressBar progressBar;
    /**进度相关*/
    private GestureDetector progressGestureDetector;
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    /**音量相关*/
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private AudioManager audiomanager;
    private int maxVolume, currentVolume;
    /**亮度相关*/
    private static final int GESTURE_MODIFY_BRIGHTNESS = 3;
    private WindowManager.LayoutParams brightnessLp;
    private int maxBrightness,currentBrightness;
    private LinearLayout progressArea;
    private int targetTime;
    //UI相关
    private RelativeLayout id_rl_video_controller;
    private FrameLayout id_fl_video_play;
    private FrameLayout id_fl_video_expand;
    private TextView id_video_time;
    private TextView id_video_totaltime;
    private SeekBar id_sb_progress;
    private ImageView id_btn_video_play;
    private ImageView id_iv_video_play;
    private TextView id_tv_video_info;

    private LinearLayout mMediaBackControl;
    private ImageView id_iv_video_back;
    private FrameLayout id_fl_video_back;


    public VideoController(Context context) {
        this(context, null);
    }

    public VideoController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
        initListener();
    }

    private void init() {
        //初始化音量相关
        audiomanager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        //初始化亮度相关
        brightnessLp=((Activity)mContext).getWindow().getAttributes();
        currentBrightness = getCurrentBrightness();
        maxBrightness = 255; //设置最大亮度
        initView();
    }

    private void initView() {
        mContainer = View.inflate(mContext, R.layout.video_controller, null);
        id_rl_video_controller = findView(R.id.id_rl_video_controller);
        mMediaController = findView(R.id.id_ll_controller);
        mProgressIndicator = findView(R.id.id_fl_progress_indicator);
        progressBar = findView(R.id.id_pb_gesture_progress);
        progressArea = findView(R.id.id_ll_video_gesture_progress);
        id_fl_video_play = findView(R.id.id_fl_video_play);
        id_fl_video_expand = findView(R.id.id_fl_video_expand);
        id_video_time = findView(R.id.id_video_time);
        id_video_totaltime = findView(R.id.id_video_totaltime);
        id_sb_progress = findView(R.id.id_sb_progress);
        id_btn_video_play = findView(R.id.id_btn_video_play);
        id_iv_video_play = findView(R.id.id_iv_video_play);
        id_tv_video_info = findView(R.id.id_tv_video_info);
        mMediaBackControl = findView(R.id.id_ll_back);
        id_iv_video_back = findView(R.id.id_iv_video_back);
        id_fl_video_back = findView(R.id.id_fl_video_back);
        addView(mContainer);
    }

    private  <T extends View> T findView(int viewId) {
        return (T) mContainer.findViewById(viewId);
    }

    public void setVideoBusiness(VideoBusiness videoBusiness) {
        this.videoBusiness = videoBusiness;
    }

    private void initListener() {
        //进度手势相关
        progressGestureDetector = new GestureDetector(mContext,new ProgressGestureListenr());
        progressGestureDetector.setIsLongpressEnabled(true);
        progressArea.setLongClickable(true);
        progressArea.setOnTouchListener(this);
        mMediaController.setOnClickListener(this);
        mMediaBackControl.setOnClickListener(this);
        id_iv_video_back.setOnClickListener(this);
        id_rl_video_controller.setOnClickListener(this);
        id_fl_video_play.setOnClickListener(this);
        id_fl_video_expand.setOnClickListener(this);
        id_btn_video_play.setOnClickListener(this);
        id_sb_progress.setOnSeekBarChangeListener(this);
    }

    //状态切换
    public void toggle() {
        if (isShow) {
            hideController();
        } else {
            showController();
        }
    }

    //隐藏底部控制栏
    public void hideController() {
        isShow = false;
        mMediaController.setVisibility(View.GONE);
        mMediaBackControl.setVisibility(GONE);
        endTimer();
    }

    //显示底部控制栏
    public void showController() {
        isShow = true;
        mMediaController.setVisibility(View.VISIBLE);
        mMediaBackControl.setVisibility(VISIBLE);
        startTimer();
    }

    private void startTimer() {
        if (hideHandler != null){
            hideHandler.removeMessages(0);
        }
        hideHandler.sendEmptyMessageDelayed(0, 5000);
    }

    private void endTimer() {
        hideHandler.removeMessages(0);
    }

    public void resetTimer() {
        endTimer();
        startTimer();
    }

    public void showLong() {
        isShow = true;
        mMediaController.setVisibility(View.VISIBLE);
        mMediaBackControl.setVisibility(VISIBLE);
    }

    //设置视频总时间
    public void setTotalTime(int time) {
        String totalTime = getTimeString(time);
        id_video_totaltime.setText(totalTime);
    }

    //设置视频当前进度
    public void setProgress(int progress){
        int maxProgress = videoBusiness.getTotalTime();
        int tempProgress = progress > maxProgress ? maxProgress : progress;
        id_sb_progress.setProgress(tempProgress);
    }

    //是指视频总进度
    public void setMaxProgress(int maxProgress){
        id_sb_progress.setMax(maxProgress);
    }

    public void showPauseBtn(){
        if (id_btn_video_play.getVisibility()==GONE ) {
            id_btn_video_play.setVisibility(VISIBLE);
            id_iv_video_play.setImageResource(R.drawable.video_pause);
        }
    }

    //获取当前亮度
    private int getCurrentBrightness(){
        int currentBrightness = 255;
        if (brightnessLp.screenBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE){
            // 获取系统亮度
            try {
                currentBrightness = Settings.System.getInt(((Activity)mContext).getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            // 获取当前窗口亮度
            currentBrightness = (int)(brightnessLp.screenBrightness * 255);
        }
        return  currentBrightness;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_ll_controller: //底部控制器
                showController();
                break;
            case R.id.id_rl_video_controller:  //全屏点击
                toggle();
                break;
            case R.id.id_fl_video_play:  // 暂停/播放
            case R.id.id_btn_video_play:  // 暂停/播放
                videoBusiness.playVideo(id_btn_video_play,id_iv_video_play);
                break;
            case R.id.id_fl_video_expand: //全屏
                resetTimer();
                videoBusiness.toggleScreenDir(view);
                break;
            case R.id.id_fl_video_back:
                videoBusiness.finish();
                break;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        getParent().requestDisallowInterceptTouchEvent(true);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mProgressIndicator.setVisibility(View.INVISIBLE);
            if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {  //调节进度
                Log.e("进度时间","targetTime="+targetTime);
                videoBusiness.seekToPlay(targetTime);
                videoBusiness.isSeekBarEnable = true;
                hideController();
            }
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
        }
        return progressGestureDetector.onTouchEvent(event);
    }

    private int currentPosition;  //当前播放进度
    private int totalPosition;   //总播放进度
    class ProgressGestureListenr implements GestureDetector.OnGestureListener{
        private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
        private int slop;// 触发设置变动的最小距离
        @Override
        public boolean onDown(MotionEvent e) {
            //初始数据
            slop = DensityUtil.dp2px(mContext,2);
            currentPosition = videoBusiness.getCurrentTime();
            totalPosition = videoBusiness.getTotalTime();
            firstScroll = true;
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {  //一次点击up事件
            toggle();
            if(videoBusiness.isPlaying()){  //正在播放
                return false;
            }else{  //暂停或者开始播放
                videoBusiness.playVideo(id_btn_video_play,id_iv_video_play);
                isShow = false;
                return true;
            }
        }

        //在屏幕上拖动事件
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                // 横向的距离变化大则调整进度，纵向的变化大则调整音量
                Log.e("xxxxxxx",e1.getX()+"");
                Log.e("yyyyyyy",e1.getY()+"");
                setScroll(e1, distanceX, distanceY);
            }
            // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
            switch (GESTURE_FLAG){
                case GESTURE_MODIFY_PROGRESS:   //调节当前进度
                    setCurrentProgress(distanceX, distanceY,slop);
                    break;
                case GESTURE_MODIFY_VOLUME:    //调节当前音量
                    setCurrentVolume(distanceX, distanceY,slop);
                    break;
                case GESTURE_MODIFY_BRIGHTNESS: //调节当前亮度
                    setCurrentBrightess(distanceX, distanceY,slop);
                    break;
            }
            firstScroll=false;
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {  //长按事件
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { //滑动手势事件
            return false;
        }
    }

    //滑动事件
    private void setScroll(MotionEvent e1, float distanceX, float distanceY) {
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        Log.e("屏宽",screenWidth+"");
        //横向的距离变化大则调整进度，纵向的变化大则调整音量
        mProgressIndicator.setVisibility(View.VISIBLE);
        if (Math.abs(distanceX) >= Math.abs(distanceY)) {  //调整进度
            progressBar.setVisibility(View.GONE);
            id_tv_video_info.setVisibility(VISIBLE);
            GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
            videoBusiness.isSeekBarEnable = false;
            endTimer();
            showLong();
        }else {  //调整音量
            progressBar.setVisibility(VISIBLE);
            id_tv_video_info.setVisibility(GONE);
            if (e1.getX() > screenWidth / 2){  //屏幕右边上下滑动调整音量
                //右半屏音量
                setVideoVolume();
            }else{                             //屏幕左边上下滑动调整亮度
                //左半屏亮度
                setVideoBrightness();
            }
        }
    }

    //设置当前进度
    private void setCurrentProgress(float distanceX, float distanceY,float slop) {
        if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
            //Log.e("setCurrentProgress",distanceX+"");
            if(distanceX >= slop){  //从右往左滑 快退
                mProgressIndicator.setBackgroundResource(R.drawable.kuaitui);
                if (currentPosition > 1000) {
                    currentPosition -= 1500;
                }
            }else if(distanceX <= -slop){  //从左往右滑 快进
                mProgressIndicator.setBackgroundResource(R.drawable.kuaijin);
                if (currentPosition < totalPosition) {
                    currentPosition += 1500;
                }
            }
        }
        targetTime = currentPosition;
        Log.e("进度时间","currentPosition="+currentPosition);
        id_sb_progress.setProgress(currentPosition);
        id_video_time.setText(getTimeString(currentPosition));
        String videoPbInfo = getTimeString(currentPosition)+"/"+ getTimeString(totalPosition);
        id_tv_video_info.setText(videoPbInfo);
    }

    //设置当前亮度
    private void setCurrentBrightess(float distanceX, float distanceY, float slop) {
        currentBrightness = getCurrentBrightness();
        if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
            if (distanceY >= slop) {// 上滑 亮度调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                if (currentBrightness < maxBrightness) {// 为避免调节过快，distanceY应大于一个设定值
                    currentBrightness += 8;
                }
            } else if (distanceY <= -slop) {// 亮度调小
                if (currentBrightness > 0) {
                    currentBrightness -= 8;
                }
                if (currentBrightness<0){
                    currentBrightness=0;
                }
            }
            mProgressIndicator.setBackgroundResource(R.drawable.liangdu);
            progressBar.setProgress(currentBrightness);
            changeAppBrightness(mContext,currentBrightness);
        }
    }

    //设置当前音量
    private void setCurrentVolume(float distanceX, float distanceY,float slop) {

        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        if (Math.abs(distanceY) > Math.abs(distanceX)) {  // 纵向移动大于横向移动
            if (distanceY >= slop) { // 上滑 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                    currentVolume++;
                }
                mProgressIndicator.setBackgroundResource(R.drawable.yinliang);
            } else if (distanceY <= -slop) {// 音量调小 下滑
                if (currentVolume > 0) {
                    currentVolume--;
                    if (currentVolume == 0) {// 静音，设定静音独有的图片
                        mProgressIndicator.setBackgroundResource(R.drawable.jingying);
                    }
                }
            }
            progressBar.setProgress(currentVolume);
            audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
    }

    //设置视频亮度
    private void setVideoBrightness() {
        progressBar.setMax(maxBrightness);
        progressBar.setProgress(currentBrightness);
        mProgressIndicator.setBackgroundResource(R.drawable.liangdu);
        GESTURE_FLAG = GESTURE_MODIFY_BRIGHTNESS;
    }

    //设置视频音量
    private void setVideoVolume() {
        progressBar.setMax(maxVolume);
        progressBar.setProgress(currentVolume);
        mProgressIndicator.setBackgroundResource(R.drawable.yinliang);
        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
    }

    //改变系统亮度
    public void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    public String getTimeString(int second) {
        int temp = second / 1000;
        int hh = temp / 3600;
        SimpleDateFormat sdf;
        if (0 != hh) {
            sdf = new SimpleDateFormat("HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("mm:ss");
        }
        String format = sdf.format(new Date(second));
        return  format;
    }

    //进度条改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String timeString = getTimeString(progress);
        id_video_time.setText(timeString);
    }

    //开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        showLong();
        videoBusiness.removeUIMessage();
    }

    //结束拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        showController();
        int progress = seekBar.getProgress();
        videoBusiness.seekToPlay(progress);
    }

}
