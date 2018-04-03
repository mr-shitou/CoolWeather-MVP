package com.example.administrator.coolweather_mvp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.coolweather_mvp.weather.ChooseAreaFragment;
import com.example.administrator.coolweather_mvp.weather.ChooseAreaPresenter;
import com.example.administrator.coolweather_mvp.weather.WeatherActivity;

public class MainActivity extends AppCompatActivity {
    private ChooseAreaPresenter mChooseAreaPresenter;
    private ChooseAreaFragment chooseAreaFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例一个Presenter
        chooseAreaFragment = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);
        mChooseAreaPresenter = new ChooseAreaPresenter(chooseAreaFragment, this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            //Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
