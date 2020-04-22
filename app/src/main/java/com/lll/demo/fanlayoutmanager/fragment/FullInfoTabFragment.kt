package com.lll.demo.fanlayoutmanager.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lll.demo.fanlayoutmanager.models.AthleticModel
import com.lll.demo.fanlayoutmanager.models.Country
import com.lll.demo.fanlayoutmanager.models.SportCardModel
import com.lll.demo.fanlayoutmanager.view.ScoreAdapter
import com.lll.layoutmanager.demo.R
import java.util.ArrayList


class FullInfoTabFragment : Fragment() {

    private val EXTRA_SRORT_CARD_MODEL = "EXTRA_SRORT_CARD_MODEL"

    private lateinit var mSportCardModel: SportCardModel
    private lateinit var mToolbar: Toolbar
    private lateinit var mPhoto: ImageView
    private lateinit var mRvAthletics: RecyclerView

    companion object {
        fun newInstance(model: SportCardModel): FullInfoTabFragment {
            val fragment: FullInfoTabFragment = FullInfoTabFragment()
            val argument: Bundle = Bundle()
            argument.putSerializable("model", model)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mSportCardModel = arguments!!.getSerializable(EXTRA_SRORT_CARD_MODEL) as SportCardModel
        }
        if (savedInstanceState != null) {
            mSportCardModel = savedInstanceState.getSerializable(EXTRA_SRORT_CARD_MODEL) as SportCardModel
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_full_info, container, false)
        mToolbar = view.findViewById(R.id.toolbar) as Toolbar
        mPhoto = view.findViewById(R.id.ivPhoto) as ImageView
        mRvAthletics = view.findViewById(R.id.rvAthletics) as RecyclerView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            mToolbar.title = mSportCardModel.sportTitle
            mToolbar.setNavigationOnClickListener(View.OnClickListener { activity!!.onBackPressed() })
            mToolbar.setNavigationIcon(R.drawable.ic_back)
        }


        mToolbar.setBackgroundColor(ContextCompat.getColor(context!!, mSportCardModel.backgroundColorResId))
        mPhoto.setImageResource(mSportCardModel.imageResId)
        val items = ArrayList<AthleticModel>()
        for (i in 10 downTo 1) {
            var points = i * 100L
            items.add(AthleticModel("Vae, mirabilis tumultumque", Country.ITALY, --points))
            items.add(AthleticModel("Cobaltums favere", Country.USA, --points))
            items.add(AthleticModel("Stella de peritus lixa", Country.ROK, --points))
        }

        val scoreAdapter = ScoreAdapter()
        scoreAdapter.addItems(items)

        mRvAthletics.adapter = scoreAdapter
        mRvAthletics.layoutManager = (LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        mRvAthletics.itemAnimator = DefaultItemAnimator()
        mRvAthletics.addItemDecoration(DividerItemDecoration(context!!, null))

    }

    class DividerItemDecoration(val context: Context, resId: Int?) : RecyclerView.ItemDecoration() {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        private var mDivider: Drawable?

        init {
            val styledAttributes = context.obtainStyledAttributes(ATTRS)
            mDivider = styledAttributes.getDrawable(0)
            styledAttributes.recycle()
            if (resId != null) {
                mDivider = ContextCompat.getDrawable(context, resId)
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left: Int = parent.paddingLeft
            val right: Int = parent.width - parent.paddingRight

            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }
    }

}