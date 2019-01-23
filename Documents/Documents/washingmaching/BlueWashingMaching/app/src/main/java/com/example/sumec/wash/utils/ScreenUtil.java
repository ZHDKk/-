package com.example.sumec.wash.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author : zlc
 * @date : On 2017/2/28
 * @eamil : zlc921022@163.com
 */

public class ScreenUtil {

    //获取屏幕宽度
    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.widthPixels;
    }

    //获取屏幕高度
    public static int getScreenHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.heightPixels;
    }
}
