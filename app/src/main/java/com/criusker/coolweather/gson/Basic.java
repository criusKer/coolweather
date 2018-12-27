package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/26.
 *  "basic": {
 *      "cid": "CN101010100",
 *      "location": "北京",
 *      "parent_city": "北京",
 *      "admin_area": "北京",
 *      "cnty": "中国",
 *      "lat": "39.90498734",
 *      "lon": "116.4052887",
 *      "tz": "+8.00"
 *      },
 */
public class Basic {

    @SerializedName("cid")
    public String cid;
    @SerializedName("location")
    public String location;
    @SerializedName("parent_city")
    public String parentCity;
    @SerializedName("admin_area")
    public String adminArea;
    @SerializedName("cnty")
    public String cnty;
    @SerializedName("lat")
    public String lat;
    @SerializedName("lon")
    public String lon;
    @SerializedName("tz")
    public String tz;

}
