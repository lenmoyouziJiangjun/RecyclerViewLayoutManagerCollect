package com.lll.demo.fanlayoutmanager.fragment

import android.animation.Animator
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.lll.demo.fanlayoutmanager.SportCardsUtils
import com.lll.demo.fanlayoutmanager.view.SharedTransitionSet
import com.lll.demo.fanlayoutmanager.view.SportCardsAdapter
import com.lll.layoutmanager.demo.R
import com.lll.layoutmanager.fanlayoutmanager.FanLayoutManagerSettings
import com.lll.layoutmanager.fanlayoutmanager.FanlayoutManager
import com.lll.layoutmanager.fanlayoutmanager.callback.FanChildDrawingOrderCallback

class MainFragment : Fragment() {

    private lateinit var mLayoutManager: FanlayoutManager
    private lateinit var mAdapter: SportCardsAdapter

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvCards)
        val fanLayoutManagerSettings: FanLayoutManagerSettings =
                FanLayoutManagerSettings.newBuilder(context!!).withFanRadius(true)
                        .withAngleItemBounce(5f)
                        .withViewHeightDp(160f)
                        .withViewWidthDp(120f)
                        .build()


        mLayoutManager = FanlayoutManager(context!!, fanLayoutManagerSettings)

        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        mAdapter = SportCardsAdapter(context!!)
        mAdapter.addAll(SportCardsUtils.generateSportCards())
        mAdapter.setOnItemClickListener(object : SportCardsAdapter.OnItemClickListener {
            override fun onItemClicked(itemPosition: Int, view: View) {
                if (mLayoutManager.getSelectedItemPosition() !== itemPosition) {
                    mLayoutManager.switchItem(recyclerView, itemPosition)
                } else {
                    mLayoutManager.straightenSelectedItem(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {

                        }

                        override fun onAnimationEnd(animator: Animator) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                onClick(view, mLayoutManager.getSelectedItemPosition())
                            } else {
                                onClick(mLayoutManager.getSelectedItemPosition())
                            }
                        }

                        override fun onAnimationCancel(animator: Animator) {

                        }

                        override fun onAnimationRepeat(animator: Animator) {

                        }
                    })
                }
            }
        })
        recyclerView.adapter = mAdapter

        recyclerView.setChildDrawingOrderCallback(FanChildDrawingOrderCallback(mLayoutManager))

        (view.findViewById(R.id.logo) as View).setOnClickListener(View.OnClickListener { mLayoutManager.collapseViews() })
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun onClick(view: View, pos: Int) {
        val fragment = FullInfoTabFragment.newInstance(mAdapter.getModelByPos(pos))

        fragment.setSharedElementEnterTransition(SharedTransitionSet())
        fragment.setEnterTransition(Fade())
        exitTransition = Fade()
        fragment.setSharedElementReturnTransition(SharedTransitionSet())

        activity!!.supportFragmentManager
                .beginTransaction()
                .addSharedElement(view, "shared")
                .replace(R.id.root, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun onClick(pos: Int) {
        val fragment = FullInfoTabFragment.newInstance(mAdapter.getModelByPos(pos))
        activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, fragment)
                .addToBackStack(null)
                .commit()
    }

    fun deselectIfSelected(): Boolean {
        when (mLayoutManager.isItemSelected()) {
            false ->
                return false
            true -> {
                mLayoutManager.deselectItem()
                return true
            }
        }
    }


}