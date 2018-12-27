package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/27.
 * "air_now_station": [
 * {
 *      "air_sta": "海虞子站",
 *      "aqi": "36",
 *      "asid": "CNA1990",
 *      "co": "0.4",
 *      "lat": "31.67",
 *      "lon": "120.758",
 *      "main": "-",
 *      "no2": "27",
 *      "o3": "55",
 *      "pm10": "36",
 *      "pm25": "23",
 *      "pub_time": "2018-12-27 10:00",
 *      "qlty": "优",
 *      "so2": "11"
 * },
 */
public class AirNowStation {

    @SerializedName("air_sta")
    public String air_sta;

    public String aqi;

    public String asid;

    public String co;

    public String lat;

    public String lon;

    public String main;

    public String no2;

    public String o3;

    public String pm10;

    public String pm25;

    @SerializedName("pub_time")
    public String pubTime;

    public String qlty;

    public String so2;
}
