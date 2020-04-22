package com.lll.layoutmanager.fanlayoutmanager

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

object ViewAnimationInfoGenerator {


    /**
     * Generate collection for open/close or shift animations.
     *
     * @param delta         delta x (shift distance) for views
     * @param isSelected    flag if have selected item
     * @param layoutManager the layout manager
     * @param centerViewPos the center view position
     * @param isCollapsed   flag if have collapsed items
     * @return collection of view data
     */
    @JvmStatic
    fun generate(delta: Int, isSelected: Boolean, @NonNull layoutManager: RecyclerView.LayoutManager, centerViewPos: Int, isCollapsed: Boolean): Collection<ViewAnimationInfo> {
        val infoViews: MutableList<ViewAnimationInfo> = mutableListOf() //定义一个可变集合
        if (centerViewPos == RecyclerView.NO_POSITION) {
            return infoViews
        }
        // +++++ prepare data +++++
        var view: View
        var viewPosition: Int
        var info: ViewAnimationInfo
        var isSelectedKoef: Int
        var collapseKoef: Int
        // ----- prepare data -----
        var count:Int =layoutManager.getChildCount()
        for(i in 0 .. count){
            view = layoutManager.getChildAt(i) !!
            viewPosition = layoutManager.getPosition(view)
            if(viewPosition == centerViewPos){
                continue
            }
            info = ViewAnimationInfo(view,layoutManager.getDecoratedLeft(view),layoutManager.getDecoratedRight(view),
                    layoutManager.getDecoratedTop(view),layoutManager.getDecoratedBottom(view) )
            if(viewPosition < centerViewPos){
                // left view

                // show views with overlapping if have selected item.
                isSelectedKoef = if (isSelected) -1 else 1
                // make distance between each item if isCollapsed = true
                collapseKoef = if (isCollapsed) centerViewPos - viewPosition else 1

                info.finishLeft = info.startLeft + delta * isSelectedKoef * collapseKoef
                info.finishRight = info.startRight + delta * isSelectedKoef * collapseKoef
            }else{
                // right view

                // show views with overlapping if have selected item.
                isSelectedKoef = if (isSelected) 1 else -1

                // make distance between each item if isCollapsed = true
                collapseKoef = if (isCollapsed) viewPosition - centerViewPos else 1

                info.finishLeft = info.startLeft + delta * isSelectedKoef * collapseKoef
                info.finishRight = info.startRight + delta * isSelectedKoef * collapseKoef
            }
            infoViews.add(i,info) //可变集合才有添加删除功能
        }
        return infoViews
    }
}