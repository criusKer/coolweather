package com.criusker.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.criusker.coolweather.gson.Air;
import com.criusker.coolweather.gson.Forecast;
import com.criusker.coolweather.gson.Weather;
import com.criusker.coolweather.util.HttpUtil;
import com.criusker.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ImageView bingPicImg;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;

    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使图片融入状态栏
        if(Build.VERSION.SDK_INT >= 21){//需要安卓5.0及以上才支持
            /**
             * 获取DecorView：activity窗口的根视图
             * setSystemUiVisibility:改变系统UI的显示
             * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE：活动的布局会显示在状态栏上面
             * getWindow().setStatusBarColor(Color.TRANSPARENT:将状态栏改成透明色
             */
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //天气
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        //空气质量
        String airString = prefs.getString("air",null);
        if(airString != null){
            //有缓存时直接解析空气质量数据
            Air air = Utility.handleAirResponse(airString);
            showAirInfo(air);
        }else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            requestAir(weatherId);
        }
        //图片加载
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }
    /**
     * 根据weather_id请求城市天气信息
     * url:https://free-api.heweather.net/s6/weather?location=CN101190402&key=cf13198cf1774afd9510f0840922cb9c
     */
    public void requestWeather(String weatherId){
        String weatherUrl = "https://free-api.heweather.net/s6/weather?location="+weatherId+"&key=cf13198cf1774afd9510f0840922cb9c";
        Log.d(TAG, "requestWeather: "+weatherId);
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();//获取JSON数据
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                            Log.d(TAG, "获取天气信息成功"+weather.status);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 根据weather_id请求城市空气质量信息
     * url:https://free-api.heweather.net/s6/air?location=CN101190402&key=cf13198cf1774afd9510f0840922cb9c
     */
    public void requestAir(String weatherId){
        String airUrl = "https://free-api.heweather.net/s6/air?location="+weatherId+"&key=cf13198cf1774afd9510f0840922cb9c";
        HttpUtil.sendOKHttpRequest(airUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"onFailure:获取空气质量信息失败",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure:获取空气质量信息失败");
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();//获取JSON数据
                final Air air = Utility.handleAirResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (air != null && "ok".equals(air.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("air",responseText);
                            editor.apply();
                            showAirInfo(air);
                            Log.d(TAG, "获取空气质量信息成功");
                        }else {
                            Toast.makeText(WeatherActivity.this,"onResponse:获取空气质量信息失败",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse:获取空气质量信息失败"+air.status);
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){

        String location = weather.basic.location;//地区：南昌
        //split("")[1]:以空格为断点产生一个字符串数组 取下标为1的值 例 2018-12-27 09：56 -> ["2018-12-27","09:56"] 取09:56
        String updateTime = weather.update.loc.split(" ")[1];//时间：09:56
        String degree = weather.now.tmp+"℃";//气温：10摄氏度
        String weatherInfo = weather.now.condTxt;//天气：晴
        titleCity.setText(location);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.condTxtD);
            maxText.setText(forecast.tmpMax);
            minText.setText(forecast.tmpMin);
            forecastLayout.addView(view);
        }
        String comfort = "舒适度："+ weather.lifeStyleList.get(0).txt;
        String carWash = "洗车指数："+ weather.lifeStyleList.get(6).txt;
        String sport = "运动建议："+ weather.lifeStyleList.get(3).txt;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 处理并展示Air实体类中的数据
     */
    private void showAirInfo(Air air){
        if (air != null){
            aqiText.setText(air.airNowCity.aqi);
            pm25Text.setText(air.airNowCity.pm25);
        }

    }

    /**
     * 加载必应每日一图
     * http://guolin.tech/api/bing_pic
     */
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
