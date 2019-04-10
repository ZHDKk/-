package com.example.sumec.wash.activity;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.utils.BleUtils;
import com.example.sumec.wash.view.ToolBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HistoryActivity extends BaseActivity {
    private ToolBarView toolBarView;
    private TextView tvLastTime, tvAllTimes, tvAllCharges;

    @Override
    public int getContentView() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolbarView();
        tvLastTime = (TextView) findViewById(R.id.tvLastTime);
        tvAllTimes = (TextView) findViewById(R.id.tvAllTimes);
        tvAllCharges = (TextView) findViewById(R.id.tvCharges);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getWorkInfo();
    }

    private void getWorkInfo() {
        MainActivity.sendData(HistoryActivity.this, DataEventType.GET_FREQUENCY);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.sendData(HistoryActivity.this, DataEventType.GET_TOTAL_TIME);
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.sendData(HistoryActivity.this, DataEventType.GET_WORKING_TIME);
            }
        }, 400);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(DataEvent dataEvent) {
        String eventTye = dataEvent.getEventType();
        if (eventTye.equals(DataEventType.GET_FREQUENCY_OK)) {
            String data = dataEvent.getMessage();
            Log.d("-----------》》》》》》总运行次数:", data);
            if (data.length() > 10) {
                String works = String.valueOf(BleUtils.HexToInt(data.substring(10, 14)));
                tvLastTime.setText(works + "次");
            }
        } else if (eventTye.equals(DataEventType.GET_TOTAL_TIME_OK)) {
            String data = dataEvent.getMessage();
            String allTimes = String.valueOf(BleUtils.HexToInt(data.substring(10, 14)));
            tvAllTimes.setText(allTimes + "分钟");
            Log.d("-----------》》》》》》总工作时间:", data);
        } else if (eventTye.equals(DataEventType.GET_WORKING_TIME_OK)) {
            String data = dataEvent.getMessage();
            String times = String.valueOf(BleUtils.HexToInt(data.substring(10, 14)));
            tvAllCharges.setText(times);
            Log.d("-----------》》》》》》当前工作时间", data);
        }
    }

    @Override
    public void setWindowFeature() {

    }

    private void setToolbarView() {
        toolBarView.setTvDoneVisibility(View.GONE);
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTitleText("历史");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
