package com.example.administrator.coolweather_mvp.Weather;

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
import android.widget.TextView;

import com.example.administrator.coolweather_mvp.MainActivity;
import com.example.administrator.coolweather_mvp.R;
import com.example.administrator.coolweather_mvp.db.City;
import com.example.administrator.coolweather_mvp.db.County;
import com.example.administrator.coolweather_mvp.db.Province;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * V中只处理视图相关操作
 */
public class ChooseAreaFragment extends Fragment implements ChooseAreaContract.view {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private int currentLevel;
    private ChooseAreaContract.Presenter mPresenter;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    public static  Province selectedProvince;
    public static City selectedCity;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_area,container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    mPresenter.getSelectItem(currentLevel,position);
                    VqueryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    mPresenter.getSelectItem(currentLevel,position);
                    VqueryCounties();
                }else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = mPresenter.getSelectCounty(currentLevel,position);
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }/*else if (activity instanceof WeatherActivity) {
                mView.VonItemClick();
                requestWeather(weatherId);
            }*/
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    VqueryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    VqueryProvince();
                }
            }
        });
        VqueryProvince();
    }




    @Override
    public void setPresenter(ChooseAreaContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void VqueryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        mPresenter.queryProvince(dataList);
    }
    public void VqueryCities() {
        titleText.setText(ChooseAreaPresenter.selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        mPresenter.queryCities(dataList);

    }
    public void VqueryCounties() {
        titleText.setText(ChooseAreaPresenter.selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        mPresenter.queryCounties(dataList);
    }


    @Override
    public void showProvince() {
        changeListView(LEVEL_PROVINCE);
    }

    @Override
    public void showCities() {
        changeListView(LEVEL_CITY);
    }

    @Override
    public void showCounties() {
        changeListView(LEVEL_COUNTY);
    }


    public void changeListView(int CurrentLevel) {
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = CurrentLevel;
    }

    @Override
    public void EndqueryFromServer() {
        closeProgressDialog();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }

    @Override
    public void BeginqueryFromServer() {
     showProgressDialog();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void VonItemClick() {
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        //weatherActivity.drawerLayout.closeDrawers();
        //weatherActivity.swipeRefresh.setRefreshing(true);
    }


    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     *//*
    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromeServer(address, "province");
        }
    }
    *//**
     * 查询市级数据
     *//*
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(
                selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromeServer(address, "city");
        }
    }

    *//**
     * 查询选中市中的所有县
     *//*
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(
                selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromeServer(address, "county");
        }
    }

    *//**
     * 根据传入的地址和类型从服务器查询数据
     *
     *//*
    private void queryFromeServer(String address, final String type) {
        showProgressDialog();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            }else if ("city".equals(type)) {
                                queryCities();
                            }else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
  }*/


}
