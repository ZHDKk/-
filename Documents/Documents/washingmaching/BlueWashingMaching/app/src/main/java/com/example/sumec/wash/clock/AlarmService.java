package com.example.sumec.wash.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.sumec.wash.utils.UiUtils;

import java.util.Calendar;
import java.util.TimeZone;

import static com.example.sumec.wash.clock.AlarmManagerUtil.ALARM_ACTION;

public class AlarmService extends Service {
    int flag;
    int month;
    int day;
    int hour;
    int minute;
    int id;
    int intervalMillis;
    String tips;
    int soundOrVibrator;
    private LoongggAlarmReceiver loongggAlarmReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         loongggAlarmReceiver = new LoongggAlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.sumec.wash.clock");
        registerReceiver(loongggAlarmReceiver, intentFilter);

        flag = intent.getIntExtra("flag", flag);
        month = intent.getIntExtra("month", month);
        day = intent.getIntExtra("day", day);
        hour = intent.getIntExtra("hour", hour);
        minute = intent.getIntExtra("minute", minute);
        intervalMillis = intent.getIntExtra("intervalMillis", 0);
        id = intent.getIntExtra("id", id);
        tips = intent.getStringExtra("tips");
        soundOrVibrator = intent.getIntExtra("soundOrVibrator", soundOrVibrator);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(calendar.get(Calendar.YEAR), month-1, day, hour, minute, 0);
        Intent intent1 = new Intent("com.example.sumec.wash.clock");
        intent1.putExtra("intervalMillis", intervalMillis);
        intent1.putExtra("msg", tips);
        intent1.putExtra("id", id);
        intent1.putExtra("soundOrVibrator", soundOrVibrator);
        PendingIntent sender = PendingIntent.getBroadcast(this, id, intent1, PendingIntent
                .FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                String date = UiUtils.stampToDate(calendar.getTimeInMillis());
                Log.d("dasa",date);
                am.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        0, sender);
                Toast.makeText(this, "预约成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "预约失败，时间设置不正确", Toast.LENGTH_LONG).show();
            }
        } else {
            if (flag == 0) {
                if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                } else {
                    Toast.makeText(this, "预约失败，时间设置不正确", Toast.LENGTH_LONG).show();
                }
            } else {
//                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - System.currentTimeMillis(), intervalMillis, sender);
            }
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);

    }

}
