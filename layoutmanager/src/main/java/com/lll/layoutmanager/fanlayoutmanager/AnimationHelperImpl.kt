package com.lll.layoutmanager.fanlayoutmanager

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.DecelerateInterpolator


private const val ANIMATION_VIEW_SCALE_FACTOR: Float = 1.5f

//base duration for open animation
private const val ANIMATION_SINGLE_OPEN_DURATION: Int = 300

// base duration for close animation
private const val ANIMATION_SINGLE_CLOSE_DURATION: Int = 300

// base duration for shift animation
private const val ANIMATION_SHIFT_VIEWS_DURATION: Int = 200

// base threshold duration for shift animation
private const val ANIMATION_SHIFT_VIEWS_DELAY_THRESHOLD: Int = 50

// base threshold duration for open/close animation (bounce effect)
private const val ANIMATION_VIEW_SCALE_FACTOR_THRESHOLD: Float = 0.4F

/**
 * Version 1.0
 * Created by lll on 2019/1/1.
 * Description
 *
 *
 * copyright generalray4239@gmail.com
 */
class AnimationHelperImpl : AnimationHelper {


    override fun openItem(view: View, delay: Int, animatorListener: Animator.AnimatorListener) {
        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, ANIMATION_VIEW_SCALE_FACTOR + ANIMATION_VIEW_SCALE_FACTOR_THRESHOLD)
        valueAnimator.addUpdateListener {
            var value: Float = it.animatedValue as Float
            if (value < 1F + ANIMATION_VIEW_SCALE_FACTOR_THRESHOLD / 2) {
                value = Math.abs(value - 2f)
            } else {
                value -= ANIMATION_VIEW_SCALE_FACTOR_THRESHOLD
            }
            scaleView(view, value)
        }

        valueAnimator.startDelay = delay.toLong()
        valueAnimator.duration = (ANIMATION_SINGLE_OPEN_DURATION.toLong())
        valueAnimator.addListener(animatorListener)
        valueAnimator.start()
    }

    private fun scaleView(view: View, value: Float) {
        view.pivotX = (view.width / 2).toFloat()
        view.pivotY = view.height.toFloat()
    }

    override fun closeItem(view: View, delay: Int, animatorListener: Animator.AnimatorListener) {
        val valueAnimator = ValueAnimator.ofFloat(ANIMATION_VIEW_SCALE_FACTOR, 1f)
        valueAnimator.addUpdateListener {
            val value = valueAnimator.animatedValue
            scaleView(view, value as Float)
        }

        valueAnimator.startDelay = delay.toLong()
        valueAnimator.duration = ANIMATION_SINGLE_CLOSE_DURATION.toLong()
        valueAnimator.addListener(animatorListener)
        valueAnimator.start()
    }

    override fun shiftSideViews(viewAnimations: Collection<ViewAnimationInfo>, delay: Int, layoutManager: RecyclerView.LayoutManager, animatorListener: Animator.AnimatorListener, animatorUpdateListener: ValueAnimator.AnimatorUpdateListener) {
        var bounceAnimator = ValueAnimator.ofFloat(0f, 1f)

        bounceAnimator.addUpdateListener { animator: ValueAnimator? ->
            var value: Float = animator!!.animatedValue as Float
            for (viewAnimation in viewAnimations) {

                // left offset for view for current update value
                val left = (viewAnimation.startLeft + value * (viewAnimation.finishLeft - viewAnimation.startLeft)).toInt()

                // right offset for view for current update value
                var right = (viewAnimation.startRight + value * (viewAnimation.finishRight - viewAnimation.startRight)).toInt()

                // update view with new params
                layoutManager.layoutDecorated(viewAnimation.view, left, viewAnimation.top, right, viewAnimation.bootom)
            }

            if (animatorUpdateListener != null) {
                animatorUpdateListener.onAnimationUpdate(animator)
            }
        }

        bounceAnimator.duration = ANIMATION_SHIFT_VIEWS_DURATION.toLong()
        bounceAnimator.startDelay = delay + ANIMATION_SHIFT_VIEWS_DELAY_THRESHOLD.toLong()

        if (animatorListener != null) {
            bounceAnimator.addListener(animatorListener)
        }
        bounceAnimator.start()
    }

    override fun getViewScaleFactor(): Float {
        return ANIMATION_VIEW_SCALE_FACTOR
    }

    override fun straightenView(view: View, listener: Animator.AnimatorListener) {
        if (view != null) {
            val viewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", view.rotation, 0f)
            viewObjectAnimator.duration = 150
            viewObjectAnimator.interpolator = DecelerateInterpolator()
            if (listener != null) {
                viewObjectAnimator.addListener(listener)
            }
            viewObjectAnimator.start()
        }
    }


    override fun rotateView(view: View, angle: Float, animatorListener: Animator.AnimatorListener) {
        if (view != null) {
            val viewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", view.rotation, angle)
            viewObjectAnimator.duration = 150
            viewObjectAnimator.interpolator = DecelerateInterpolator()
            if (animatorListener != null) {
                viewObjectAnimator.addListener(animatorListener)
            }
            viewObjectAnimator.start()
        }
    }
}