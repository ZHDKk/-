package com.example.sumec.wash.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.sumec.wash.R;
import com.example.sumec.wash.UrlEmpty;
import com.example.sumec.wash.adapter.WeatherAdapter;
import com.example.sumec.wash.base.BaseActivity;
import com.example.sumec.wash.model.DailyForecastBean;
import com.example.sumec.wash.model.NowWeatherBean;
import com.example.sumec.wash.model.WeatherBean;
import com.example.sumec.wash.utils.UiUtils;
import com.example.sumec.wash.view.ToolBarView;
import com.example.sumec.wash.view.recycleview.PullLoadMoreRecyclerView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    private ToolBarView toolBarView;
    private TextView tvLocation, tvWindSc, tvPcpn, tvTmp, tvLoc, tvCond, tvFl;
    private PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    private WeatherAdapter weatherAdapter;

    private List<DailyForecastBean> list = new ArrayList<>();
    private LocationClient locationClient = null;
    private SpotsDialog dialog;
    private DailyForecastBean dailyForecast;
    private FloatingActionButton fabAlarm;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                WeatherBean.HeWeather6Bean heWeather6Bean = (WeatherBean.HeWeather6Bean) msg.obj;
                String status = heWeather6Bean.getStatus();//状态
                String parentCity = heWeather6Bean.getBasic().getParent_city();//城市
                String loc = heWeather6Bean.getUpdate().getLoc();//当地更新时间
                List<WeatherBean.HeWeather6Bean.DailyForecastBean> dailyForecastBeans = heWeather6Bean.getDaily_forecast();
                for (WeatherBean.HeWeather6Bean.DailyForecastBean dailyForecastBean : dailyForecastBeans) {
                    dailyForecast = new DailyForecastBean();
                    String date = dailyForecastBean.getDate();//预报日期
                    String sr = dailyForecastBean.getSr();//日出时间
                    String ss = dailyForecastBean.getSs();//日落时间
                    String tmpMax = dailyForecastBean.getTmp_max();//最高温度
                    String tmpMin = dailyForecastBean.getTmp_min();//最低温度
                    String condCoded = dailyForecastBean.getCond_code_d();//白天天气状况
                    String condCoden = dailyForecastBean.getCond_code_n();//晚间天气状况
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
                    dailyForecast.setCond_code_d(condCoded);
                    dailyForecast.setCond_code_n(condCoden);
                    dailyForecast.setWind_dir(windDir);
                    dailyForecast.setWind_spd(windSpd);
                    dailyForecast.setHum(hum);
                    dailyForecast.setPcpn(pcpn);
                    dailyForecast.setPop(pop);
                    dailyForecast.setVis(vis);
                    list.add(dailyForecast);
                }
                weatherAdapter = new WeatherAdapter(WeatherActivity.this, list);
                pullLoadMoreRecyclerView.setAdapter(weatherAdapter);

            } else if (msg.what == 1) {
                NowWeatherBean.HeWeather6Bean heWeather6Bean = (NowWeatherBean.HeWeather6Bean) msg.obj;
                String status = heWeather6Bean.getStatus();
                String fl = heWeather6Bean.getNow().getFl();//体感温度
                String tmp = heWeather6Bean.getNow().getTmp();//温度
                String condTxt = heWeather6Bean.getNow().getCond_txt();//实况天气状况
                String windDir = heWeather6Bean.getNow().getWind_dir();//风向
                String windSc = heWeather6Bean.getNow().getWind_sc();//风力
                String hum = heWeather6Bean.getNow().getHum();//湿度
                String pcpn = heWeather6Bean.getNow().getPcpn();//降水量
                Date date = Calendar.getInstance().getTime();
                String time = UiUtils.DateToWeek(date);
                tvLoc.setText(time);
                tvLocation.setText(district);
                tvFl.setText("体感温度：" + fl);
                tvTmp.setText(tmp);
                tvCond.setText(condTxt);
                tvWindSc.setText("风力：" + windSc);
                tvPcpn.setText("降水量：" + pcpn);
                //获取未来天气数据
                getWeatherData(city);
            }
        }
    };
    private String city;
    private String district;
    private OkHttpClient okHttpClient;


    @Override
    public int getContentView() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        toolBarView = (ToolBarView) findViewById(R.id.toolbarView);
        tvLocation = findViewById(R.id.tv_location);
        tvWindSc = findViewById(R.id.tv_windSc);
        tvLoc = findViewById(R.id.tv_loc);
        tvCond = findViewById(R.id.tv_condTxt);
        tvPcpn = findViewById(R.id.tv_pcpn);
        tvFl = findViewById(R.id.tv_fl);
        tvTmp = findViewById(R.id.tv_tmp);
        okHttpClient = new OkHttpClient();
        initRecyclerView();
        fabAlarm = findViewById(R.id.fabAlarm);
        fabAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(WeatherActivity.this, AlarmActivity.class);
            }
        });
        setToolBarView();

        dialog = new SpotsDialog(this);


    }

    private void initRecyclerView() {
        pullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.mPullLoadMoreRecyclerView);
        //获取mRecyclerView对象
        RecyclerView recyclerView = pullLoadMoreRecyclerView.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        recyclerView.setVerticalScrollBarEnabled(true);
        //设置下拉刷新是否可见
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        //设置是否可以下拉刷新
        pullLoadMoreRecyclerView.setPullRefreshEnable(false);
        //设置是否可以上拉刷新
        pullLoadMoreRecyclerView.setPushRefreshEnable(false);
        //显示下拉刷新
        pullLoadMoreRecyclerView.setRefreshing(true);
        //设置上拉刷新文字
        pullLoadMoreRecyclerView.setFooterViewText(getString(R.string.load_more_text));
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        //设置上拉刷新文字颜色
        //mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.white);
        //设置加载更多背景色
        //mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.colorBackground);
        pullLoadMoreRecyclerView.setLinearLayout();
        //开启定位
        initLocationOption();
    }

    private void initLocationOption() {
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption locationOption = new LocationClientOption();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //设置位置取得模式  （这里是指定为设备传感器也就是 GPS 定位）
        locationOption.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //设置 间隔扫描的时间  也就是 位置时隔多长时间更新
//        locationOption.setScanSpan(5000);
        //设置 是否需要地址 （需要联网取得 百度提供的位置信息）
        locationOption.setIsNeedAddress(true);

        locationClient.setLocOption(locationOption);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                String addr = location.getAddrStr();    //获取详细地址信息
                String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                city = location.getCity();    //获取城市
                district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息

                //获取当天天气
                getRealTimeWeatherData(city);

            }
        });
        //开始定位
        locationClient.start();
    }

    private void getRealTimeWeatherData(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(UrlEmpty.newWeatherUrl + "key=" + UrlEmpty.weatherKey + "&location=" + city)
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
                        Log.d("实况天气数据：", data);
                        paresJSONWithGsonNowWeather(data);
                    }
                });
            }
        }).start();

    }

    private void paresJSONWithGsonNowWeather(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Gson gson = new Gson();
            NowWeatherBean.HeWeather6Bean heWeather6Bean = gson.fromJson(weatherContent, NowWeatherBean.HeWeather6Bean.class);
            Message message = new Message();
            message.obj = heWeather6Bean;
            message.what = 1;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
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
    }

    private void getWeatherData(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(UrlEmpty.weatherUrl + "key=" + UrlEmpty.weatherKey + "&location=" + city)
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
                        Log.d("未来7天天气数据：", data);
                        if (dialog.isShowing())
                            dialog.dismiss();
                        paresJSONWithGson(data);

                    }
                });
            }
        }).start();

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
            message.what = 2;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setWindowFeature() {

    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
