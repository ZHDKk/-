package com.example.sumec.wash.fragment;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.sumec.wash.R;
import com.example.sumec.wash.activity.ControlActivity;
import com.example.sumec.wash.activity.DeviceScanActivity;
import com.example.sumec.wash.activity.VideoActivity;
import com.example.sumec.wash.base.BaseFragment;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.view.CodeView;
import com.example.sumec.wash.view.InputCodeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zhdk on 2019/1/14.
 */

public class ProductFragment extends BaseFragment implements View.OnClickListener {
    private View productView;
    private Button btnClickConnection;
    private LinearLayout productLayout;
    private Button btnStart,btnVideo;
    @Override
    public View initView(LayoutInflater inflater) {

        if (productView == null){
            productView = inflater.inflate(R.layout.fragment_product,null);
        }
        return productView;
    }

    @Override
    protected void initFindViewById(View view) {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
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

        productLayout.setVisibility(View.VISIBLE);
        btnClickConnection.setVisibility(View.GONE);
        setPinCodeDialog();
    }

    @Override
    public void initData() {
        btnStart.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void getData(DataEvent dataEvent){
        String eventType= dataEvent.getEventType();
        if (eventType.equals(DataEventType.CONNECT_SUCCESS)){
            //TODO:显示产品信息并弹出密码输入框
            if (productLayout.getVisibility() == View.GONE && btnClickConnection.getVisibility() == View.VISIBLE) {
                productLayout.setVisibility(View.VISIBLE);
                btnClickConnection.setVisibility(View.GONE);
            }
            setPinCodeDialog();
        }else if (eventType.equals(DataEventType.CONNECT_FAILED)){
            //TODO:隐藏产品信息
//            if (productLayout.getVisibility() == View.VISIBLE && btnClickConnection.getVisibility() == View.GONE) {
//                productLayout.setVisibility(View.GONE);
//                btnClickConnection.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void setPinCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.dialogview,null);
        CodeView codeView = (CodeView) dialogView.findViewById(R.id.codeView);
        codeView.settvErrorVisibility(View.GONE);
        codeView.setInputCompleteListener(new InputCodeView.InputCompleteListener() {
            @Override
            public void inputComplete(String text) {
                //TODO 输入密码判断是否正确
            }

            @Override
            public void deleteContent() {

            }
        });
        builder.setView(dialogView);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                //TODO:点击开始工作
                startActivity(getActivity(), ControlActivity.class);
                break;
            case R.id.btnVideo:
                //TODO:点击观看视频
            startActivity(getActivity(), VideoActivity.class);
                break;
        }

    }
}
