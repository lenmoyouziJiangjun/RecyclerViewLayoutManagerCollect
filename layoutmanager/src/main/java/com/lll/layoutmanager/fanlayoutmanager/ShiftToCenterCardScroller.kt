package com.lll.layoutmanager.fanlayoutmanager

import android.content.Context
import android.util.DisplayMetrics
import android.view.View

/**
 * Version 1.0
 * Created by lll on 2019/1/7.
 *
 * Description 
 * copyright generalray4239@gmail.com
 */

private const val MILLISECONDS_PER_INCH:Float = 400f

class ShiftToCenterCardScroller(context: Context) : BaseSmoothScroller(context) {


    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
        val layoutManager = layoutManager
        return if (layoutManager != null) {
            // add to calculated dx offset. Need to scroll to center of RecyclerView.
            super.calculateDxToMakeVisible(view, snapPreference) + layoutManager.width / 2 - view.width / 2
        } else {
            // no layoutManager detected - not expected case.
            super.calculateDxToMakeVisible(view, snapPreference)
        }
    }

    protected override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
    }

}