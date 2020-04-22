package com.lll.layoutmanager.fanlayoutmanager

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * Version 1.0
 * Created by lll on 2018/12/29.
 * Description
 * <pre>
 *
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
open class BaseSmoothScroller(context: Context) : LinearSmoothScroller(context) {


    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        val layoutManager: RecyclerView.LayoutManager? = getLayoutManager()
        if (null != layoutManager && layoutManager is FanlayoutManager) {
            if (childCount == 0) {
                return null
            }

            val firstChildPos = layoutManager.getPosition(layoutManager.getChildAt(0)!!)
            val direction = if (targetPosition < firstChildPos) -1f else 1f
            return PointF(direction, 0f)

        }
        return PointF()
    }
}