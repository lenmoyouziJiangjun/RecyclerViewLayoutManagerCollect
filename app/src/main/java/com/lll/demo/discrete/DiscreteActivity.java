package com.lll.demo.discrete;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lll.demo.DemoBaseActivity;
import com.lll.demo.discrete.gallery.Gallery;
import com.lll.demo.discrete.gallery.GalleryActivity;
import com.lll.demo.discrete.shop.DiscreteScrollViewOptions;
import com.lll.demo.discrete.shop.ShopActivity;
import com.lll.demo.discrete.weather.WeatherActivity;
import com.lll.layoutmanager.demo.R;

/**
 * Version 1.0
 * Created by lll on 2018/11/12.
 * Description
 * <pre>
 *   Discrete LayoutManager
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class DiscreteActivity extends DemoBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discrete);
        DiscreteScrollViewOptions.init(this);
    }


    public void showShop(View view) {
        startDemoActivity(ShopActivity.class);
    }

    public void showSmall(View view) {
        startDemoActivity(WeatherActivity.class);
    }

    public void showVertical(View view) {
        startDemoActivity(GalleryActivity.class);
    }


}
