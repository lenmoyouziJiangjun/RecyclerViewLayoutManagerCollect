package com.lll.layoutmanager.fanlayoutmanager.callback

import android.support.v7.widget.RecyclerView
import android.view.View
import java.lang.ref.WeakReference

/**
 *
 */
class FanChildDrawingOrderCallback(layoutManager: RecyclerView.LayoutManager) : RecyclerView.ChildDrawingOrderCallback {

    private val mLayoutManager: WeakReference<RecyclerView.LayoutManager>

    init {
        mLayoutManager = WeakReference(layoutManager)
    }

    /**
     *
     */
    override fun onGetChildDrawingOrder(childCount: Int, i: Int): Int {
        val layoutManager = mLayoutManager.get()
        if (null != layoutManager) {
            val startChild: View? = layoutManager.getChildAt(0)
            var position = layoutManager.getPosition(startChild!!)

            var isStartFromBelow = if (position % 2 == 0) true else false
            var result = 0
            when (isStartFromBelow) {
                true ->
                    if (i % 2 == 0) {
                        result = if (i == 0) 0 else i - 1
                    } else run {
                        result = if (i + 1 >= childCount) i else i + 1
                    }
                false ->
                    if (i % 2 == 0) {
                        result = if (i + 1 >= childCount) i else i + 1
                    } else run {
                        result = i - 1
                    }

            }
            return result
        }
        return i
    }
}