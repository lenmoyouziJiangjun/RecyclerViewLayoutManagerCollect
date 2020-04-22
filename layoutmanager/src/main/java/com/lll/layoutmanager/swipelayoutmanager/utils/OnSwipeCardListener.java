package com.lll.layoutmanager.swipelayoutmanager.utils;


import androidx.recyclerview.widget.RecyclerView;

public interface OnSwipeCardListener<T> {
    void onSwiping(RecyclerView.ViewHolder viewHolder, float dx, float dy, int direction);


    void onSwipedOut(RecyclerView.ViewHolder viewHolder, T t, int direction);


    void onSwipedClear();

}
