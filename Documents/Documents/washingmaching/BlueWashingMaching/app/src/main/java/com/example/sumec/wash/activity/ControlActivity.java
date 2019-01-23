package com.example.sumec.wash.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sumec.wash.R;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.model.SpeechBean;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.utils.WakeUpUtil;
import com.example.sumec.wash.view.ToolBarView;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import java.util.ArrayList;

public class ControlActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRun,btnStop,btnVoice;
    private ToolBarView toolBarView;
    private WakeUpUtil wakeUpUtil;


    @Override
    public int getContentView() {
        return R.layout.activity_control;
    }

    @Override
    public void initView() {
        btnRun = (Button) findViewById(R.id.btnRun);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnVoice = (Button) findViewById(R.id.btnVoice);
        btnRun.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnVoice.setOnClickListener(this);

        setToolBarView();
    }

    private void setToolBarView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        toolBarView.setTitleText("佳孚无线自带水桶高压清洗机");
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setTvDoneVisibility(View.GONE);
    }

    @Override
    public void setWindowFeature() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRun:
                //TODO:发送启动命令

                break;
            case R.id.btnStop:
                //TODO: 发送停止命令

                break;
            case R.id.btnVoice:
                //TODO:启动语音
                createSpeechDialog();
                break;
        }

    }

    private void createSpeechDialog() {
        RecognizerDialog recognizerDialog = new RecognizerDialog(ControlActivity.this,null);
        recognizerDialog.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        recognizerDialog.setParameter(SpeechConstant.ACCENT,"mandarin");
        recognizerDialog.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if (!b){
                    String result = parseVoice(recognizerResult.getResultString());
                    ToastUtil.showToast(ControlActivity.this,result);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e("返回错误吗",speechError.getErrorCode()+"");
            }
        });
        recognizerDialog.show();

    }
    /**
     * 解析语音json
     */
    public String parseVoice(String resultString) {
        Gson gson = new Gson();
        SpeechBean voiceBean = gson.fromJson(resultString, SpeechBean.class);
        StringBuffer sb = new StringBuffer();
        ArrayList<SpeechBean.WSBean> ws = voiceBean.ws;
        for (SpeechBean.WSBean wsBean : ws) {
            String word = wsBean.cw.get(0).w;
            sb.append(word);
        }
        return sb.toString();
    }

    @Override
    protected void onResume() {
        FlowerCollector.onResume(ControlActivity.this);
        super.onResume();
    }
}
