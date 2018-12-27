package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/26.
 * "update": {
 *    "loc": "2018-12-26 22:56",
 *    "utc": "2018-12-26 14:56"
 *  },
 */
public class Update {
    @SerializedName("loc")
    public String loc;
    @SerializedName("utc")
    public String utc;
}
