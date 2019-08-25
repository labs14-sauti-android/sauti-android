package com.labs.sauti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.model.market_price.MarketPrice
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import java.text.DecimalFormat

class DashboardFavoritesAdapter(
    private var favorites: MutableList<Any>,
    private val onItemClickListener: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MARKET_PRICE = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MARKET_PRICE -> MarketPriceViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_market_price, parent, false))
            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (favorites[position]) {
            is MarketPrice -> TYPE_MARKET_PRICE
            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favorite = favorites[position]
        when (holder) {
            is MarketPriceViewHolder -> holder.bind(favorite as MarketPrice, onItemClickListener)
        }
    }

    fun setFavorites(favorites: MutableList<Any>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    class MarketPriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.layoutParams.width = itemView.context.resources.getDimension(R.dimen.cardWidth).toInt()
        }

        fun bind(marketPrice: MarketPrice, onItemClickListener: (Any) -> Unit) {
            itemView.setOnClickListener {
                onItemClickListener(marketPrice)
            }

            itemView.t_recent_product.text = "${marketPrice.product}"
            itemView.t_recent_market.text = "${marketPrice.market}"
            val decimalFormat = DecimalFormat("#,##0")
            val wholesaleStr = decimalFormat.format(marketPrice.wholesale ?: 0.0)
            itemView.t_recent_wholesale.text = "$wholesaleStr ${marketPrice.currency}/1Kg"
            if (marketPrice.retail != null && marketPrice.retail!! > 0.0) {
                itemView.ll_recent_retail.visibility = View.VISIBLE

                val retailStr = decimalFormat.format(marketPrice.retail!!)
                itemView.t_recent_retail.text = "$retailStr ${marketPrice.currency}/1Kg"
            } else {
                itemView.ll_recent_retail.visibility = View.GONE
            }
            itemView.t_recent_updated.text = "Updated: ${marketPrice.date?.substring(0, 10)}"
            itemView.t_recent_source.text = "Source: EAGC-RATIN" // TODO
        }
    }

}