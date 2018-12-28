package com.criusker.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.criusker.coolweather.db.City;
import com.criusker.coolweather.db.County;
import com.criusker.coolweather.db.Province;
import com.criusker.coolweather.util.HttpUtil;
import com.criusker.coolweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Create by 李菀直 on 2018/12/26.
 */
public class ChooseAreaFragment extends Fragment {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    //当前选中的级别
    private int currentLevel;

    //控件
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ProgressBar progressbar;

    private ArrayAdapter<String> adapter;

    //省市县列表
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    //选中的省市
    private Province selectedProvince;
    private City selectedCity;

    private List<String> dataList = new ArrayList<>();


    //onCreateView：加载布局
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        progressbar = view.findViewById(R.id.progressbar);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    //onActivityCreated：处理逻辑
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    //获取选中的省
                    selectedProvince = provinceList.get(position);
                    //查询选中省的市
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    //获取选中的市
                    selectedCity = cityList.get(position);
                    //查询选中市内的县
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();//获取weatherId
                    if(getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);//点击县跳转到WeatherActivity并将weather_id传入
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swpieRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                        activity.requestAir(weatherId);
                    }

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    /**
     * 查询所有的省 优先从数据库查询 如果没有再从服务器上查询
     * notifyDataSetChanged():通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
     */
    private void queryProvince(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);//查询Province表
        if (provinceList.size()>0){//如果有查询内容
            dataList.clear();//先清空dataList
            for (Province province:provinceList){//遍历provinceList
                dataList.add(province.getProvinceName());//将每一条查询出来的ProvinceName添加到dataList
            }
            adapter.notifyDataSetChanged();//刷新内容
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {//没有查询内容则从服务器上获取
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }
    /**
     * 查询选中省内所有的市 优先从数据库查询 如果没有再从服务器上查询
     */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);//查询City表
        if (cityList.size()>0){//如果有查询内容
            dataList.clear();//先清空dataList
            for (City city:cityList){//遍历cityList
                dataList.add(city.getCityName());//将每一条查询出来的CityName添加到dataList
            }
            adapter.notifyDataSetChanged();//刷新内容
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {//没有查询内容则从服务器上获取
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
    /**
     * 查询选中市内所有的县 优先从数据库查询 如果没有再从服务器上查询
     */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);//查询County表
        if (countyList.size()>0){//如果有查询内容
            dataList.clear();//先清空dataList
            for (County county:countyList){//遍历countyList
                dataList.add(county.getCountyName());//将每一条查询出来的CityName添加到dataList
            }
            adapter.notifyDataSetChanged();//刷新内容
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {//没有查询内容则从服务器上获取
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address, final String Type){
        progressbar.setVisibility(View.VISIBLE);
        HttpUtil.sendOKHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                //根据不同的Type值将数据存入相应的表中
                if("province".equals(Type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(Type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(Type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){//若result为true 说明已将数据获取到本地
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setVisibility(View.GONE);
                            if("province".equals(Type)){
                                queryProvince();
                            }else if ("city".equals(Type)){
                                queryCities();
                            }else if("county".equals(Type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
