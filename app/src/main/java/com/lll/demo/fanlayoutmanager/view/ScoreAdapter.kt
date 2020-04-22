package com.lll.demo.fanlayoutmanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lll.demo.fanlayoutmanager.models.AthleticModel
import com.lll.demo.fanlayoutmanager.models.Country
import com.lll.layoutmanager.demo.R
import java.util.ArrayList

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.AthleticHolder>() {
    private val mItems = ArrayList<AthleticModel>()

    fun addItems(items: Collection<AthleticModel>) {
        mItems.addAll(items)
        notifyItemRangeInserted(mItems.size - items.size - 1, items.size)
    }

    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleticHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return AthleticHolder(view)
    }

    override fun onBindViewHolder(holder: AthleticHolder, position: Int) {
        val item = mItems[position]
        holder.tvAthleticName.text = (item.name)
        holder.tvCountry.text = (item.country.name)
        holder.tvScore.text = (item.score.toString())
        when (item.country) {
            Country.USA -> {
                holder.ivAthleticFlag.setImageResource(R.drawable.us_flag)
            }
            Country.ROK -> {
                holder.ivAthleticFlag.setImageResource(R.drawable.flag_korea)
            }
            Country.ITALY -> {
                holder.ivAthleticFlag.setImageResource(R.drawable.italy_flag)
            }
        }

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class AthleticHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAthleticFlag: ImageView
        var tvCountry: TextView
        var tvAthleticName: TextView
        var tvScore: TextView


        init {
            ivAthleticFlag = itemView.findViewById(R.id.ivAthleticFlag) as ImageView
            tvCountry = itemView.findViewById(R.id.tvCountry) as TextView
            tvAthleticName = itemView.findViewById(R.id.tvAthleticName) as TextView
            tvScore = itemView.findViewById(R.id.tvScore) as TextView
        }
    }
}