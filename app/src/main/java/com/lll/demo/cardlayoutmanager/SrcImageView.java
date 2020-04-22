package com.lll.demo.cardlayoutmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Version 1.0
 * Created by lll on 2020/4/22.
 * Description
 * copyright generalray4239@gmail.com
 */

public class SrcImageView extends AppCompatImageView {
    public SrcImageView(Context context) {
        super(context);
    }

    public SrcImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SrcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result=super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.e("SrcImageView", "dispatchTouchEvent=ACTION_MOVE  " + result);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("SrcImageView", "dispatchTouchEvent=ACTION_UP  " + result);
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e("SrcImageView", "dispatchTouchEvent=ACTION_DOWN  " + result);
                break;
        }

        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result=super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
              return false;
            case MotionEvent.ACTION_UP:
                Log.e("SrcImageView", "onTouchEvent=ACTION_UP  " + result);
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e("SrcImageView", "onTouchEvent=ACTION_DOWN  " + result);
                break;
        }
        return result;
    }
}
