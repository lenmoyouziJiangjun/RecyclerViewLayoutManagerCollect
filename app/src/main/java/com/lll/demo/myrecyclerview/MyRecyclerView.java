package com.lll.demo.myrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Stack;

public class MyRecyclerView extends ViewGroup {

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * @param <T>
     */
    public abstract static class Adapter<T extends ViewHolder> {

        int getViewTypeByPosition(int position) {
            return 0;
        }

        abstract ViewHolder createViewHolder(ViewGroup parent, int viewType);

        abstract void bindViewHolder(ViewHolder holder, int position);
    }

    /**
     * 自定义ViewHolder
     * 1、涉及到ViewHolder 的状态：remove、invalid、update、
     * 2、layoutPosition
     * 3、id
     * 4、item view type
     */
    public abstract static class ViewHolder {
        View itemView;

        int mFlags;

        int mPosition;

        public ViewHolder(View view) {
            if (view != itemView) {
                itemView = view;
            }
        }
    }


    /**
     * 缓存池管理
     */
    public static class Recycler {

        /**
         * 用于存储每一种类型的缓存View
         */
        Stack<View>[] mViewStacks;

        int mDefaultCacheSize;

        /**
         * @param type 类型
         */
        public Recycler(int type) {
            for (int i = 0; i < type; i++) {
                mViewStacks = new Stack[mDefaultCacheSize];
            }
        }


        public View getViewByType(int type) {
            return mViewStacks[type].pop();
        }

        public void putView(View view, int type) {
            mViewStacks[type].push(view);
        }

    }
}
