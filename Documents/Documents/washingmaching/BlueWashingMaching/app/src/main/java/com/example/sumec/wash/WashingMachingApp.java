package com.example.sumec.wash;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;

/**
 * Created by zhdk on 2019/1/16.
 */

public class WashingMachingApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //讯飞语音控制初始化
        SpeechUtility.createUtility(this,SpeechConstant.APPID + "=5c3eabb8");
        //mob分享初始化
        MobSDK.init(this);
    }
}
