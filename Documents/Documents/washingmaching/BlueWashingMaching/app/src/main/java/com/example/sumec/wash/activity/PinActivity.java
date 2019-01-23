package com.example.sumec.wash.activity;


import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.CodeView;
import com.example.sumec.wash.view.InputCodeView;
import com.example.sumec.wash.view.ToolBarView;

public class PinActivity extends BaseActivity implements View.OnClickListener {
    private ToolBarView toolBarView;
    private EditText edPwd1,edPwd2,edPwd3;
    private TextView forgetPwd;


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
        setToolBarView();


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
                if (!TextUtils.isEmpty(edPwd1.getText().toString().trim())&&!TextUtils.isEmpty(edPwd2.getText().toString().trim())&&!TextUtils.isEmpty(edPwd3.getText().toString().trim())) {
                    if (edPwd1.getText().toString().trim().equals("oldPwd")) {
                        if (edPwd2.getText().toString().trim().equals(edPwd3.getText().toString().trim())) {

                        } else {
                            ToastUtil.showToast(PinActivity.this,"两次密码输入不一致");
                        }

                    } else {
                        ToastUtil.showToast(PinActivity.this, "原密码不正确");
                    }
                }else {
                    ToastUtil.showToast(PinActivity.this,"密码不能为空");
                }
            }
        });
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_pwd:
                //忘记密码
                setPinCodeDialog();
                break;
        }
    }

    private void setPinCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PinActivity.this);
        View dialogView= LayoutInflater.from(PinActivity.this).inflate(R.layout.dialogview,null);
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
    }
}
