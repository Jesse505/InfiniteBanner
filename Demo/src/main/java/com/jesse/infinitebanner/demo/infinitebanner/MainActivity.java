package com.jesse.infinitebanner.demo.infinitebanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.jesse.infinitebanner.InfiniteBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    InfiniteBanner infiniteBanner;
    CheckBox cb_loop, cb_autoTurn, cb_touchScroll;
    private RadioGroup radioGroup6;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_banner);
        infiniteBanner = (InfiniteBanner) findViewById(R.id.commonBanner);
        cb_loop = (CheckBox) findViewById(R.id.cb_loop);
        cb_autoTurn = (CheckBox) findViewById(R.id.cb_autoTurn);
        cb_touchScroll = (CheckBox) findViewById(R.id.cb_touchScroll);
        radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        radioGroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        radioGroup6 = (RadioGroup) findViewById(R.id.radioGroup6);
        initCustomBanner();

        cb_loop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                infiniteBanner.setIsNeedShowIndicatorOnOnlyOnePage(isChecked);
            }
        });
        cb_autoTurn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                infiniteBanner.setAutoPlayAble(isChecked);
            }
        });
        cb_touchScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                infiniteBanner.setAllowUserScrollable(isChecked);
            }
        });

        radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.data:
                        initCustomBanner();
                        break;
                    case R.id.data2:
                        updateCustomBanner();
                        break;
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.time_01:
                        infiniteBanner.setAutoPlayInterval(3000);
                        break;
                    case R.id.time_02:
                        infiniteBanner.setAutoPlayInterval(8000);
                        break;
                }
            }
        });
        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.speeed_01:
                        infiniteBanner.setPageChangeDuration(800);
                        break;
                    case R.id.speeed_02:
                        infiniteBanner.setPageChangeDuration(2000);
                        break;
                }
            }
        });
    }

    private void initCustomBanner() {
        List<Integer> homeSplashResourceList = new ArrayList<>();
        homeSplashResourceList.add(R.mipmap.home_splash_page_1);
        homeSplashResourceList.add(R.mipmap.home_splash_page_2);
        homeSplashResourceList.add(R.mipmap.home_splash_page_3);
        infiniteBanner.setAdapter(new HomePageTopViewPagerAdapter(homeSplashResourceList, this));
    }

    private void updateCustomBanner() {
        List<Integer> homeSplashResourceList = new ArrayList<>();
        homeSplashResourceList.add(R.mipmap.home_splash_page_1);
        infiniteBanner.setAdapter(new HomePageTopViewPagerAdapter(homeSplashResourceList, this));
    }
}
