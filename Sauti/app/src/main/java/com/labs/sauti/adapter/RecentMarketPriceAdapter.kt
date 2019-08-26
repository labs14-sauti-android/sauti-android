package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.market_price.MarketPrice
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import java.text.DecimalFormat

class RecentMarketPriceAdapter(
    private var recentMarketPrices: List<MarketPrice>,
    private val onRecentMarketPriceClickedListener: OnRecentMarketPriceClickedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecentMarketPriceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recent_market_price, parent, false))
    }

    override fun getItemCount(): Int {
        return recentMarketPrices.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recentMarketPrice = recentMarketPrices[position]

        if (holder is RecentMarketPriceViewHolder) {
            holder.bind(recentMarketPrice, onRecentMarketPriceClickedListener)
        }
    }

    fun setRecentMarketPrices(recentMarketPrices: List<MarketPrice>) {
        this.recentMarketPrices = recentMarketPrices

        notifyDataSetChanged()
    }

    class RecentMarketPriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recentMarketPrice: MarketPrice, onRecentMarketPriceClickedListener: OnRecentMarketPriceClickedListener) {
            itemView.setOnClickListener {
                onRecentMarketPriceClickedListener.onRecentMarketPriceClicked(layoutPosition, recentMarketPrice)
            }

            itemView.t_recent_product.text = "${recentMarketPrice.product}"
            itemView.t_recent_market.text = "${recentMarketPrice.market}"
            val decimalFormat = DecimalFormat("#,##0")
            val wholesaleStr = decimalFormat.format(recentMarketPrice.wholesale ?: 0.0)
            itemView.t_recent_wholesale.text = "$wholesaleStr ${recentMarketPrice.currency}/1Kg"
            if (recentMarketPrice.retail != null && recentMarketPrice.retail!! > 0.0) {
                itemView.ll_recent_retail.visibility = View.VISIBLE

                val retailStr = decimalFormat.format(recentMarketPrice.retail!!)
                itemView.t_recent_retail.text = "$retailStr ${recentMarketPrice.currency}/1Kg"
            } else {
                itemView.ll_recent_retail.visibility = View.GONE
            }
            itemView.t_recent_updated.text = "Updated: ${recentMarketPrice.date?.substring(0, 10)}"
            itemView.t_recent_source.text = "Source: EAGC-RATIN" // TODO
        }
    }

    interface OnRecentMarketPriceClickedListener {
        fun onRecentMarketPriceClicked(position: Int, recentMarketPrice: MarketPrice)
    }

}