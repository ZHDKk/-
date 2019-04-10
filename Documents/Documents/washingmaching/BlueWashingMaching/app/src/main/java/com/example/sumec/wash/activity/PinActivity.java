package com.example.sumec.wash.activity;


import android.app.AlertDialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.CodeView;
import com.example.sumec.wash.view.InputCodeView;
import com.example.sumec.wash.view.ToolBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PinActivity extends BaseActivity implements View.OnClickListener {
    private ToolBarView toolBarView;
    private EditText edPwd1, edPwd2, edPwd3;
    private TextView forgetPwd;
    private String pinData;


    @Override
    public int getContentView() {
        return R.layout.activity_pin;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        edPwd1 = (EditText) findViewById(R.id.ed_pwd1);
        edPwd2 = (EditText) findViewById(R.id.ed_pwd2);
        edPwd3 = (EditText) findViewById(R.id.ed_pwd3);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        forgetPwd.setOnClickListener(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //获取pin码
        MainActivity.sendData(PinActivity.this, DataEventType.GET_PIN);
        setToolBarView();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DataEvent dataEvent) {
        String eventTye = dataEvent.getEventType();
        if (eventTye.equals(DataEventType.GET_PIN_OK)) {
            String data = dataEvent.getMessage();
            pinData = data.substring(11, 12) + data.substring(13, 14) + data.substring(15, 16) + data.substring(17, 18);
            ToastUtil.showToast(PinActivity.this, "-----------》》》》》》原pin码:" + pinData);
        }else if (eventTye.equals(DataEventType.SET_PIN_OK)){
            ToastUtil.showToast(PinActivity.this, "修改成功");
            finish();
        }
    }

    private void setToolBarView() {
        toolBarView.setTitleText("修改PIN码");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTvDoneVisibility(View.VISIBLE);
        toolBarView.setTvDoneOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edPwd1.getText().toString().trim()) && !TextUtils.isEmpty(edPwd2.getText().toString().trim()) && !TextUtils.isEmpty(edPwd3.getText().toString().trim())) {
                    if (edPwd1.getText().toString().trim().equals(pinData)) {
                        if (edPwd2.getText().toString().trim().equals(edPwd3.getText().toString().trim())) {
                            String edStr = edPwd2.getText().toString().trim();
                            String data = "0" + edStr.substring(0, 1) + "0" + edStr.substring(1, 2) + "0" + edStr.substring(2, 3) + "0" + edStr.substring(3, 4);
                            MainActivity.sendData(PinActivity.this, DataEventType.SET_PIN + data);
                        } else {
                            ToastUtil.showToast(PinActivity.this, "两次密码输入不一致");
                        }

                    } else {
                        ToastUtil.showToast(PinActivity.this, "原密码不正确");
                    }
                } else {
                    ToastUtil.showToast(PinActivity.this, "密码不能为空");
                }
            }
        });
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_pwd:
                //忘记密码
                setPinCodeDialog();
                break;
        }
    }

    private void setPinCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PinActivity.this);
        View dialogView = LayoutInflater.from(PinActivity.this).inflate(R.layout.dialogview, null);
        CodeView codeView = (CodeView) dialogView.findViewById(R.id.codeView);
        codeView.settvErrorVisibility(View.GONE);
        codeView.settvForgetVisibility(View.GONE);
        codeView.setTvTitleTitle("输入PUK码");
        codeView.setInputCompleteListener(new InputCodeView.InputCompleteListener() {
            @Override
            public void inputComplete(String text) {
                //TODO 输入PUK判断是否正确

            }

            @Override
            public void deleteContent() {

            }
        });
        builder.setView(dialogView);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
