package com.lll.layoutmanager.fanlayoutmanager

import android.animation.Animator
import android.view.View
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.support.annotation.NonNull
import android.support.annotation.Nullable


/**
 * Version 1.0
 * Created by lll on 2018/12/29.
 * Description
 *
 * copyright generalray4239@gmail.com
 */
interface AnimationHelper {


    /**
     * Select view animation with start delay.
     *
     * @param view             view to scale (open)
     * @param delay            start delay duration
     * @param animatorListener select view animation listener
     */
    fun openItem(@NonNull view: View, delay: Int, animatorListener: Animator.AnimatorListener)

    /**
     * Deselect view animation with start delay
     *
     * @param view             view to scale (close)
     * @param delay            start delay
     * @param animatorListener deselect view animation listener
     */
    fun closeItem(@NonNull view: View, delay: Int, animatorListener: Animator.AnimatorListener)

    /**
     * Move views to sides or to the center of the screen.
     *
     * @param views                  view data information for shift animation {@link ViewAnimationInfo}
     * @param delay                  start delay
     * @param layoutManager          the layout manager
     * @param animatorListener       animator listener to check start or end animation
     * @param animatorUpdateListener value animator listener to check updates
     */
    fun shiftSideViews(@NonNull views: Collection<ViewAnimationInfo>, delay: Int, @NonNull layoutManager: RecyclerView.LayoutManager,
                       animatorListener: Animator.AnimatorListener?,
                       animatorUpdateListener: ValueAnimator.AnimatorUpdateListener?)

    /**
     * @return scale factor for select and deselect animation.
     */

    fun getViewScaleFactor(): Float

    /**
     * Rotate view from custom angle to 0.
     *
     * @param view     view to rotate
     * @param listener animator listener to check start or end animation
     */
    fun straightenView(view: View, @Nullable listener: Animator.AnimatorListener)


    /**
     * Rotate view from current to custom angle.
     *
     * @param view     view to rotate
     * @param angle   rotate angle
     * @param listener animator listener to check start or end animation
     */
    fun rotateView(view: View, angle: Float, @Nullable animatorListener: Animator.AnimatorListener)
}