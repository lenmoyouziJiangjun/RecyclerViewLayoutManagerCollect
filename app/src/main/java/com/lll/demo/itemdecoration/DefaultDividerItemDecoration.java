package com.lll.demo.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Version 1.0
 * Created by lll on 2020/4/21.
 * Description
 * <pre>
 *     默认背景的分割线
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class DefaultDividerItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * item之间的间距
     */
    private int itemMargin;

    /**
     * @param itemSpace
     */
    public DefaultDividerItemDecoration(Context context, int itemSpace) {
        itemMargin = TypedValue.complexToDimensionPixelOffset(itemSpace, context.getResources().getDisplayMetrics());
    }

    /**
     * itemView之前绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    /**
     * itemView 之后绘制，覆盖在itemView只上
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 设置itemView的偏移量，如果有
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            int spanCount = manager.getSpanCount();
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if (itemPosition > 0) {
                outRect.top = itemMargin;
                if (itemPosition % spanCount == 0) {//left
                    outRect.left = itemMargin;
                    outRect.right = itemMargin / 2;
                } else if (itemPosition % spanCount == spanCount - 1) {//right
                    outRect.left = itemMargin / 2;
                    outRect.right = itemMargin;
                } else {//middle
                    outRect.left = itemMargin / 2;
                    outRect.right = itemMargin / 2;
                }
                outRect.bottom = 0;
            }

        } else if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, itemMargin);
            } else {
                outRect.set(0, 0, itemMargin, 0);
            }
        }


    }
}
