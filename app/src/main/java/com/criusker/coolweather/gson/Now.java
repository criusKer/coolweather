package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/27.
 *  "now": {
 *      "cloud": "91",
 *      "cond_code": "100",
 *      "cond_txt": "晴",
 *      "fl": "-11",
 *      "hum": "20",
 *      "pcpn": "0.0",
 *      "pres": "1037",
 *      "tmp": "-6",
 *      "vis": "22",
 *      "wind_deg": "4",
 *      "wind_dir": "北风",
 *      "wind_sc": "2",
 *      "wind_spd": "11"
 *  },
 */
public class Now {

    @SerializedName("cloud")
    public String cloud;
    @SerializedName("cond_code")
    public String condCode;
    @SerializedName("cond_txt")
    public String condTxt;
    @SerializedName("fl")
    public String fl;
    @SerializedName("hum")
    public String hum;
    @SerializedName("pcpn")
    public String pcpn;
    @SerializedName("pres")
    public String pres;
    @SerializedName("tmp")
    public String tmp;
    @SerializedName("vis")
    public String vis;
    @SerializedName("wind_deg")
    public String windDeg;
    @SerializedName("wind_dir")
    public String windDir;
    @SerializedName("wind_sc")
    public String windSc;
    @SerializedName("wind_spd")
    public String windSpd;
}
