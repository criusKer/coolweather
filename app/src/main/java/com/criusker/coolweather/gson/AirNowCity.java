package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/27.
 * "air_now_city": {
 *      "aqi": "39",
 *      "qlty": "优",
 *      "main": "-",
 *      "pm25": "25",
 *      "pm10": "39",
 *      "no2": "28",
 *      "so2": "14",
 *      "co": "0.4",
 *      "o3": "52",
 *      "pub_time": "2018-12-27 10:00"
 * },
 */
public class AirNowCity {

    public String aqi;

    public String qlty;

    public String main;

    public String pm25;

    public String pm10;

    public String no2;

    public String so2;

    public String co;

    public String o3;

    @SerializedName("pub_time")
    public String pubTime;
}
