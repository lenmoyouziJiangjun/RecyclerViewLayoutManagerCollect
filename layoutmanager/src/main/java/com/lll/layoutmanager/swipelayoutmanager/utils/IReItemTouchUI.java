package com.lll.layoutmanager.swipelayoutmanager.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Version 1.0
 * Created by lll on 2018/12/3.
 * Description
 * <p>
 * copyright generalray4239@gmail.com
 */
public interface IReItemTouchUI {

    void onDraw(Canvas c, RecyclerView recyclerView, View view,
                float dX, float dY, int actionState, boolean isCurrentlyActive);


    void onDrawOver(Canvas c, RecyclerView recyclerView, View view,
                    float dX, float dY, int actionState, boolean isCurrentlyActive);


    void clearView(View view);


    void onSelected(View view);


}
