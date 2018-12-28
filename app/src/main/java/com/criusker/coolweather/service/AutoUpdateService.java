package com.criusker.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.criusker.coolweather.WeatherActivity;
import com.criusker.coolweather.gson.Air;
import com.criusker.coolweather.gson.Weather;
import com.criusker.coolweather.util.HttpUtil;
import com.criusker.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    private static final String TAG = "AutoUpdateService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();//更新天气信息
        updateBingPic();//更新必应每日一图
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);//创建定时任务
        int anHour = 8*60*60*1000;//8小时 时间间隔
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;//触发时间
        Intent i = new Intent(this,AutoUpdateService.class);//自己调用自己 无限循环
        PendingIntent pi = PendingIntent.getService(AutoUpdateService.this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //天气
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.cid;

            String weatherUrl = "https://free-api.heweather.net/s6/weather?location=" + weatherId + "&key=cf13198cf1774afd9510f0840922cb9c";
            Log.d(TAG, "requestWeather: " + weatherId);
            HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();//获取JSON数据
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                        Log.d(TAG, "获取天气信息成功" + weather.status);
                    }
                }
            });
        }
        //空气质量
        String airString = prefs.getString("air",null);
        if(airString != null){
            //有缓存时直接解析空气质量数据
            Air air = Utility.handleAirResponse(airString);
            String weatherId = air.basic.cid;

            String airUrl = "https://free-api.heweather.net/s6/air?location="+weatherId+"&key=cf13198cf1774afd9510f0840922cb9c";
            HttpUtil.sendOKHttpRequest(airUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();//获取JSON数据
                    final Air air = Utility.handleAirResponse(responseText);
                    if (air != null && "ok".equals(air.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("air",responseText);
                        editor.apply();
                        Log.d(TAG, "获取空气质量信息成功");
                    }
                }
            });

        }
    }
    /**
     * 更新必应每日一图
     */
    private void updateBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}
