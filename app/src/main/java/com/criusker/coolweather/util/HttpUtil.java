package com.criusker.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Create by 李菀直 on 2018/12/26.
 * 网络请求工具类:服务器返回的是JSON格式
 */
public class HttpUtil {
    //用OKHttp发送网络请求
    public static void sendOKHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
