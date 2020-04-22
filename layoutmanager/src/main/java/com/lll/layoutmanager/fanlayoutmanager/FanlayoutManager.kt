package com.lll.layoutmanager.fanlayoutmanager

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.NotNull
import java.util.*


/**
 * Version 1.0
 * Created by lll on 2019/1/7.
 * Description
 * Custom implementation of {@link RecyclerView.LayoutManager}
 * change view's position, rotation and translation to create effect fan scrolling.
 * <p>
 * How to use:
 * <p>
 * 1) Create object of class. You can use
 * {@link #Context)} with default settings
 * or
 * {@link #Context, ettings)} with custom settings.
 * See {@link ettings} to create custom settings.
 * <p>
 * 2) Set the to your RecyclerView. See {@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)}
 * <p>
 * 3) Use methods {@link #switchItem(RecyclerView, int)} ot select and deselect item.
 * <p>
 * 4) Use method {@link #collapseViews()} to collapse views.
 * <p>
 * 5) Use method {@link #straightenSelectedItem(Animator.AnimatorListener)} to straight selected view.
 * <p>
 * 6) Use method {@link #getSelectedItemPosition()} to get selected item position
 * <p>
 * 7) Use method {@link #isItemSelected()} to check if item is selected or not.
 *
 *
 * copyright generalray4239@gmail.com
 */
class FanlayoutManager(val context: Context, @Nullable settings: FanLayoutManagerSettings?) : RecyclerView.LayoutManager() {

    private var mSettings: FanLayoutManagerSettings

    /**
     * Map with view cache.
     */
    private val mViewCache = SparseArray<View>()
    /**
     * LinearSmoothScroller for switch views.
     */
    private var mFanCardScroller: FanCardScroller
    /**
     * LinearSmoothScroller to show view in the middle of the screen.
     */
    private var mShiftToCenterCardScroller: ShiftToCenterCardScroller

    /**
     * Just random ))
     */
    private val mRandom = Random()

    /**
     * Map with view (card) rotations. This need to save bounce rotations for views.
     * [.updateArcViewPositions]
     */
    private var mViewRotationsMap = SparseArray<Float>()

    /**
     * Helper module need to implement 'open','close', 'shift' views functionality.
     * By default using [AnimationHelperImpl]
     * Can be changed [.setAnimationHelper]
     */
    private var mAnimationHelper: AnimationHelper
    /**
     * Position of selected item in adapter. ADAPTER!!
     */
    private var mSelectedItemPosition = RecyclerView.NO_POSITION
    /**
     * Position of item we need to scroll to right now.
     */
    private var mScrollToPosition = RecyclerView.NO_POSITION
    /**
     * Need to block some events between smooth scroll and select item animation.
     * true before start smoothScroll to selected item
     * false after smooth scroll finished and after select animation is started.
     */
    private var mIsWaitingToSelectAnimation = false
    /**
     * Need to block some events while scaling view.
     * true right after smooth scroll finished scrolling.
     */
    private var mIsSelectAnimationInProcess = false

    /**
     * Need to block some events while deselecting item is preparing.
     */
    private var mIsWaitingToDeselectAnimation = false
    /**
     * Need to block some events.
     */
    private var mIsDeselectAnimationInProcess = false

    /**
     * Flag using to change bounce radius.
     */
    private var mIsSelectedItemStraightened = false

    /**
     * Need to block some events.
     */
    private var mIsSelectedItemStraightenedInProcess = false

    /**
     * Need to block some events while collapsing views.
     */
    private var mIsViewCollapsing = false

    /**
     * Saved state for layout manager.
     */
    private var mPendingSavedState: SavedState? = null
    /**
     * true if item selected
     */
    private var mIsSelected = false

    /**
     * true if views collapsed
     */
    private var mIsCollapsed = false

    /**
     * View in center of screen
     */
    private var mCenterView: View? = null

    init {
        mAnimationHelper = AnimationHelperImpl()

        mSettings = settings ?: FanLayoutManagerSettings.Builder(context).build()
        // create default animation helper
        mAnimationHelper = AnimationHelperImpl()
        // create default FanCardScroller
        mFanCardScroller = FanCardScroller(context)
        // set callback which return calculated scroll time
        mFanCardScroller.setCardTimeCallback(object : FanCardScroller.FanCardTimeCallback {
            override fun onTimeForScrollingCalculated(targetPosition: Int, time: Int) {
                // select item after scroll to item
                selectItem(targetPosition, time)
            }
        })
        // create default smooth scroller to show item in the middle of the screen
        mShiftToCenterCardScroller = ShiftToCenterCardScroller(context)
    }

