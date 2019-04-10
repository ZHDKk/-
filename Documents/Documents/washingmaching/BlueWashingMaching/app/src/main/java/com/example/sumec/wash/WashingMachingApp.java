package com.example.sumec.wash;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;
//import com.umeng.commonsdk.UMConfigure;

/**
 * Created by zhdk on 2019/1/16.
 */

public class WashingMachingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //mob分享初始化
        MobSDK.init(this);
//        UMConfigure.setLogEnabled(true);
    }
}
