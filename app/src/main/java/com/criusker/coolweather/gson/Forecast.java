package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/27.
 * "daily_forecast": [
 *  {
 *      "cond_code_d": "101",
 *      "cond_code_n": "101",
 *      "cond_txt_d": "多云",
 *      "cond_txt_n": "多云",
 *      "date": "2018-12-26",
 *      "hum": "14",
 *      "mr": "21:04",
 *      "ms": "10:28",
 *      "pcpn": "0.0",
 *      "pop": "0",
 *      "pres": "1035",
 *      "sr": "07:34",
 *      "ss": "16:56",
 *      "tmp_max": "-2",
 *      "tmp_min": "-10",
 *      "uv_index": "2",
 *      "vis": "10",
 *      "wind_deg": "-1",
 *      "wind_dir": "无持续风向",
 *      "wind_sc": "3-4",
 *      "wind_spd": "19"
 * },
 * {...}]
 */
public class Forecast {
    @SerializedName("cond_code_d")
    public String condCodeD;
    @SerializedName("cond_code_n")
    public String condCodeN;
    @SerializedName("cond_txt_d")
    public String condTxtD;
    @SerializedName("cond_txt_n")
    public String condTxtN;
    @SerializedName("date")
    public String date;
    @SerializedName("hum")
    public String hum;
    @SerializedName("mr")
    public String mr;
    @SerializedName("ms")
    public String ms;
    @SerializedName("pcpn")
    public String pcpn;
    @SerializedName("pop")
    public String pop;
    @SerializedName("pres")
    public String pres;
    @SerializedName("sr")
    public String sr;
    @SerializedName("ss")
    public String ss;
    @SerializedName("tmp_max")
    public String tmpMax;
    @SerializedName("tmp_min")
    public String tmpMin;
    @SerializedName("uv_index")
    public String uvIndex;
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
