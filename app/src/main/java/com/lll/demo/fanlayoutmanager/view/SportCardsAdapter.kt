package com.lll.demo.fanlayoutmanager.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lll.demo.fanlayoutmanager.models.SportCardModel
import com.lll.layoutmanager.demo.R

class SportCardsAdapter(val context: Context) : RecyclerView.Adapter<SportCardsAdapter.SportCardsViewHolder>() {

    private val mItems: MutableList<SportCardModel> = mutableListOf()

    lateinit var mItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(pos: Int, view: View)
    }

    fun add(item: SportCardModel): Boolean {
        val isAdded = mItems.add(item)
        if (isAdded) {
            notifyDataSetChanged()
        }
        return isAdded
    }

    fun addAll(items: Collection<SportCardModel>): Boolean {
        val isAdded = mItems.addAll(items)
        if (isAdded) {
            notifyDataSetChanged()
        }
        return isAdded
    }

    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportCardsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return SportCardsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(viewHolder: SportCardsViewHolder, index: Int) {
        viewHolder.bindData(mItems[index])
    }

    fun getModelByPos(pos: Int): SportCardModel {
        return mItems[pos]
    }

    /**
     * 嵌套类和内部类的区别
     * 嵌套类不放访问外部类的实例
     */
    inner class SportCardsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSportTitle: TextView
        val tvSportSubtitle: TextView
        val tvSportRound: TextView
        val tvTime: TextView
        val tvDayPart: TextView
        var ivSportPreview: ImageView

        init {
            tvSportTitle = view.findViewById(R.id.tvSportTitle) as TextView
            tvSportSubtitle = view.findViewById(R.id.tvSportSubtitle) as TextView
            tvSportRound = view.findViewById(R.id.tvSportRound) as TextView
            ivSportPreview = view.findViewById(R.id.ivSportPreview) as ImageView
            tvTime = view.findViewById(R.id.tvTime) as TextView
            tvDayPart = view.findViewById(R.id.tvDayPart) as TextView
        }

        fun bindData(model: SportCardModel) {

            tvSportTitle.text = model.sportTitle
            tvSportSubtitle.text = model.sportSubtitle
            tvSportRound.text = model.sportRound
            ivSportPreview.setImageResource(model.imageResId)
            tvTime.text = model.time
            tvDayPart.text = model.dayPart

            (itemView as CardView).setCardBackgroundColor(ContextCompat.getColor(itemView.context, model.backgroundColorResId))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivSportPreview.transitionName = "shared" + position.toString()
            }

            itemView.setOnClickListener {
                if (mItemClickListener != null) { //注意 使用到了外部类的成员变量，所以这个类是内部类，需要用inner class 修饰
                    mItemClickListener.onItemClicked(adapterPosition, ivSportPreview)
                }
            }
        }
    }
}