package com.criusker.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by 李菀直 on 2018/12/27.
 * "lifestyle": [
 * {
 *      "type": "comf",
 *      "brf": "很不舒适",
 *      "txt": "今天夜间天气虽然晴好，但天气凉，您会感觉很冷，不舒适，请注意保暖防寒。"
 * },
 * {...}]
 */
public class LifeStyle {

    @SerializedName("type")
    public String type;
    @SerializedName("brf")
    public String brf;
    @SerializedName("txt")
    public String txt;

}
