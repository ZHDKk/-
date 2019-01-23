package com.example.sumec.wash.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sumec.wash.R;

/**
 * Created by zhdk on 2019/1/15.
 */

public class CodeView extends LinearLayout{
    private TextView tvTitle,tvError,tvForget;
    private InputCodeView inputCodeView;
    public CodeView(Context context) {
        super(context);
    }

    public CodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pin_dialog,this);
        tvTitle = (TextView) findViewById(R.id.tv_diaogTitle);
        tvError = (TextView) findViewById(R.id.tv_error1);
        tvForget = (TextView) findViewById(R.id.tv_forget1);
        inputCodeView = (InputCodeView) findViewById(R.id.verificationCodeInput1);
    }

    public void setTvTitleTitle(String titleText){
        tvTitle.setText(titleText);
    }

    public void settvErrorVisibility(int visibility){
        tvError.setVisibility(visibility);
    }
    public void settvForgetVisibility(int visibility){
        tvForget.setVisibility(visibility);
    }
    public void settvForgetOnClickListener(OnClickListener listener){
        tvForget.setOnClickListener(listener);
    }
    public void setInputCompleteListener(InputCodeView.InputCompleteListener listener){
        inputCodeView.setInputCompleteListener(listener);
    }
}
