package com.lll.layoutmanager.swipelayoutmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Version 1.0
 * Created by lll on 2018/12/3.
 * Description
 * <pre>
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class SwipeTouchLayout extends FrameLayout {

    private boolean mFirstMove;
    private float mDownX;
    private float mDownY;

    SwipeTouchListener mSwipeTouchListener;

    public SwipeTouchLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeTouchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeTouchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setSwipeTouchListener(SwipeTouchListener swipeTouchListener) {
        mSwipeTouchListener = swipeTouchListener;
    }

    public interface SwipeTouchListener {
        void onTouchDown(MotionEvent event);

        void onTouchUp(MotionEvent event);

        void onTouchMove(View v, MotionEvent event);

    }

}
