package com.criusker.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.criusker.coolweather.db.City;
import com.criusker.coolweather.db.County;
import com.criusker.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create by 李菀直 on 2018/12/26.
 * 解析处理JSON数据类
 */
public class Utility {

    private static final String TAG = "Utility";

    /**
     * 解析和处理服务器返回的省级数据
     * http://guolin.tech/apip/china
     * int id;
     * String provinceName;
     * int provinceCode;
     * {"id":16,"name":"江苏"}
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){//先判断返回的数据不为空
            try {
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                    Log.d(TAG, "保存省数据成功");
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * http://guolin.tech/apip/china/16
     * int id;
     * String cityName;
     * int cityCode;
     * int provinceId;
     * {"id":116,"name":"苏州"}
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){//先判断返回的数据不为空
            try {
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * http://guolin.tech/apip/china/16/116
     * int id;
     * String countyName;
     * String weatherId;
     * int cityId;
     * {"id":938,"name":"常熟","weather_id":"CN101190402"}
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){//先判断返回的数据不为空
            try {
                JSONArray allCounties = new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
