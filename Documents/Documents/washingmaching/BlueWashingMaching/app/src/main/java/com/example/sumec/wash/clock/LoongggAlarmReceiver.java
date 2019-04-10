package com.example.sumec.wash.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class LoongggAlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    int flag = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (flag == 0) {
            flag = 1;
            // TODO Auto-generated method stub
            String msg = intent.getStringExtra("msg");
            long intervalMillis = intent.getLongExtra("intervalMillis", 0);
//        if (intervalMillis != 0) {
//            AlarmManagerUtil.setAlarmTime(context, System.currentTimeMillis() + intervalMillis,
//                    intent);
//        }
            if (intervalMillis != 0) {
                AlarmManagerUtil.setAlarmTime(context, System.currentTimeMillis() + intervalMillis,
                        intent);
            }
            int flag = intent.getIntExtra("soundOrVibrator", 0);
            Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
            clockIntent.putExtra("msg", msg);
            clockIntent.putExtra("flag", flag);
            clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(clockIntent);
        }
    }


}
