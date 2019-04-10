package com.example.sumec.wash.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.activity.MainActivity;
import com.example.sumec.wash.activity.PinActivity;
import com.example.sumec.wash.activity.StallActivity;
import com.example.sumec.wash.activity.WeatherActivity;
import com.example.sumec.wash.base.BaseFragment;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhdk on 2019/1/14.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private View settingView;
    private LinearLayout stallLl, pinLl, weatherLl, reviewLl;

    @Override
    public View initView(LayoutInflater inflater) {
        if (settingView == null) {
            settingView = inflater.inflate(R.layout.fragment_setting, null);
        } else {
            return settingView;
        }
        return settingView;
    }

    @Override
    protected void initFindViewById(View view) {
        stallLl = (LinearLayout) view.findViewById(R.id.stallSet);
        pinLl = (LinearLayout) view.findViewById(R.id.pinSet);
        weatherLl = (LinearLayout) view.findViewById(R.id.weatherSet);
        reviewLl = (LinearLayout) view.findViewById(R.id.reviewSet);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initData() {
        stallLl.setOnClickListener(this);
        pinLl.setOnClickListener(this);
        weatherLl.setOnClickListener(this);
        reviewLl.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DataEvent dataEvent) {
        String dataType = dataEvent.getEventType();
        if (dataType.equals(DataEventType.SET_RESET_OK)) {
            ToastUtil.showToast(getActivity(), "恢复出厂设置成功");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stallSet:
                startActivity(getActivity(), StallActivity.class);
                break;
            case R.id.pinSet:
                startActivity(getActivity(), PinActivity.class);
                break;
            case R.id.weatherSet:
                startActivity(getActivity(), WeatherActivity.class);
                break;
            case R.id.reviewSet:
                reviewDialog();
                break;
        }
    }

    private void reviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示:");
        builder.setMessage("确认恢复到出厂设置吗？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO:发送恢复出厂设置的命令
                MainActivity.sendData(getActivity(), DataEventType.SET_RESET);
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
