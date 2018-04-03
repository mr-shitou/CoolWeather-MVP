package com.example.administrator.coolweather_mvp.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather_mvp.R;
import com.example.administrator.coolweather_mvp.db.City;
import com.example.administrator.coolweather_mvp.db.County;
import com.example.administrator.coolweather_mvp.db.Province;
import com.example.administrator.coolweather_mvp.gson.Forecast;
import com.example.administrator.coolweather_mvp.gson.Weather;
import com.example.administrator.coolweather_mvp.service.AutoUpdateService;
import com.example.administrator.coolweather_mvp.util.HttpUtil;
import com.example.administrator.coolweather_mvp.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.administrator.coolweather_mvp.weather.ChooseAreaFragment.LEVEL_CITY;
import static com.example.administrator.coolweather_mvp.weather.ChooseAreaFragment.LEVEL_PROVINCE;
import static org.litepal.LitePalApplication.getContext;

/**
 * Created by Administrator on 2018/4/1.
 */

public class ChooseAreaPresenter implements ChooseAreaContract.Presenter {
    private final ChooseAreaContract.view mView;
    private Activity activity;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    public static  Province selectedProvince;
    public static City selectedCity;
    SharedPreferences pref;  //= PreferenceManager.getDefaultSharedPreferences(this);
    private String mweatherId;

    public ChooseAreaPresenter(@NonNull ChooseAreaContract.view View, Activity activity) {
        //传递View和Activity过来
        this.activity = activity;
        mView = View;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void queryProvince( List<String> dataList) {
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mView.showProvince();
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromeServer(address, "province",dataList);
        }
    }

    @Override
    public void queryCities( List<String> dataList) {
        cityList = DataSupport.where("provinceid = ?", String.valueOf(
                selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mView.showCities();
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromeServer(address, "city", dataList);
        }

    }

    @Override
    public void queryCounties(List<String> dataList) {
        countyList = DataSupport.where("cityid = ?", String.valueOf(
                selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mView.showCounties();
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromeServer(address, "county", dataList);
        }

    }

    @Override
    public void queryFromeServer(String address, final String type, final List<String> dataList) {
        mView.BeginqueryFromServer();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.EndqueryFromServer();
                            if ("province".equals(type)) {
                                queryProvince(dataList);
                            }else if ("city".equals(type)) {
                                queryCities(dataList);
                            }else if ("county".equals(type)) {
                                queryCounties(dataList);
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.EndqueryFromServer();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void getSelectItem(int level, int position) {
        switch (level) {
            case LEVEL_PROVINCE:
                selectedProvince = provinceList.get(position);
                break;
            case LEVEL_CITY:
                selectedCity = cityList.get(position);
               break;

        }

    }

    @Override
    public String getSelectCounty(int level, int position) {
        return countyList.get(position).getWeatherId();

    }
}
