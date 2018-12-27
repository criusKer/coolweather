package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by 李菀直 on 2018/12/26.
 * {
 * "HeWeather6": [
 * {
 *      "status": "ok",
 *      "basic": {},
 *      "update": {},
 *      "now": {},
 *      "daily_forecast": [{ }],
 *      "lifestyle": [{}]
 * }
 * ]
 * }
 */
public class Weather {

    public String status;

    public Basic basic;

    public Update update;

    public Now now;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;


}
