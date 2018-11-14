package com.lll.demo;

import android.app.Application;

/**
 * Version 1.0
 * Created by lll on 2018/11/14.
 * Description
 * copyright generalray4239@gmail.com
 */
public class LayoutManagerDemoApplication extends Application {

    public static LayoutManagerDemoApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
