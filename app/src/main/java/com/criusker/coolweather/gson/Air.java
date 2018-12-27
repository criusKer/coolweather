package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by 李菀直 on 2018/12/27.
 * {
 * "HeWeather6": [
 * {
 *      "basic": {},
 *      "update": {},
 *      "status": "ok",
 *      "air_now_city": {},
 *      "air_now_station": [{}]
 * }
 * ]
 * }
 *
 */
public class Air {

    public String status;

    public Basic basic;

    public Update update;

    @SerializedName("air_now_city")
    public AirNowCity airNowCity;

    @SerializedName("air_now_station")
    public List<AirNowStation> airNowStationList;
}