    fun saveState() {
        mPendingSavedState = SavedState()
        // save center view position
        mPendingSavedState!!.mCenterItemPosition = findCurrentCenterViewPos()
        // save selected state for center view
        mPendingSavedState!!.isSelected = mIsSelected
        // save collapsed state for views
        mPendingSavedState!!.isCollapsed = mIsCollapsed
        // center view position
        mPendingSavedState!!.mRotation = mViewRotationsMap as SparseArray<Any>
    }

    override fun onSaveInstanceState(): Parcelable? {
        saveState()
        return mPendingSavedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state != null && state is SavedState) {
            mPendingSavedState = state
            // center view position
            mScrollToPosition = mPendingSavedState!!.mCenterItemPosition
            // position for selected item
            mSelectedItemPosition = if (mPendingSavedState!!.isSelected) mScrollToPosition else RecyclerView.NO_POSITION
            // selected state
            mIsSelected = mPendingSavedState!!.isSelected
            // collapsed state
            mIsCollapsed = mPendingSavedState!!.isCollapsed
            // rotation state
            mViewRotationsMap = mPendingSavedState!!.mRotation as SparseArray<Float>
        }
    }

    /**
     * @return selected item position
     */
    fun getSelectedItemPosition(): Int {
        return mSelectedItemPosition
    }


    /**
     * @return is selected item straightened or has base (bounce) rotation
     */
    fun isSelectedItemStraightened(): Boolean {
        return mIsSelectedItemStraightened
    }

    /**
     * Setter for custom animation helper
     *
     * @param animationHelper custom animation helper.
     */
    @Deprecated("")
    internal fun setAnimationHelper(animationHelper: AnimationHelper?) {
        mAnimationHelper = animationHelper ?: AnimationHelperImpl()
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        // find center view before detach or recycle all views
        mCenterView = findCurrentCenterView()
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler!!)
            return
        }
        detachAndScrapAttachedViews(recycler!!)
        fill(recycler)
    }

    /**
     * Method create or reuse views for recyclerView.
     *
     * @param recycler recycler from the recyclerView
     */
    private fun fill(recycler: RecyclerView.Recycler) {
        mViewCache.clear()
        for (i in 0..childCount) {
            val view = getChildAt(i)
            if (null != view) {
                val pos = getPosition(view)
                mViewCache.put(pos, view)
            }
        }
        for (i in 0..mViewCache.size()) {
            detachView(mViewCache.valueAt(i))
        }
        // position for center view
        var centerViewPosition = if (mCenterView == null) 0 else getPosition(mCenterView!!)

        var centerViewOffset = if (mCenterView == null) (width / 2f - mSettings.getViewWidthPx() / 2f).toInt() else getDecoratedLeft(mCenterView!!)

        // main fill logic
        if (mScrollToPosition != RecyclerView.NO_POSITION) {
            // fill views if start position not in the middle of screen (restore state)
            fillRightFromCenter(mScrollToPosition, centerViewOffset, recycler)
        } else {
            // fill views if start position in the middle of the screen
            fillRightFromCenter(centerViewPosition, centerViewOffset, recycler)
        }

        //update center view after recycle all views
        if (childCount != 0) {
            mCenterView = findCurrentCenterView()
        }

        for (i in 0 until mViewCache.size()) {
            recycler.recycleView(mViewCache.valueAt(i))
        }
        // update rotations.
        updateArcViewPositions()
    }

    /**
     * Measure view with margins and specs
     *
     * @param child      view to measure
     * @param widthSpec  spec for width
     * @param heightSpec spec for height
     */
    private fun measureChildWithDecorationsAndMargin(child: View, widthSpec: Int, heightSpec: Int) {
        var decorRect = Rect()
        calculateItemDecorationsForChild(child, decorRect)
        val lp = child.layoutParams as RecyclerView.LayoutParams
        var myWidthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + decorRect.left,
                lp.rightMargin + decorRect.right)
        var myHeightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + decorRect.top,
                lp.bottomMargin + decorRect.bottom)
        child.measure(myWidthSpec, myHeightSpec)
    }

    private fun updateSpecWithExtra(spec: Int, startInset: Int, endInset: Int): Int {
        if (startInset == 0 && endInset == 0) {
            return spec
        }
        val mode: Int = View.MeasureSpec.getMode(spec)
        return if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(spec) - startInset - endInset, mode)
        } else spec
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        // after fillRightFromCenter(...) we don't need this param.
        mScrollToPosition = RecyclerView.NO_POSITION
