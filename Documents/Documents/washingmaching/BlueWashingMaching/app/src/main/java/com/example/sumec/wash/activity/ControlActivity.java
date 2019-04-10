package com.example.sumec.wash.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sumec.wash.R;
import com.example.sumec.wash.SampleGattAttributes;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.eventbus.DataEvent;
import com.example.sumec.wash.eventbus.DataEventType;
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
import com.tencent.aai.AAIClient;
import com.tencent.aai.audio.data.AudioRecordDataSource;
import com.tencent.aai.auth.LocalCredentialProvider;
import com.tencent.aai.config.ClientConfiguration;
import com.tencent.aai.exception.ClientException;
import com.tencent.aai.exception.ServerException;
import com.tencent.aai.listener.AudioRecognizeResultListener;
import com.tencent.aai.listener.AudioRecognizeStateListener;
import com.tencent.aai.listener.AudioRecognizeTimeoutListener;
import com.tencent.aai.log.AAILogger;
import com.tencent.aai.model.AudioRecognizeRequest;
import com.tencent.aai.model.AudioRecognizeResult;
import com.tencent.aai.model.type.AudioRecognizeConfiguration;
import com.tencent.aai.model.type.AudioRecognizeTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ControlActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRun, btnStop, btnVoice;
    private ToolBarView toolBarView;
    private WakeUpUtil wakeUpUtil;
    private AlertDialog.Builder dialog;
    private LocalCredentialProvider credentialProvider;
    LinkedHashMap<String, String> resMap = new LinkedHashMap<>();
    private AAIClient aaiClient;
    private Handler handler;
    private static final Logger logger = LoggerFactory.getLogger(ControlActivity.class);
    private TextView tvVolume, tvVoiceState;
    private ImageView imgVolume;

    @Override
    public int getContentView() {
        return R.layout.activity_control;
    }

    int currentRequestId = 0;

    @Override
    public void initView() {
        btnRun = (Button) findViewById(R.id.btnRun);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnVoice = (Button) findViewById(R.id.btnVoice);

        btnRun.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnVoice.setOnClickListener(this);

        setToolBarView();

        // 签名鉴权类，sdk中给出了一个本地的鉴权类，但由于需要用户提供secretKey，这可能会导致一些安全上的问题，
        // 因此，请用户自行实现CredentialProvider接口
        credentialProvider = new LocalCredentialProvider(SampleGattAttributes.secretKey);

        // 用户配置
        ClientConfiguration.setServerProtocolHttps(false); // 是否启用https，默认启用
        ClientConfiguration.setMaxAudioRecognizeConcurrentNumber(2); // 语音识别的请求的最大并发数
        ClientConfiguration.setMaxRecognizeSliceConcurrentNumber(10); // 单个请求的分片最大并发数

        handler = new Handler(getMainLooper());
//注册eventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DataEvent dataEvent) {
        String eventTye = dataEvent.getEventType();
            if (eventTye.equals(DataEventType.SET_WORK_OK)) {
                String data = dataEvent.getMessage();
                ToastUtil.showToast(ControlActivity.this, "-----------》》》》》》开始工作:" + data);
            } else if (eventTye.equals(DataEventType.SET_CLOSE_OK)) {
                String data = dataEvent.getMessage();
                ToastUtil.showToast(ControlActivity.this, "-----------》》》》》》停止工作:" + data);
            }
    }

    private void setToolBarView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        toolBarView.setTitleText("佳孚锂电高压清洗机");
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
        switch (view.getId()) {
            case R.id.btnRun:
                //TODO:发送启动命令
                MainActivity.sendData(ControlActivity.this, DataEventType.SET_WORK);
                break;
            case R.id.btnStop:
                //TODO: 发送停止命令
                MainActivity.sendData(ControlActivity.this, DataEventType.SET_CLOSE);
                break;
            case R.id.btnVoice:
                //TODO:启动语音
                createSpeechDialog();
                break;
        }

    }

    private void createSpeechDialog() {
        dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(ControlActivity.this).inflate(R.layout.dialog_voice, null);
        ImageButton btnCross = view.findViewById(R.id.btnCross);
        tvVolume = view.findViewById(R.id.tvVolume);
        tvVoiceState = view.findViewById(R.id.tvVoiceState);
        imgVolume = view.findViewById(R.id.imgVolume);
        Button btnStart = view.findViewById(R.id.btnStart);
        Button btnStop = view.findViewById(R.id.btnStop);
        dialog.setView(view);
        final AlertDialog dia = dialog.show();

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:停止语音
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean taskExist = false;
                        if (aaiClient != null) {
                            taskExist = aaiClient.stopAudioRecognize(currentRequestId);
                        }
                        if (!taskExist) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvVoiceState.setText("不存在该任务，无法停止");
                                }
                            });
                        }
                    }
                }).start();
                dia.dismiss();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:启动语音
                AudioRecognizeRequest.Builder builder = new AudioRecognizeRequest.Builder();
                //File file = new File(Environment.getExternalStorageDirectory()+"/tencent_aai____/audio", "1.pcm");

                // 初始化识别请求
                final AudioRecognizeRequest audioRecognizeRequest = builder
                        .pcmAudioDataSource(new AudioRecordDataSource()) // 设置数据源
                        //.templateName(templateName) // 设置模板
                        .template(new AudioRecognizeTemplate(1, 0, 0)) // 设置自定义模板
                        .build();

                // 自定义识别配置
                final AudioRecognizeConfiguration audioRecognizeConfiguration = new AudioRecognizeConfiguration.Builder()
                        .enableAudioStartTimeout(false) // 是否使能起点超时停止录音
                        .enableAudioEndTimeout(false) // 是否使能终点超时停止录音
                        .enableSilentDetect(true) // 是否使能静音检测，true表示不检查静音部分
                        .minAudioFlowSilenceTime(1000) // 语音流识别时的间隔时间
                        .maxAudioFlowSilenceTime(10000) // 语音终点超时时间
                        .maxAudioStartSilenceTime(10000) // 语音起点超时时间
                        .minVolumeCallbackTime(80) // 音量回调时间
                        .sensitive(2)
                        .build();

                if (aaiClient == null) {
                    try {
                        aaiClient = new AAIClient(ControlActivity.this, Integer.parseInt(SampleGattAttributes.appid), 0, SampleGattAttributes.secretId, credentialProvider);
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
                //currentRequestId = audioRecognizeRequest.getRequestId();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        aaiClient.startAudioRecognize(audioRecognizeRequest, audioRecognizeResultlistener,
                                audioRecognizeStateListener, audioRecognizeTimeoutListener,
                                audioRecognizeConfiguration);

                    }
                }).start();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:停止语音
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean taskExist = false;
                        if (aaiClient != null) {
                            taskExist = aaiClient.stopAudioRecognize(currentRequestId);
                        }
                        if (!taskExist) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvVoiceState.setText("不存在该任务，无法停止");
                                }
                            });
                        }
                    }
                }).start();
            }
        });


    }

    // 识别结果回调监听器
    final AudioRecognizeResultListener audioRecognizeResultlistener = new AudioRecognizeResultListener() {

        boolean dontHaveResult = true;

        /**
         * 返回分片的识别结果
         * @param request 相应的请求
         * @param result 识别结果
         * @param seq 该分片所在语音流的序号 (0, 1, 2...)
         */
        @Override
        public void onSliceSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int seq) {

            if (dontHaveResult && !TextUtils.isEmpty(result.getText())) {
                dontHaveResult = false;
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String time = format.format(date);
                String message = String.format("voice flow order = %d, receive first response in %s, result is = %s", seq, time, result.getText());
//                ToastUtil.showToast(ControlActivity.this, result.getText().toString());
            }

//            resMap.put(String.valueOf(seq), result.getText());
//            final String msg = buildMessage(resMap);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtil.showToast(ControlActivity.this, msg);
//                }
//            });

        }

        /**
         * 返回语音流的识别结果
         * @param request 相应的请求
         * @param result 识别结果
         * @param seq 该语音流的序号 (1, 2, 3...)
         */
        @Override
        public void onSegmentSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int seq) {
            dontHaveResult = true;
            ToastUtil.showToast(ControlActivity.this, result.getText());
            String res = result.getText();
            int length = res.length();
            String msg = res.substring(0, length - 1);
            if (msg.substring(0, 2).equals("校服")
                    || msg.substring(0, 2).equals("小幅")
                    || msg.substring(0, 2).equals("小腹")
                    || msg.substring(0, 2).equals("小福")
                    || msg.substring(0, 2).equals("孝服")
                    || msg.substring(0, 2).equals("孝妇")
                    || msg.substring(0, 2).equals("嚣浮")
                    || msg.substring(0, 2).equals("小孚")) {
                int n = msg.indexOf("工");
                if (msg.substring(n + 1).equals("作")) {
                    MainActivity.sendData(ControlActivity.this, DataEventType.SET_WORK);
                }
                int m = msg.indexOf("停");
                if (msg.substring(m + 1).equals("止")) {
                    MainActivity.sendData(ControlActivity.this, DataEventType.SET_CLOSE);
                }

            } else {
                ToastUtil.showToast(ControlActivity.this, "请说出正确内容");
            }
//            resMap.put(String.valueOf(seq), result.getText());
//            final String msg = buildMessage(resMap);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtil.showToast(ControlActivity.this, msg);
//                }
//            });
        }

        /**
         * 识别结束回调，返回所有的识别结果
         * @param request 相应的请求
         * @param result 识别结果
         */
        @Override
        public void onSuccess(AudioRecognizeRequest request, String result) {
        }

        /**
         * 识别失败
         * @param request 相应的请求
         * @param clientException 客户端异常
         * @param serverException 服务端异常
         */
        @Override
        public void onFailure(AudioRecognizeRequest request, final ClientException clientException, final ServerException serverException) {
            if (clientException != null) {
                AAILogger.info(logger, "onFailure..:" + clientException.toString());
            }
            if (serverException != null) {
                AAILogger.info(logger, "onFailure..:" + serverException.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (clientException != null) {
                        ToastUtil.showToast(ControlActivity.this, "失败,  " + clientException.toString());
                    } else if (serverException != null) {
                        ToastUtil.showToast(ControlActivity.this, "失败,  " + serverException.toString());
                    }
                }
            });
        }
    };

    /**
     * 识别状态监听器
     */
    final AudioRecognizeStateListener audioRecognizeStateListener = new AudioRecognizeStateListener() {

        /**
         * 开始录音
         * @param request
         */
        @Override
        public void onStartRecord(AudioRecognizeRequest request) {
            currentRequestId = request.getRequestId();
            AAILogger.info(logger, "onStartRecord..");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("开始录音");
                    ToastUtil.showToast(ControlActivity.this, "");
                }
            });
        }

        /**
         * 结束录音
         * @param request
         */
        @Override
        public void onStopRecord(AudioRecognizeRequest request) {
            AAILogger.info(logger, "onStopRecord..");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("停止录音");
                    // start.setEnabled(true);
                }
            });
        }

        /**
         * 第seq个语音流开始识别
         * @param request
         * @param seq
         */
        @Override
        public void onVoiceFlowStartRecognize(AudioRecognizeRequest request, int seq) {


            AAILogger.info(logger, "onVoiceFlowStartRecognize.. seq = {}", seq);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("语音识别流开始");
                }
            });
        }

        /**
         * 第seq个语音流结束识别
         * @param request
         * @param seq
         */
        @Override
        public void onVoiceFlowFinishRecognize(AudioRecognizeRequest request, int seq) {

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String time = format.format(date);
            String message = String.format("voice flow order = %d, recognize finish in %s", seq, time);

            AAILogger.info(logger, "onVoiceFlowFinishRecognize.. seq = {}", seq);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("语音识别流结束");
                }
            });
        }

        /**
         * 第seq个语音流开始
         * @param request
         * @param seq
         */
        @Override
        public void onVoiceFlowStart(AudioRecognizeRequest request, int seq) {

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String time = format.format(date);
            String message = String.format("voice flow order = %d, start in %s", seq, time);

            AAILogger.info(logger, "onVoiceFlowStart.. seq = {}", seq);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("语音流开始");
                }
            });
        }

        /**
         * 第seq个语音流结束
         * @param request
         * @param seq
         */
        @Override
        public void onVoiceFlowFinish(AudioRecognizeRequest request, int seq) {

            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String time = format.format(date);
            String message = String.format("voice flow order = %d, stop in %s", seq, time);

            AAILogger.info(logger, "onVoiceFlowFinish.. seq = {}", seq);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("语音流结束");
                }
            });
        }

        /**
         * 语音音量回调
         * @param request
         * @param volume
         */
        @Override
        public void onVoiceVolume(AudioRecognizeRequest request, final int volume) {
            AAILogger.info(logger, "onVoiceVolume..");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVolume.setText("音量:" + volume);
                    if (volume == 0) {
                        imgVolume.setBackgroundResource(R.drawable.volume0);
                    } else if (volume > 0 && volume <= 10) {
                        imgVolume.setBackgroundResource(R.drawable.volume1);
                    } else if (volume > 10 && volume <= 20) {
                        imgVolume.setBackgroundResource(R.drawable.volume2);
                    } else if (volume > 20) {
                        imgVolume.setBackgroundResource(R.drawable.volume3);
                    }
                }
            });
        }

    };

    /**
     * 识别超时监听器
     */
    final AudioRecognizeTimeoutListener audioRecognizeTimeoutListener = new AudioRecognizeTimeoutListener() {

        /**
         * 检测第一个语音流超时
         * @param request
         */
        @Override
        public void onFirstVoiceFlowTimeout(AudioRecognizeRequest request) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("开始说话超时");
                }
            });
        }

        /**
         * 检测下一个语音流超时
         * @param request
         */
        @Override
        public void onNextVoiceFlowTimeout(AudioRecognizeRequest request) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tvVoiceState.setText("结束说话超时");
                }
            });
        }
    };

    private String buildMessage(Map<String, String> msg) {

        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = msg.entrySet().iterator();
        while (iter.hasNext()) {
            String value = iter.next().getValue();
            stringBuffer.append(value + "\r\n");
        }
        return stringBuffer.toString();
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
