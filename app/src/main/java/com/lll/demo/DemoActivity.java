package com.lll.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lll.demo.cardlayoutmanager.CardLayoutManagerActivity;
import com.lll.demo.carouselayoutmanager.CarouseLayoutManagerActivity;
import com.lll.demo.discrete.DiscreteActivity;
import com.lll.demo.fanlayoutmanager.FanlayoutManagerActivity;
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

    public void showCardLayout(View view) {
        startDemoActivity(CardLayoutManagerActivity.class);
    }

    public void showFanLayout(View view) {
        startDemoActivity(FanlayoutManagerActivity.class);
    }
}
