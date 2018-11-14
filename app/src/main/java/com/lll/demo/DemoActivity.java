package com.lll.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lll.demo.carouselayoutmanager.CarouseLayoutManagerActivity;
import com.lll.demo.discrete.DiscreteActivity;
import com.lll.layoutmanager.demo.R;

/**
 * Version 1.0
 * Created by lll on 2018/11/14.
 * Description
 * copyright generalray4239@gmail.com
 */
public class DemoActivity extends DemoBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void showCarouse(View view) {
        startDemoActivity(CarouseLayoutManagerActivity.class);
    }

    public void showDiscrete(View view) {
        startDemoActivity(DiscreteActivity.class);
    }


}
