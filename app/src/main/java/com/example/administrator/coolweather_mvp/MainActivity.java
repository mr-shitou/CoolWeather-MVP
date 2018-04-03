package com.example.administrator.coolweather_mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.coolweather_mvp.Weather.ChooseAreaFragment;
import com.example.administrator.coolweather_mvp.Weather.ChooseAreaPresenter;

public class MainActivity extends AppCompatActivity {
    private ChooseAreaPresenter mChooseAreaPresenter;
    private ChooseAreaFragment chooseAreaFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseAreaFragment = (ChooseAreaFragment) getSupportFragmentManager().findFragmentById(R.id.choose_area_fragment);
        mChooseAreaPresenter = new ChooseAreaPresenter(chooseAreaFragment,this);
    }
}
