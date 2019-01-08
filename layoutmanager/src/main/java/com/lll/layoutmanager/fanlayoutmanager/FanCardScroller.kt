package com.lll.layoutmanager.fanlayoutmanager

import android.content.Context
import android.util.DisplayMetrics
import android.view.View

/**
 * Version 1.0
 * Created by lll on 2019/1/4.
 * Description
 * <pre>
 *     LinearSmoothScroller for switch views.
 * </pre>
 * copyright generalray4239@gmail.com
 */

//TODO Need to change this to make it more flexible.
private const val MILLISECONDS_PER_INCH: Float = 200f

class FanCardScroller(context: Context) : BaseSmoothScroller(context) {

    /**
     * 延迟初始化
     */
    lateinit var mCardTimeCallback: FanCardTimeCallback

    interface FanCardTimeCallback {
        /**
         * @param targetPosition item position to scroll to
         * @param time           scroll duration
         */
        open fun onTimeForScrollingCalculated(targetPosition: Int, time: Int)
    }

    fun setCardTimeCallback(callback: FanCardTimeCallback){
        this.mCardTimeCallback = callback
    }

    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
        if (layoutManager == null) {
            // add to calculated dx offset. Need to scroll to center of RecyclerView.
            return super.calculateDxToMakeVisible(view, snapPreference) + layoutManager!!.width / 2 - view!!.width/ 2
        } else run {
            // no layoutManager detected - not expected case. can be magic or end of the world...
            return super.calculateDxToMakeVisible(view, snapPreference)
        }
    }

    override fun calculateTimeForScrolling(dx: Int): Int {
        var time = super.calculateTimeForScrolling(dx)
        if(mCardTimeCallback != null){
            mCardTimeCallback.onTimeForScrollingCalculated(getTargetPosition(), time)
        }
        return time
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
        return MILLISECONDS_PER_INCH / displayMetrics!!.densityDpi
    }


}