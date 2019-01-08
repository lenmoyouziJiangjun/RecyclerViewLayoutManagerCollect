package com.lll.layoutmanager.fanlayoutmanager

import android.view.View

/**
 * Version 1.0
 * Created by lll on 2018/12/29.
 * Description
 * copyright generalray4239@gmail.com
 */
data class ViewAnimationInfo(var view: View,var startLeft: Int,
                             var startRight: Int,
                             var top: Int,
                             var bottom: Int
                             ) {

    var finishRight: Int = 0
    var finishLeft: Int = 0

    constructor( view: View, startLeft: Int,
                 startRight: Int,
                 top: Int,
                 bottom: Int,
                 finishLeft:Int,
                 finishRight:Int):this(view,startLeft,startRight,top,bottom){
        this.finishLeft = finishLeft
        this.finishRight = finishRight
    }


}