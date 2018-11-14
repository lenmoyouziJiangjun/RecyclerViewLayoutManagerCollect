package com.lll.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class DemoBaseActivity extends AppCompatActivity {

    public void startDemoActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}
