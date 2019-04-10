package com.example.sumec.wash.activity;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.utils.BleUtils;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.ToolBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StallActivity extends BaseActivity implements View.OnClickListener {
    private ToolBarView toolBarView;
    private LinearLayout softLl, generalLl, efficientLl, strongLl;
    private ImageView softImage, generalImage, efficentImage, strongImage;
    private TextView tvSoft, tvGeneral, tvEfficent, tvStrong;

    @Override
    public int getContentView() {
        return R.layout.activity_stall;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        softLl = (LinearLayout) findViewById(R.id.softLl);
        softLl.setOnClickListener(this);
        generalLl = (LinearLayout) findViewById(R.id.generalLl);
        generalLl.setOnClickListener(this);
        efficientLl = (LinearLayout) findViewById(R.id.efficientLl);
        efficientLl.setOnClickListener(this);
        strongLl = (LinearLayout) findViewById(R.id.strongLl);
        strongLl.setOnClickListener(this);
        softImage = (ImageView) findViewById(R.id.softImage);
        generalImage = (ImageView) findViewById(R.id.generalImage);
        efficentImage = (ImageView) findViewById(R.id.efficientImage);
        strongImage = (ImageView) findViewById(R.id.strongImage);
        tvSoft = (TextView) findViewById(R.id.softTv);
        tvGeneral = (TextView) findViewById(R.id.generalTv);
        tvEfficent = (TextView) findViewById(R.id.efficientTv);
        tvStrong = (TextView) findViewById(R.id.strongTv);
        setToolBarView();
//注册eventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void setToolBarView() {
        toolBarView.setTitleText("佳孚无线自带水桶高压清洗机");
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setTvDoneVisibility(View.GONE);

        getSpeedDrive();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DataEvent dataEvent) {
        String eventTye = dataEvent.getEventType();
        if (eventTye.equals(DataEventType.GET_SPEED_DRIVE_OK)) {
            String data = dataEvent.getMessage();
            String stallData =data.substring(10, 12);
            ToastUtil.showToast(StallActivity.this, "-----------》》》》》》档位:" + stallData);
            if (stallData.equals("00")) {
                softLl.setBackgroundColor(getResources().getColor(R.color.blue1));
                generalLl.setBackgroundColor(Color.WHITE);
                efficientLl.setBackgroundColor(Color.WHITE);
                strongLl.setBackgroundColor(Color.WHITE);
            } else if (stallData.equals("01")) {
                softLl.setBackgroundColor(Color.WHITE);
                generalLl.setBackgroundColor(getResources().getColor(R.color.blue1));
                efficientLl.setBackgroundColor(Color.WHITE);
                strongLl.setBackgroundColor(Color.WHITE);
            }
            if (stallData.equals("02")) {
                softLl.setBackgroundColor(Color.WHITE);
                generalLl.setBackgroundColor(Color.WHITE);
                efficientLl.setBackgroundColor(getResources().getColor(R.color.blue1));
                strongLl.setBackgroundColor(Color.WHITE);
            }
            if (stallData.equals("03")) {
                softLl.setBackgroundColor(Color.WHITE);
                generalLl.setBackgroundColor(Color.WHITE);
                efficientLl.setBackgroundColor(Color.WHITE);
                strongLl.setBackgroundColor(getResources().getColor(R.color.blue1));
            }
        } else if (eventTye.equals(DataEventType.SET_SPEED_DRIVE_OK)) {
            ToastUtil.showToast(StallActivity.this, "档位设置成功");
            getSpeedDrive();
        }
    }

    private void getSpeedDrive() {
        MainActivity.sendData(StallActivity.this, DataEventType.GET_SPEED_DRIVE);
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.softLl:
                MainActivity.sendData(StallActivity.this, DataEventType.SET_SPEED_DRIVE + "00");
                break;
            case R.id.generalLl:
                MainActivity.sendData(StallActivity.this, DataEventType.SET_SPEED_DRIVE + "01");
                break;
            case R.id.efficientLl:
                MainActivity.sendData(StallActivity.this, DataEventType.SET_SPEED_DRIVE + "02");
                break;
            case R.id.strongLl:
                MainActivity.sendData(StallActivity.this, DataEventType.SET_SPEED_DRIVE + "03");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
