package com.example.sumec.wash.activity;

import android.content.IntentFilter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.clock.AlarmManagerUtil;
import com.example.sumec.wash.clock.LoongggAlarmReceiver;
import com.example.sumec.wash.utils.ACacheUtil;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.ToolBarView;
import com.example.sumec.wash.view.alarmView.SelectRemindCyclePopup;
import com.example.sumec.wash.view.alarmView.SelectRemindWayPopup;

import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class AlarmActivity extends BaseActivity implements View.OnClickListener {
    private TextView date_tv, ring_tv;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl, ring_rl;
    private LinearLayout allLayout;
    private Button set_btn;
    private String time;
    private int cycle;
    private int ring;
    private ToolBarView toolBarView;
    private String textDate;
    private String[] times;
    private String[] days;
    private String[] months;
    private String lastTextDate;
    private CardView reservationBtn, wayBtn;
    private LoongggAlarmReceiver loongggAlarmReceiver;

    @Override
    public int getContentView() {
        return R.layout.activity_alarm;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolBarView();
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        reservationBtn = findViewById(R.id.reservationBtn);
        reservationBtn.setOnClickListener(this);
        date_tv = (TextView) findViewById(R.id.date_tv);
        ring_tv = findViewById(R.id.ring_tv);
        wayBtn = findViewById(R.id.wayBtn);
        wayBtn.setOnClickListener(this);
        pvTime = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                if (time != null && time.length() > 0) {
                    times = time.split(" ");
                    months = times[0].split("-");
                    days = times[1].split(":");
                    textDate = months[0] + "月" + months[1] + "日" + " " + days[0] + ":" + days[1];
                    ACacheUtil.get(AlarmActivity.this).put("reservationDate", textDate);
                    date_tv.setText(textDate);
                }
            }
        });

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

        lastTextDate = ACacheUtil.get(AlarmActivity.this).getAsString("reservationDate");
        String ringText = ACacheUtil.get(AlarmActivity.this).getAsString("ringText");
        if (!TextUtils.isEmpty(lastTextDate)) {
            date_tv.setText(lastTextDate);
        }
        if (!TextUtils.isEmpty(ringText)) {
            ring_tv.setText(ringText);
        }
    }

    private void setToolBarView() {
        toolBarView.setTitleText("预约下次洗车时间");
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setTvDoneVisibility(View.VISIBLE);
        toolBarView.setTvDoneOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(lastTextDate) && lastTextDate.equals(date_tv.getText().toString())) {
                    ToastUtil.showToast(AlarmActivity.this, "请设置预约时间");
                } else {
                    setClock();
//                    finish();
                }
            }
        });


    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.repeat_rl:
//                selectRemindCycle();
//                break;
            case R.id.wayBtn:
                selectRingWay();
                break;
            case R.id.reservationBtn:
//                if (!TextUtils.isEmpty(lastTextDate) && lastTextDate.equals(date_tv.getText().toString())) {
//                    ToastUtil.showToast(AlarmActivity.this, "请设置预约时间");
//                } else {
//                    setClock();
//                }
                break;
            default:
                break;
        }
    }

    private void setClock() {
        AlarmManagerUtil.setDateAlarm(this, 0, Integer.parseInt(months[0]), Integer.parseInt(months[1]), Integer.parseInt(days[0]), Integer.parseInt
                (days[1]), 0, "您预约的洗车时间已到", ring);

    }
//    private void setClock() {
//        if (time != null && time.length() > 0) {
//            String[] times = time.split(" ");
//            String[] months = times[0].split("-");
//            String[] days = times[1].split(":");
//            if (cycle == 0) {//是每天的闹钟
//                AlarmManagerUtil.setDateAlarm(this, 0, Integer.parseInt(months[0]), Integer.parseInt(months[1]), Integer.parseInt(days[0]), Integer.parseInt
//                        (days[1]), 0, 0, "洗车时间到了", ring);
//            }
//            if (cycle == -1) {//是只响一次的闹钟
//                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
//                        (times[1]), 0, 0, "洗车时间到了", ring);
//            } else {//多选，周几的闹钟
//                String weeksStr = parseRepeat(cycle, 1);
//                String[] weeks = weeksStr.split(",");
//                for (int i = 0; i < weeks.length; i++) {
//                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
//                            .parseInt(times[1]), i, Integer.parseInt(weeks[i]), "洗车时间到了", ring);
//                }
//            }
//            Toast.makeText(this, "预约成功", Toast.LENGTH_LONG).show();
//        }
//
//    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        ring_tv.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        ring_tv.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        ring_tv.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        ring_tv.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        ring_tv.setText("铃声");
                        ring = 1;
                        break;
                    case 2:
                        ring_tv.setText("铃声加震动");
                        ring = 2;
                    default:
                        break;
                }
                ACacheUtil.get(AlarmActivity.this).put("ringText", ring_tv.getText().toString());
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(loongggAlarmReceiver);
    }
}
