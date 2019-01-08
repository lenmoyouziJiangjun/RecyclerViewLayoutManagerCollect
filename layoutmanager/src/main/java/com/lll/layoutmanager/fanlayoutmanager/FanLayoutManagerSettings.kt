package com.lll.layoutmanager.fanlayoutmanager

import android.content.Context


private const val BOUNCE_MAX: Float = 10F
private const val DEFAULT_VIEW_WIDTH_DP: Float = 120f
private const val DEFAULT_VIEW_HEIGHT_DP: Float = 160f

class FanLayoutManagerSettings(builder: Builder) {

    private val mViewWidthDp: Float
    private val mViewHeightDp: Float
    private val mViewWidthPx: Int
    private val mViewHeightPx: Int
    private val mIsFanRadiusEnable: Boolean
    private val mAngleItemBounce: Float

    init {
        this.mViewWidthDp = builder.mViewWidthDp
        this.mViewHeightDp = builder.mViewHeightDp
        this.mViewWidthPx = builder.mViewWidthPx
        this.mViewHeightPx = builder.mViewHeightPx
        this.mIsFanRadiusEnable = builder.mIsFanRadiusEnable
        this.mAngleItemBounce = builder.mAngleItemBounce
    }

    companion object {
        fun newBuilder(context: Context): Builder {
            return Builder(context)
        }
    }


    internal fun getViewWidthDp(): Float {
        return mViewWidthDp
    }

    internal fun getViewHeightDp(): Float {
        return mViewHeightDp
    }

    internal fun isFanRadiusEnable(): Boolean {
        return mIsFanRadiusEnable
    }

    internal fun getAngleItemBounce(): Float {
        return mAngleItemBounce
    }

    internal fun getViewWidthPx(): Int {
        return mViewWidthPx
    }

    internal fun getViewHeightPx(): Int {
        return mViewHeightPx
    }


    class Builder(val mContext: Context) {
        var mViewWidthDp: Float = 0f
        var mViewHeightDp: Float = 0f
        var mIsFanRadiusEnable: Boolean = false
        var mAngleItemBounce: Float = 0f
        var mViewWidthPx: Int = 0
        var mViewHeightPx: Int = 0

        /**
         * Sets the `mViewWidthDp` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param viewWidthDp the `mViewWidthDp` to set
         * @return a reference to this Builder
         */
        fun withViewWidthDp(viewWidthDp: Float): Builder {
            mViewWidthDp = viewWidthDp
            mViewWidthPx = Math.round(mContext.getResources().getDisplayMetrics().density * viewWidthDp)
            mViewWidthPx = Math.min(mContext.getResources().getDisplayMetrics().widthPixels, mViewWidthPx)
            return this
        }

        /**
         * Sets the `mViewHeightDp` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param viewHeightDp the `mViewHeightDp` to set
         * @return a reference to this Builder
         */
        fun withViewHeightDp(viewHeightDp: Float): Builder {
            mViewHeightDp = viewHeightDp
            mViewHeightPx = Math.round(mContext.getResources().getDisplayMetrics().density * viewHeightDp)
            mViewHeightPx = Math.min(mContext.getResources().getDisplayMetrics().heightPixels, mViewHeightPx)
            return this
        }

        /**
         * Sets the `fanRadius` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param isFanRadiusEnable the `mIsFanRadiusEnable` to set
         * @return a reference to this Builder
         */
        fun withFanRadius(isFanRadiusEnable: Boolean): Builder {
            mIsFanRadiusEnable = isFanRadiusEnable
            return this
        }

        /**
         * Sets the `mAngleItemBounce` and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param angleItemBounce the `mAngleItemBounce` to set in range 0f...10f
         * @return a reference to this Builder
         */
        fun withAngleItemBounce(angleItemBounce: Float): Builder {
            if (angleItemBounce <= 0f) {
                return this
            }
            mAngleItemBounce = Math.min(BOUNCE_MAX, angleItemBounce)
            return this
        }

        fun build(): FanLayoutManagerSettings {
            if (java.lang.Float.compare(mViewWidthDp, 0f) == 0) {
                withViewWidthDp(DEFAULT_VIEW_WIDTH_DP)
            }
            if (java.lang.Float.compare(mViewHeightDp, 0f) == 0) {
                withViewHeightDp(DEFAULT_VIEW_HEIGHT_DP)
            }
            return FanLayoutManagerSettings(this)
        }


    }
}