//        // after fillRightFromCenter(...) we don't need this param.
        mPendingSavedState = null

        if (dx == RecyclerView.NO_POSITION) {
            val delta = scrollHorizontallyInternal(dx)
            offsetChildrenHorizontal(-delta)
            fill(recycler!!)
            return delta
        }
        if (mSelectedItemPosition != RecyclerView.NO_POSITION && !mIsSelectAnimationInProcess && !mIsDeselectAnimationInProcess &&
                !mIsWaitingToDeselectAnimation && !mIsWaitingToSelectAnimation) {
            // if item selected and any animation isn't in progress
            deselectItem(mSelectedItemPosition)
        }
        // if animation in progress block scroll
        if (mIsDeselectAnimationInProcess || mIsSelectAnimationInProcess || mIsViewCollapsing) {
            return 0
        }
        val delta = scrollHorizontallyInternal(dx)
        offsetChildrenHorizontal(-delta)
        fill(recycler!!)
        return delta
    }

    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val scaledHeight = (mSettings.getViewHeightPx() * mAnimationHelper.getViewScaleFactor())
        val scaledWidth = (mSettings.getViewWidthPx() * mAnimationHelper.getViewScaleFactor())
        val height = if (heightMode == View.MeasureSpec.EXACTLY)
            View.MeasureSpec.getSize(heightSpec)
        else
            Math.sqrt((scaledHeight * scaledHeight + scaledWidth * scaledWidth).toDouble()).toInt()

        //noinspection Range
        var myHeightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        updateArcViewPositions()
        super.onMeasure(recycler, state, widthSpec, myHeightSpec)
    }

    /**
     * Calculate delta x for views.
     *
     * @param dx fling (user scroll gesture) delta x
     * @return delta x for views
     */
    private fun scrollHorizontallyInternal(dx: Int): Int {
        if (childCount == 0) {
            return 0
        }
        var leftView: View? = getChildAt(0)
        var rightView: View? = getChildAt(childCount - 1)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (getDecoratedLeft(leftView!!) > getDecoratedLeft(child!!)) {
                leftView = child
            }
            if (getDecoratedRight(rightView!!) < getDecoratedRight(child!!)) {
                rightView = child
            }
        }

        // area with filling views. need to find borders
        val viewSpan = if (getDecoratedRight(rightView!!) > width)
            getDecoratedRight(rightView)
        else
            width - if (getDecoratedLeft(leftView!!) < 0) getDecoratedLeft(leftView) else 0

        // check left and right borders
        if (viewSpan < width) {
            return 0
        }

        var delta = 0
        if (dx < 0) {// move views left
            val firstViewAdapterPos = getPosition(leftView!!)
            if (firstViewAdapterPos > 0) {
                // if item isn't first in the adapter
                delta = dx
            } else {
                // if item first in the adapter

                // stop scrolling if item in the middle.
                val viewLeft = getDecoratedLeft(leftView) - width / 2 + getDecoratedMeasuredWidth(leftView) / 2
                delta = Math.max(viewLeft, dx)
            }
        } else if (dx > 0) {// move views right
            val lastViewAdapterPos = getPosition(rightView)

            if (lastViewAdapterPos < itemCount - 1) {
                // if item isn't last in the adapter
                delta = dx
            } else {
                // if item last in the adapter

                // stop scrolling if item in the middle.
                val viewRight = getDecoratedRight(rightView) + width / 2 - getDecoratedMeasuredWidth(rightView) / 2
                val parentRight = width
                delta = Math.min(viewRight - parentRight, dx)
            }
        }
        return delta
    }

    /**
     * Change pivot, rotation, translation for view to create fan effect.
     * Change rotation to create bounce effect.
     */
    private fun updateArcViewPositions() {
        // +++++ init params +++++
        val halfWidth = (width / 2).toFloat()
        // minimal radius is recyclerView width * 2
        val radius = (width * 2).toDouble()
        val powRadius = radius * radius
        var rotation: Double
        var halfViewWidth: Float
        var deltaX: Double
        var deltaY: Double
        var viewPosition: Int
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            rotation = 0.0

            halfViewWidth = (child!!.width / 2).toFloat()
            // change pivot point to center bottom of the view
            child.pivotX = halfViewWidth
            child.pivotY = child.height.toFloat()

            if (mSettings.isFanRadiusEnable()) {

                // distance between center of screen to center of view in x-axis
                deltaX = (halfWidth - getDecoratedLeft(child).toFloat() - halfViewWidth).toDouble()

                // distance in which need to move view in y-axis. Low accuracy
                deltaY = radius - Math.sqrt(powRadius - deltaX * deltaX)
                child.translationY = (deltaY.toFloat())
                // calculate view rotation
                rotation = (Math.toDegrees(Math.asin((radius - deltaY) / radius)) - 90) * Math.signum(deltaX)

            }

            viewPosition = getPosition(child!!)
            var baseViewRotation: Float? = mViewRotationsMap.get(viewPosition)

            if (baseViewRotation == null) {
                // generate base (bounce) rotation for view.
                baseViewRotation = generateBaseViewRotation()
                mViewRotationsMap.put(viewPosition, baseViewRotation)
            }

            var rotationP: Float = if (mSelectedItemPosition == viewPosition && mIsSelectedItemStraightened) 0f else baseViewRotation!!.toFloat()
            child.rotation = (rotation.toFloat() + rotationP)
        }
    }

    private fun generateBaseViewRotation(): Float {
        return mRandom.nextFloat() * mSettings.getAngleItemBounce() * 2 - mSettings.getAngleItemBounce()
    }

    /**
     * Method draw view using center view position.
     *
     * @param centerViewPosition position of center view (anchor). This view will be in center
     * @param recycler           Recycler from the recyclerView
     */
    private fun fillRightFromCenter(centerViewPosition: Int, centerViewOffset: Int, recycler: RecyclerView.Recycler) {
        // left limit. need to prepare with before they will be show to user.
        val leftBorder = -(mSettings.getViewWidthPx() + if (mIsCollapsed) mSettings.getViewWidthPx() else 0)

        // right limit.
        val rightBorder = width + (mSettings.getViewWidthPx() + if (mIsCollapsed) mSettings.getViewWidthPx() else 0)
        var leftViewOffset = centerViewOffset
        var leftViewPosition = centerViewPosition

        // margin to draw cards in bottom
        val baseTopMargin = Math.max(0, height - mSettings.getViewHeightPx() - mSettings.getViewWidthPx() / 4)
        val overlapDistance: Int
        if (mIsCollapsed) {

            // overlap distance if views are collapsed
            overlapDistance = -mSettings.getViewWidthPx() / 4
        } else {

            // overlap distance if views aren't collapsed
            overlapDistance = mSettings.getViewWidthPx() / 4
        }

        var fillRight = true

        // specs for item views
        val widthSpec = View.MeasureSpec.makeMeasureSpec(mSettings.getViewWidthPx(), View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(mSettings.getViewHeightPx(), View.MeasureSpec.EXACTLY)

        // if have to restore state with selected item
        val hasPendingStateSelectedItem = mPendingSavedState != null && mPendingSavedState!!.isSelected &&
                mPendingSavedState!!.mCenterItemPosition != RecyclerView.NO_POSITION

        // offset for left and right views in case we have to restore pending state with selected view.
        // this is delta distance between overlap cards state and collapse (selected card) card state
        // need to use ones for all left view and right views
        val deltaOffset = mSettings.getViewWidthPx() / 2

        // --------- Prepare data ---------

        // search left position for first view
        while (leftViewOffset > leftBorder && leftViewPosition >= 0) {
            if (mIsCollapsed) {
                // offset for collapsed views
                leftViewOffset -= mSettings.getViewWidthPx() + Math.abs(overlapDistance)
            } else {
                // offset for NOT collapsed views
                leftViewOffset -= mSettings.getViewWidthPx() - Math.abs(overlapDistance)
            }
            leftViewPosition--
        }

        if (leftViewPosition < 0) {
            // if theoretically position for left view is less than left view.
            if (mIsCollapsed) {
                // offset for collapsed views
                leftViewOffset += (mSettings.getViewWidthPx() + Math.abs(overlapDistance)) * Math.abs(leftViewPosition)
            } else {
                // offset for NOT collapsed views
                leftViewOffset += (mSettings.getViewWidthPx() - Math.abs(overlapDistance)) * Math.abs(leftViewPosition)
            }
            leftViewPosition = 0
        }

        // offset for left views if we restore state and have selected item
        if (hasPendingStateSelectedItem && leftViewPosition != mPendingSavedState!!.mCenterItemPosition) {
            leftViewOffset += -deltaOffset
        }

        var selectedView: View? = null
        while (fillRight && leftViewPosition < itemCount) {

            // offset for current view if we restore state and have selected item
            if (hasPendingStateSelectedItem && leftViewPosition == mPendingSavedState!!.mCenterItemPosition && leftViewPosition != 0) {
                leftViewOffset += deltaOffset
            }

            // get view from local cache
            var view: View? = mViewCache.get(leftViewPosition)

            if (view == null) {
                // get view from recycler
                view = recycler.getViewForPosition(leftViewPosition)
                // optimization for view rotation
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                // add vew to the recyclerView
                addView(view)
                // measuring view
                measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec)
                // set offsets, with and height in the recyclerView
                layoutDecorated(view, leftViewOffset, baseTopMargin,
                        leftViewOffset + mSettings.getViewWidthPx(), baseTopMargin + mSettings.getViewHeightPx())
            } else {
                attachView(view, leftViewPosition)
                mViewCache.remove(leftViewPosition)
            }

            view.scaleX = 1f
            view.scaleY = 1f

            if (mIsSelected && centerViewPosition == leftViewPosition) {
                selectedView = view
            }


            // calculate position for next view. last position + view height - overlap between views.
            leftViewOffset = leftViewOffset + mSettings.getViewWidthPx() - overlapDistance

            // check right border. stop loop if next view is > then right border.
            fillRight = leftViewOffset < rightBorder

            // offset for right views if we restore state and have selected item
            if (hasPendingStateSelectedItem && leftViewPosition == mPendingSavedState!!.mCenterItemPosition) {
                leftViewOffset += deltaOffset
            }

            leftViewPosition++
        }

        // if we have to restore state with selected item
        // this part need to scale center selected view
        if (hasPendingStateSelectedItem) {
            //            View view = findCurrentCenterView();
            if (selectedView != null) {
                selectedView.scaleX = mAnimationHelper.getViewScaleFactor()
                selectedView.scaleY = mAnimationHelper.getViewScaleFactor()
            }
        }
    }


    private fun findCurrentCenterView(): View? {
        // +++++ prepare data +++++
        // center of the screen in x-axis
        val centerX = width / 2f
        val viewHalfWidth = mSettings.getViewWidthPx() / 2f
        var nearestToCenterView: View? = null
        var nearestDeltaX: Int = 0
        var item: View?
        var centerXView: Int
        for (i in 0..childCount) {
            item = getChildAt(i)
            item?.let {
                centerXView = (getDecoratedLeft(item!!) + viewHalfWidth).toInt()
                if (nearestToCenterView == null || Math.abs(nearestDeltaX) > Math.abs(centerX - centerXView)) {
                    nearestToCenterView = item
                    nearestDeltaX = (centerX - centerXView).toInt()
                }
            }
        }
        return nearestToCenterView
    }

    /**
     * Method change item state from close to open and open to close (switch state)
     *
     * @param recyclerView         current recycler view. Need to smooth scroll.
     * @param selectedViewPosition item view position
     */
     fun switchItem(recyclerView: RecyclerView?, selectedViewPosition: Int) {
        if (mIsDeselectAnimationInProcess || mIsSelectAnimationInProcess || mIsViewCollapsing ||
                mIsWaitingToDeselectAnimation || mIsWaitingToSelectAnimation || mIsSelectedItemStraightenedInProcess) {
            // block event if any animation in progress
            return
        }

        if (recyclerView != null) {
            if (mSelectedItemPosition != RecyclerView.NO_POSITION && mSelectedItemPosition != selectedViewPosition) {
                // if item selected
                deselectItem(recyclerView, mSelectedItemPosition, selectedViewPosition, 0)
                return
            }
            // if item not selected need to smooth scroll and then select item
            smoothScrollToPosition(recyclerView, null, selectedViewPosition)
        }
    }

    fun isItemSelected(): Boolean {
        return mSelectedItemPosition != RecyclerView.NO_POSITION
    }

    private fun selectItem(position: Int, delay: Int) {
        if (mSelectedItemPosition == position) {
            // if select already selected item
            deselectItem(mSelectedItemPosition)
            return
        }

        // search view by position
        var viewToSelect: View? =
                null
        val count = childCount
        var i = 0
        while (i < count) {
            val view = getChildAt(i)
            if (position == getPosition(view!!)) {
                viewToSelect = view
            }
            i++
        }

        if (viewToSelect == null) {
            // view to select not found!!!
            return
        }
        // save position of view which will be selected
        mSelectedItemPosition = position
        // save selected stay... no way back...
        mIsSelected = true
        // open item animation wait for start but not in process.
        // select item animation prepare and wait until smooth scroll is finished
        mIsWaitingToSelectAnimation = true

        mAnimationHelper.openItem(viewToSelect, delay * 3 /*need to finish scroll before start open*/,
                object : SimpleAnimatorListener() {
                    override fun onAnimationStart(animation: Animator?) {
                        mIsSelectAnimationInProcess = true
                        mIsWaitingToSelectAnimation = false
                        // shift distance between center view and left, right views.
                        val delta = mSettings.getViewWidthPx() / 2
                        // generate data for animation helper. (calculate final positions for all views)
                        val infoViews = ViewAnimationInfoGenerator.generate(delta,
                                true,
                                this@FanlayoutManager,
                                mSelectedItemPosition,
                                false)

                        // animate shifting let and right views
                        mAnimationHelper.shiftSideViews(
                                infoViews,
                                0,
                                this@FanlayoutManager, null,
                                ValueAnimator.AnimatorUpdateListener {
                                    // update rotation and translation for all views
                                    updateArcViewPositions()
                                })
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        mIsSelectAnimationInProcess = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        mIsSelectAnimationInProcess = false
                    }
                })
    }

    /**
     * Deselect selected item. [.deselectItem]
     */
    fun deselectItem() {
        deselectItem(mSelectedItemPosition)
    }

    /**
     * Deselect item with default params. [.deselectItem]
     *
     * @param position selected item position
     */
    private fun deselectItem(position: Int) {
        deselectItem(null, position, RecyclerView.NO_POSITION, 0)
    }

    /**
     * Deselect item
     *
     * @param recyclerView     RecyclerView for this LayoutManager
     * @param position         position item for deselect
     * @param scrollToPosition position to scroll after deselect
     * @param delay            waiting duration before start deselect
     */

    private fun deselectItem(recyclerView: RecyclerView?, position: Int, scrollToPosition: Int, delay: Int) {

        if (position == RecyclerView.NO_POSITION) {
            // if position is default non selected value
            return
        }

        if (mIsSelectedItemStraightened) {
            restoreBaseRotationSelectedItem(object : SimpleAnimatorListener() {

                override fun onAnimationEnd(animation: Animator?) {
                    closeItem(recyclerView, position, scrollToPosition, delay)
                }
            })
        } else {
            closeItem(recyclerView, position, scrollToPosition, delay)
        }

    }

    /**
     * Close item
     *
     * @param recyclerView     RecyclerView for this LayoutManager
     * @param position         position item for deselect
     * @param scrollToPosition position to scroll after deselect
     * @param delay            waiting duration before start deselect
     */

    private fun closeItem(recyclerView: RecyclerView?, position: Int, scrollToPosition: Int, delay: Int) {
        // wait for start deselect animation
        mIsWaitingToDeselectAnimation = true
        // search view by position
        var viewToDeselect: View? = null
        val count = childCount
        var i = 0
        while (i < count) {
            val view = getChildAt(i)
            if (position == getPosition(view!!)) {
                viewToDeselect = view
            }
            i++
        }
        // remove selected item position
        mSelectedItemPosition = RecyclerView.NO_POSITION

        // remove selected state... no way back...
        mIsSelected = false

        if (viewToDeselect == null) {
            mSelectedItemPosition = RecyclerView.NO_POSITION
            // search error!!! No view found!!!
            return
        }

        // close item animation
        mAnimationHelper.closeItem(viewToDeselect, delay, object : SimpleAnimatorListener() {

            override fun onAnimationStart(animation: Animator?) {
                // change states
                mIsDeselectAnimationInProcess = true
                mIsWaitingToDeselectAnimation = false

                // shift distance between center view and left, right views.
                val delta = mSettings.getViewWidthPx() / 2

                // generate data for animation helper. (calculate final positions for all views)
                val infoViews = ViewAnimationInfoGenerator.generate(delta,
                        false,
                        this@FanlayoutManager,
                        position,
                        false)

                // animate shifting let and right views
                mAnimationHelper.shiftSideViews(
                        infoViews,
                        0,
                        this@FanlayoutManager, null,
                        ValueAnimator.AnimatorUpdateListener {
                            // update rotation and translation for all views
                            updateArcViewPositions()
                        })
            }

            override fun onAnimationEnd(animation: Animator?) {
                mIsDeselectAnimationInProcess = false
                if (recyclerView != null && scrollToPosition != RecyclerView.NO_POSITION) {
                    // scroll to new position after deselect animation end
                    smoothScrollToPosition(recyclerView, null, scrollToPosition)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                mIsDeselectAnimationInProcess = false
                if (recyclerView != null && scrollToPosition != RecyclerView.NO_POSITION) {
                    // scroll to new position after deselect animation cancel
                    smoothScrollToPosition(recyclerView, null, scrollToPosition)
                }
            }
        })
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        // when user stop scrolling
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // show view in the middle of screen
            scrollToCenter()
        }
    }

    /**
     * Scroll views left or right so nearest view will be in the middle of screen
     */
    private fun scrollToCenter() {
        val nearestToCenterView = findCurrentCenterView()
        if (nearestToCenterView != null) {
            // scroll to the nearest view
            mShiftToCenterCardScroller.targetPosition = getPosition(nearestToCenterView)
            startSmoothScroll(mShiftToCenterCardScroller)
        }
    }

    override fun scrollToPosition(position: Int) {
        mScrollToPosition = position
        requestLayout()
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        if (position >= itemCount) {
            // if position is not in range
            return
        }
        // smooth scroll to position
        mFanCardScroller.targetPosition = position
        startSmoothScroll(mFanCardScroller)
    }

    /**
     * Method need to remove bounce item rotation
     *
     * @param listener straighten function listener
     */
    fun straightenSelectedItem(listener: Animator.AnimatorListener?) {
        // check all animations
        if (mSelectedItemPosition == RecyclerView.NO_POSITION || mIsSelectAnimationInProcess ||
                mIsDeselectAnimationInProcess || mIsSelectedItemStraightenedInProcess || mIsWaitingToDeselectAnimation ||
                mIsWaitingToSelectAnimation || mIsViewCollapsing || mIsSelectedItemStraightened) {
            // block if any animation in progress
            return
        }

        // +++++ prepare data +++++
        var viewToRotate: View? = null
        var view: View?
        // ----- prepare data -----

        // search selected view
        val count = childCount
        var i = 0
        while (i < count) {
            view = getChildAt(i)

            if (mSelectedItemPosition == getPosition(view!!)) {
                viewToRotate = view
            }
            i++
        }

        if (viewToRotate != null) {
            // start straight animation
            mAnimationHelper.straightenView(viewToRotate, object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    listener?.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    listener?.onAnimationEnd(animation)
                    mIsSelectedItemStraightened = true
                    mIsSelectedItemStraightenedInProcess = false

                }

                override fun onAnimationCancel(animation: Animator) {
                    listener?.onAnimationCancel(animation)
                    mIsSelectedItemStraightened = true
                    mIsSelectedItemStraightenedInProcess = false
                }

                override fun onAnimationRepeat(animation: Animator) {
                    listener?.onAnimationRepeat(animation)
                }
            })

            // save state
            mIsSelectedItemStraightenedInProcess = true
        }
    }

    /**
     * Method need to restore base (bounce) rotation for item
     *
     * @param listener rotate function listener
     */
    fun restoreBaseRotationSelectedItem(listener: Animator.AnimatorListener?) {
        // check all animations
        if (mSelectedItemPosition == RecyclerView.NO_POSITION || mIsSelectAnimationInProcess ||
                mIsDeselectAnimationInProcess || mIsSelectedItemStraightenedInProcess || mIsWaitingToDeselectAnimation ||
                mIsWaitingToSelectAnimation || mIsViewCollapsing || !mIsSelectedItemStraightened) {
            // block if any animation in progress
            return
        }

        // +++++ prepare data +++++
        var viewToRotate: View? = null
        var view: View?
        // ----- prepare data -----

        // search selected view
        val count = childCount
        var i = 0
        while (i < count) {
            view = getChildAt(i)

            if (mSelectedItemPosition == getPosition(view!!)) {
                viewToRotate = view
            }
            i++
        }

        var baseViewRotation: Float? = mViewRotationsMap.get(mSelectedItemPosition)

        if (baseViewRotation == null) {
            // generate base (bounce) rotation for view.
            baseViewRotation = generateBaseViewRotation()
            mViewRotationsMap.put(mSelectedItemPosition, baseViewRotation)
        }

        if (viewToRotate != null) {
            // save state
            mIsSelectedItemStraightenedInProcess = true

            // start straight animation
            mAnimationHelper.rotateView(viewToRotate, baseViewRotation, object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    listener?.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    listener?.onAnimationEnd(animation)
                    mIsSelectedItemStraightened = false
                    mIsSelectedItemStraightenedInProcess = false

                }

                override fun onAnimationCancel(animation: Animator) {
                    listener?.onAnimationCancel(animation)
                    mIsSelectedItemStraightened = false
                    mIsSelectedItemStraightenedInProcess = false
                }

                override fun onAnimationRepeat(animation: Animator) {
                    listener?.onAnimationRepeat(animation)
                }
            })
        }
    }

    /**
     * Method collapsed views (cards).
     */
    fun collapseViews() {
        // check all animations
        if (mIsSelectAnimationInProcess || mIsWaitingToSelectAnimation ||
                mIsDeselectAnimationInProcess || mIsWaitingToDeselectAnimation ||
                mIsSelectedItemStraightenedInProcess || mIsViewCollapsing) {
            return
        }
        // steps:
        // 1) Lock screen (Stop scrolling)
        // 2) Collapse all cards
        // 3) Unlock screen
        // 4) Scroll to center nearest card if not selected

        // 1) lock screen
        mIsViewCollapsing = true

        // 2) Collapse all cards
        updateItemsByMode()
    }

    /**
     * Method collapsing all views
     */
    private fun updateItemsByMode() {

        // collapse distance
        val delta = mSettings.getViewWidthPx() / 2

        // generate data for collapse animation
        val infoViews = ViewAnimationInfoGenerator.generate(delta, !mIsCollapsed, this@FanlayoutManager, findCurrentCenterViewPos(), true)

        // collapse views
        mAnimationHelper.shiftSideViews(infoViews,
                0,
                this@FanlayoutManager,
                object : SimpleAnimatorListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        // 3) Unlock screen
                        mIsViewCollapsing = !mIsViewCollapsing
                        // 4) Scroll to center nearest card
                        scrollToCenter()
                    }
                }, ValueAnimator.AnimatorUpdateListener {
            // update rotation and translation for all views
            updateArcViewPositions()
        })
    }

    /**
     * Find position of view in the middle of screen
     *
     * @return position of center view or [RecyclerView.NO_POSITION]
     */
    private fun findCurrentCenterViewPos(): Int {
        val view = mCenterView
        return if (view == null) RecyclerView.NO_POSITION else getPosition(view)
    }

    override fun onItemsChanged(recyclerView: RecyclerView) {
        super.onItemsChanged(recyclerView)
        recyclerView.stopScroll()
        saveState()
        if (itemCount <= mSelectedItemPosition) {
            mSelectedItemPosition = RecyclerView.NO_POSITION
            // save selected state for center view
            mPendingSavedState!!.isSelected = false
            mIsSelected = false
        }
    }

    override fun onItemsAdded(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsAdded(recyclerView, positionStart, itemCount)
        recyclerView.stopScroll()
        saveState()
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount)
        recyclerView.stopScroll()
        saveState()
    }

    override fun onItemsUpdated(recyclerView: RecyclerView, positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemsUpdated(recyclerView, positionStart, itemCount, payload)
        recyclerView.stopScroll()
        saveState()
    }

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount)
        recyclerView.stopScroll()
        saveState()
        if (mSelectedItemPosition >= positionStart && mSelectedItemPosition < positionStart + itemCount) {
            mSelectedItemPosition = RecyclerView.NO_POSITION
            // save selected state for center view
            mPendingSavedState!!.isSelected = false
        }
    }

    override fun onItemsMoved(recyclerView: RecyclerView, from: Int, to: Int, itemCount: Int) {
        super.onItemsMoved(recyclerView, from, to, itemCount)
    }


    private class SavedState : Parcelable {

        internal var mCenterItemPosition = RecyclerView.NO_POSITION
        internal var isCollapsed: Boolean = false
        internal var isSelected: Boolean = false
        internal var mRotation: SparseArray<Any>

        constructor() {
            mRotation = SparseArray()
        }

        internal constructor(`in`: Parcel) {
            mCenterItemPosition = `in`.readInt()
            isCollapsed = `in`!!.readInt() == 1
            isSelected = `in`.readInt() == 1

            mRotation = `in`.readSparseArray(SparseArray::class.java.classLoader) as SparseArray<Any>
        }

        constructor(other: SavedState) {
            mCenterItemPosition = other.mCenterItemPosition
            isCollapsed = other.isCollapsed
            isSelected = other.isSelected
            mRotation = other.mRotation
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(mCenterItemPosition)
            dest.writeInt(if (isCollapsed) 1 else 0)
            dest.writeInt(if (isSelected) 1 else 0)

            dest.writeSparseArray(mRotation)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return newArray(size)
                }
            }
        }
    }

}