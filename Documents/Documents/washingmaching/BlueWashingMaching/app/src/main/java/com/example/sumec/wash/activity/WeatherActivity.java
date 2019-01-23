package com.example.sumec.wash.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.sumec.wash.R;
import com.example.sumec.wash.UrlEmpty;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.model.DailyForecastBean;
import com.example.sumec.wash.model.WeatherBean;
import com.example.sumec.wash.utils.ToastUtil;
import com.example.sumec.wash.view.ToolBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {

    private ToolBarView toolBarView;

    private List<DailyForecastBean> list = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                WeatherBean.HeWeather6Bean heWeather6Bean = (WeatherBean.HeWeather6Bean) msg.obj;
                String status = heWeather6Bean.getStatus();//状态
                String parentCity = heWeather6Bean.getBasic().getParent_city();//城市
                String loc = heWeather6Bean.getUpdate().getLoc();//当地更新时间
                String nowCondTxtd = heWeather6Bean.getDaily_forecast().get(0).getCond_txt_d();//当天天气
                String nowtmpMax = heWeather6Bean.getDaily_forecast().get(0).getTmp_max();//当天最高温度
                String nowtmpMin = heWeather6Bean.getDaily_forecast().get(0).getTmp_min();//当天最高温度
                String nowPop = heWeather6Bean.getDaily_forecast().get(0).getPop();//当天降水率
                String nowWindDir = heWeather6Bean.getDaily_forecast().get(0).getWind_dir();//当天风向
                String nowWinSpd = heWeather6Bean.getDaily_forecast().get(0).getWind_spd();//当天风向
                ToastUtil.showToast(WeatherActivity.this, status + parentCity + loc + nowCondTxtd + nowtmpMax + nowtmpMin + nowPop + nowWindDir + nowWinSpd + "");
                List<WeatherBean.HeWeather6Bean.DailyForecastBean> dailyForecastBeans = heWeather6Bean.getDaily_forecast();
                DailyForecastBean dailyForecast = new DailyForecastBean();
                for (WeatherBean.HeWeather6Bean.DailyForecastBean dailyForecastBean : dailyForecastBeans) {
                    String date = dailyForecastBean.getDate();//预报日期
                    String sr = dailyForecastBean.getSr();//日出时间
                    String ss = dailyForecastBean.getSs();//日落时间
                    String tmpMax = dailyForecastBean.getTmp_max();//最高温度
                    String tmpMin = dailyForecastBean.getTmp_min();//最低温度
                    String condTxtd = dailyForecastBean.getCond_txt_d();//白天天气状况
                    String condTxtn = dailyForecastBean.getCond_txt_n();//晚间天气状况
                    String windDir = dailyForecastBean.getWind_dir();//风向
                    String windSpd = dailyForecastBean.getWind_spd();//风速
                    String hum = dailyForecastBean.getHum();//相对湿度
                    String pcpn = dailyForecastBean.getPcpn();//降水量
                    String pop = dailyForecastBean.getPop();//降水概率
                    String vis = dailyForecastBean.getVis();//能见度，单位：公里
                    dailyForecast.setDate(date);
                    dailyForecast.setSr(sr);
                    dailyForecast.setSs(ss);
                    dailyForecast.setTmp_max(tmpMax);
                    dailyForecast.setTmp_min(tmpMin);
                    dailyForecast.setCond_txt_d(condTxtd);
                    dailyForecast.setCond_txt_n(condTxtn);
                    dailyForecast.setWind_dir(windDir);
                    dailyForecast.setWind_spd(windSpd);
                    dailyForecast.setHum(hum);
                    dailyForecast.setPcpn(pcpn);
                    dailyForecast.setPop(pop);
                    dailyForecast.setVis(vis);
                    list.add(dailyForecast);
                }
            }
        }
    };

    @Override
    public int getContentView() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        setToolBarView();
    }

    private void setToolBarView() {
        toolBarView.setTitleText("天气");
        toolBarView.setImageMoreVisibility(View.GONE);
        toolBarView.setImageBackOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolBarView.setTvDoneVisibility(View.GONE);
        //获取天气数据
        getWeatherData();
    }

    private void getWeatherData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlEmpty.weatherUrl + "key=" + UrlEmpty.weatherKey + "&location=CN101190101")
                .method("GET", null)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO:请求失败执行的方法

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO：请求成功执行的方法
                String data = response.body().string();
                Log.d("天气数据：", data);
                paresJSONWithGson(data);

            }
        });
    }

    private void paresJSONWithGson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Gson gson = new Gson();
            WeatherBean.HeWeather6Bean heWeather6Bean = gson.fromJson(weatherContent, WeatherBean.HeWeather6Bean.class);
            Message message = new Message();
            message.obj = heWeather6Bean;
            message.what = 1;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setWindowFeature() {

    }
}
