package com.example.administrator.coolweather_mvp.Weather;

import android.app.Activity;

import com.example.administrator.coolweather_mvp.BasePresenter;
import com.example.administrator.coolweather_mvp.BaseView;
import com.example.administrator.coolweather_mvp.db.City;
import com.example.administrator.coolweather_mvp.db.County;
import com.example.administrator.coolweather_mvp.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/1.
 * 这个接口主类可以很好的知道V和P通信的接口方法，便于管理。
 * 编写程序时，先处理V中的逻辑，需要数据操作时，
 * 在P中创建接口方法，并在V中提供一个供调用的接口方法来改变界面。
 * V中使用普通方法来调用P中的接口
 */

public interface ChooseAreaContract {
    interface view extends BaseView<Presenter> {

        void showProvince();

        void showCities();

        void showCounties();

        void BeginqueryFromServer();
        void EndqueryFromServer();

        void VonItemClick();
    }

    interface Presenter extends BasePresenter {
        //void onItemClick(int position,List<String> dataList);
        //void backButtonOnClick(List<String> dataList);
        void queryProvince(List<String> dataList);

        void queryCities(List<String> dataList);

        void queryCounties( List<String> dataList);

        void queryFromeServer(String address, final String type,List<String> dataList);
        //void requestWeather(String weatherId);
        void getSelectItem (int level,int position);
        String  getSelectCounty(int level,int position);
    }
}
