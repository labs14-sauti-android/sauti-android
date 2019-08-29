package com.labs.sauti.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.labs.sauti.R
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import kotlinx.android.synthetic.main.item_recent_exchange_rate.view.*
import java.text.DecimalFormat

class RecentExchangeRateConversionResultsAdapter(
    private var recentExchangeRateConversionResults: List<ExchangeRateConversionResult>,
    private val onRecentExchangeRateConversionResultClickedListener: OnRecentExchangeRateConversionResultClickedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecentExchangeRateConversionResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recent_exchange_rate, parent, false))
    }

    override fun getItemCount(): Int {
        return recentExchangeRateConversionResults.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exchangeRateConversionResult = recentExchangeRateConversionResults[position]

        if (holder is RecentExchangeRateConversionResultViewHolder) {
            holder.bind(exchangeRateConversionResult, onRecentExchangeRateConversionResultClickedListener)
        }
    }

    fun setRecentExchangeRateConversionResults(recentExchangeRateConversionResults: List<ExchangeRateConversionResult>) {
        this.recentExchangeRateConversionResults = recentExchangeRateConversionResults

        notifyDataSetChanged()
    }

    class RecentExchangeRateConversionResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            recentExchangeRateConversionResult: ExchangeRateConversionResult,
            onRecentExchangeRateConversionResultClickedListener: OnRecentExchangeRateConversionResultClickedListener
        ) {
            itemView.setOnClickListener {
                onRecentExchangeRateConversionResultClickedListener.onRecentExchangeRateConversionResultClicked(layoutPosition, recentExchangeRateConversionResult)
            }

            val decimalFormat = DecimalFormat("#,##0.00")
            val amountStr = decimalFormat.format(recentExchangeRateConversionResult.amount)
            val resultStr = decimalFormat.format(recentExchangeRateConversionResult.result)
            itemView.t_recent_result.text = "$amountStr ${recentExchangeRateConversionResult.fromCurrency} ${itemView.context.resources.getString(R.string.exchange_rate_is)} $resultStr ${recentExchangeRateConversionResult.toCurrency}"
            val toPerFromStr = decimalFormat.format(recentExchangeRateConversionResult.toPerFrom)
            itemView.t_recent_to_per_from.text = "(1${recentExchangeRateConversionResult.fromCurrency} = $toPerFromStr ${recentExchangeRateConversionResult.toCurrency})"
        }
    }

    interface OnRecentExchangeRateConversionResultClickedListener {
        fun onRecentExchangeRateConversionResultClicked(position: Int, recentExchangeRateConversionResult: ExchangeRateConversionResult)
    }

}