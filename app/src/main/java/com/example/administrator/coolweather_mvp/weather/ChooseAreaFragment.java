package com.example.administrator.coolweather_mvp.weather;

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
                        /*Intent intent = new Intent(getActivity(),Main2Activity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();*/
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
            }
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

}
