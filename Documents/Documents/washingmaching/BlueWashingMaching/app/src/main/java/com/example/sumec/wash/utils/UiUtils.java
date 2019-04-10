package com.example.sumec.wash.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.mrengineer13.snackbar.SnackBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 提供ui操作的帮助类
 * 
 * @author Fussen
 * 
 */
public class UiUtils {

	// 返回context对象
	public static Context getContext() {
		return MyApplication.getContext();
	}

	//软键盘弹出
	public static void showKeyboard(Context context, EditText editText){
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(editText, 0);
	}

	/**
	 * 关闭软键盘
	 * @param context
	 */
	public static void closeKeyboard(Activity context) {
		View view = context.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/*
	强制关闭软键盘
	 */
	public static void closeKeyboardForce(Activity context) {
		View view = context.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	//底部弹出提示框
	public static void showSackBar(Context context,String text,int colorbgId,int textColorId){
		SnackBar.Builder builder = new SnackBar.Builder((Activity) context);
		builder.withMessage(text);
		builder.withBackgroundColorId(colorbgId);
		builder.withTextColorId(textColorId);
		builder.show();
	}

	public static void backgroundAlpha(float bgAlpha,Activity context)
	{
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		context.getWindow().setAttributes(lp);
	}

	//隐藏底部虚拟键盘
	public static void hideBottomUIMenu(Activity activity){
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
			View v = activity.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			//for new api versions.
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
					| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
					| View.SYSTEM_UI_FLAG_IMMERSIVE;
			decorView.setSystemUiVisibility(uiOptions);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	public static  void startActivity(Context context,Class clazz){
		context.startActivity(new Intent(context,clazz));
	}
	public static void startActivityParam(Context context,Class clazz,String key,String param){
		Intent intent = new Intent(context,clazz);
		Bundle bundle = new Bundle();
		bundle.putString(key,param);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	public static void startActivityParam(Context context,Class clazz,String titleKey,String titleParam,String contentKey,String contentParam){
		Intent intent = new Intent(context,clazz);
		Bundle bundle = new Bundle();
		bundle.putString(titleKey,titleParam);
		bundle.putString(contentKey,contentParam);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}


	//日期转换星期
	public static String[] WEEK = {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
	public static final int WEEKDAYS = 7;

	public static String DateToWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {
			return null;
		}

		return WEEK[dayIndex - 1];
	}


	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String time) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(time);
		long ts = date.getTime();
		return String.valueOf(ts);
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(long timeMillis) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(timeMillis);
		return simpleDateFormat.format(date);

	}

}
