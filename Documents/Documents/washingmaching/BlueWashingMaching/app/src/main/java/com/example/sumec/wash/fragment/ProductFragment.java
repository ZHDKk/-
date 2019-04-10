package com.example.sumec.wash.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.activity.ControlActivity;
import com.example.sumec.wash.activity.DeviceScanActivity;
import com.example.sumec.wash.activity.FragmentCallback;
import com.example.sumec.wash.activity.VideoActivity;
import com.example.sumec.wash.activity.VideoListActivity;
import com.example.sumec.wash.base.BaseFragment;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.utils.BleUtils;
import com.example.sumec.wash.utils.UiUtils;
import com.example.sumec.wash.view.CodeView;
import com.example.sumec.wash.view.InputCodeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by zhdk on 2019/1/14.
 */

public class ProductFragment extends BaseFragment implements View.OnClickListener {
    private View productView;
    private Button btnClickConnection;
    private LinearLayout productLayout;
    private Button btnStart, btnVideo;
    private TextView tvPower, tvB1, tvB2, tvB3, tvB4, tvB5, tvB6;
    private FragmentCallback fragmentCallback;
    private AlertDialog pinDialog;
    private int flag;

    @Override
    public View initView(LayoutInflater inflater) {

        if (productView == null) {
            productView = inflater.inflate(R.layout.fragment_product, null);
        }
        return productView;
    }

    @Override
    protected void initFindViewById(View view) {

        btnClickConnection = (Button) view.findViewById(R.id.clickConnection);
        productLayout = (LinearLayout) view.findViewById(R.id.productLayout);
        btnClickConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getActivity(), DeviceScanActivity.class);
            }
        });
        btnStart = (Button) view.findViewById(R.id.btnStart);

        btnVideo = (Button) view.findViewById(R.id.btnVideo);
        tvPower = view.findViewById(R.id.tvPower);  //电量
        tvB1 = view.findViewById(R.id.tvB1);
        tvB2 = view.findViewById(R.id.tvB2);
        tvB3 = view.findViewById(R.id.tvB3);
        tvB4 = view.findViewById(R.id.tvB4);
        tvB5 = view.findViewById(R.id.tvB5);
        tvB6 = view.findViewById(R.id.tvB6);


//        productLayout.setVisibility(View.VISIBLE);
//        btnClickConnection.setVisibility(View.GONE);
//        setPinCodeDialog();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initData() {
        btnStart.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DataEvent dataEvent) {
        String eventType = dataEvent.getEventType();
        if (eventType.equals(DataEventType.CONNECT_SUCCESS)) {
            //TODO:显示产品信息并弹出密码输入框
            if (productLayout.getVisibility() == View.GONE && btnClickConnection.getVisibility() == View.VISIBLE) {
                getPin();

            }

        } else if (eventType.equals(DataEventType.CONNECT_FAILED)) {
//            TODO:隐藏产品信息
            if (productLayout.getVisibility() == View.VISIBLE && btnClickConnection.getVisibility() == View.GONE) {
                productLayout.setVisibility(View.GONE);
                btnClickConnection.setVisibility(View.VISIBLE);
            }
        } else if (eventType.equals(DataEventType.GET_BATTERY_OK)) {
            productLayout.setVisibility(View.VISIBLE);
            btnClickConnection.setVisibility(View.GONE);
            String message = dataEvent.getMessage();
            Log.d("--------->>>>>>>>", "电池信息" + message);
            String strB1 = String.valueOf(BleUtils.HexToInt(message.substring(10, 14)));
            tvB1.setText(strB1 + "V");
            String strB3 = String.valueOf(BleUtils.HexToInt(message.substring(14, 16)));
            tvB3.setText(strB3 + "℃");
            String strB4 = String.valueOf(BleUtils.HexToInt(message.substring(16, 18)));
            tvB4.setText(strB4 + "AH");
            fragmentCallback.getMainActivity().sendData(getActivity(), DataEventType.GET_CURRENT);
        } else if (eventType.equals(DataEventType.GET_CURRENT_OK)) {
            String message = dataEvent.getMessage();
            Log.d("--------->>>>>>>>", "当前工作电流" + message);
            String strB2 = String.valueOf(BleUtils.HexToInt(message.substring(10, 14)));
            tvB2.setText(strB2 + "A");
            fragmentCallback.getMainActivity().sendData(getActivity(), DataEventType.GET_MAX_CURRENT);
        } else if (eventType.equals(DataEventType.GET_MAX_CURRENT_OK)) {
            String message = dataEvent.getMessage();
            String strB5 = String.valueOf(BleUtils.HexToInt(message.substring(10, 14)));
            tvB5.setText(strB5 + "A");
            Log.d("--------->>>>>>>>", "最大工作电流" + message);
        } else if (eventType.equals(DataEventType.GET_PIN_OK)) {
            if (flag == 0) {
                setPinCodeDialog();
                flag = 1;
            }
            String pinStr = dataEvent.getMessage();
            Log.d("------->>>>>Pin", pinStr);
        }
    }

    private void getPin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentCallback.getMainActivity().sendData(getActivity(), DataEventType.GET_PIN);
            }
        }, 1000);
    }


    private void setPinCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialogview, null);
        CodeView codeView = (CodeView) dialogView.findViewById(R.id.codeView);
        codeView.settvErrorVisibility(View.GONE);
        codeView.setInputCompleteListener(new InputCodeView.InputCompleteListener() {
            @Override
            public void inputComplete(String text) {
                //TODO 输入密码判断是否正确
                if (text.equals("0000")) {
                    pinDialog.dismiss();
                    UiUtils.closeKeyboardForce(getActivity());
                    fragmentCallback.getMainActivity().sendData(getActivity(), DataEventType.GET_BATTERY);

                }
            }

            @Override
            public void deleteContent() {

            }
        });
        builder.setView(dialogView);
        pinDialog = builder.create();
        //设置点击屏幕不消失
        pinDialog.setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        pinDialog.setCancelable(false);

        pinDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                //TODO:点击开始工作
                startActivity(getActivity(), ControlActivity.class);
                break;
            case R.id.btnVideo:
                //TODO:点击观看视频
                startActivity(getActivity(), VideoListActivity.class);
                break;
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallback = (FragmentCallback) activity;
    }
}
