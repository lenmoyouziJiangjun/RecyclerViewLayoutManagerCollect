package com.lll.demo;

import android.app.ActivityOptions;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class DemoBaseActivity extends AppCompatActivity {

    public void startDemoActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, null).toBundle());
    }
}
