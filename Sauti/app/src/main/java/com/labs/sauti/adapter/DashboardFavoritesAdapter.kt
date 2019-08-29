package com.labs.sauti.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.model.market_price.MarketPrice
import kotlinx.android.synthetic.main.item_recent_exchange_rate.view.*
import kotlinx.android.synthetic.main.item_recent_market_price.view.*
import java.text.DecimalFormat

class DashboardFavoritesAdapter(
    private var favorites: MutableList<Any>,
    private val onItemClickListener: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MARKET_PRICE = 0
        private const val TYPE_EXCHANGE_RATE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MARKET_PRICE -> MarketPriceViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_market_price, parent, false))
            TYPE_EXCHANGE_RATE -> ExchangeRateConversionResultViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_exchange_rate, parent, false))
            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (favorites[position]) {
            is MarketPrice -> TYPE_MARKET_PRICE
            is ExchangeRateConversionResult -> TYPE_EXCHANGE_RATE
            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favorite = favorites[position]
        when (holder) {
            is MarketPriceViewHolder -> holder.bind(favorite as MarketPrice, onItemClickListener)
            is ExchangeRateConversionResultViewHolder -> holder.bind(favorite as ExchangeRateConversionResult, onItemClickListener)
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

    class ExchangeRateConversionResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.layoutParams.width = itemView.context.resources.getDimension(R.dimen.cardWidth).toInt()
        }

        fun bind(exchangeRateConversionResult: ExchangeRateConversionResult, onItemClickListener: (Any) -> Unit) {
            itemView.setOnClickListener {
                onItemClickListener(exchangeRateConversionResult)
            }

            val decimalFormat = DecimalFormat("#,##0.00")
            val amountStr = decimalFormat.format(exchangeRateConversionResult.amount)
            val resultStr = decimalFormat.format(exchangeRateConversionResult.result)
            itemView.t_recent_result.text = "$amountStr ${exchangeRateConversionResult.fromCurrency} ${itemView.context.resources.getString(R.string.exchange_rate_is)} $resultStr ${exchangeRateConversionResult.toCurrency}"
            val toPerFromStr = decimalFormat.format(exchangeRateConversionResult.toPerFrom)
            itemView.t_recent_to_per_from.text = "(1${exchangeRateConversionResult.fromCurrency} = $toPerFromStr ${exchangeRateConversionResult.toCurrency})"
        }
    }

}