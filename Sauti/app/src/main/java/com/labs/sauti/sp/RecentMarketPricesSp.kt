package com.labs.sauti.sp

import android.content.Context
import com.google.gson.Gson
import com.labs.sauti.model.MarketPrice
import com.google.gson.reflect.TypeToken



class RecentMarketPricesSp(private val context: Context, private val gson: Gson) {

    companion object {
        private const val SP_NAME = "recent_market_prices"
        private const val RECENT_MARKET_PRICES_SIZE = 2
        private const val KEY_RECENT_MARKET_PRICES = "recent_market_prices"

    }

    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun insertRecentMarketPrice(marketPrice: MarketPrice) {
        val recentMarketPrices = getRecentMarketPrices()

        recentMarketPrices.add(0, marketPrice)
        clampRecentMarketPrices(recentMarketPrices)

        val editor = sp.edit()
        editor.putString(KEY_RECENT_MARKET_PRICES, gson.toJson(recentMarketPrices))
        editor.apply()
    }

    fun getRecentMarketPrices(): MutableList<MarketPrice?> {
        val recentMarketPricesStr = sp.getString(KEY_RECENT_MARKET_PRICES, "[]")

        val listType = object : TypeToken<MutableList<MarketPrice?>>() {}.type
        var recentMarketPrices = gson.fromJson<MutableList<MarketPrice?>>(recentMarketPricesStr, listType)

        clampRecentMarketPrices(recentMarketPrices)

        return recentMarketPrices
    }

    private fun clampRecentMarketPrices(recentMarketPrices: MutableList<MarketPrice?>): MutableList<MarketPrice?> {
        when {
            recentMarketPrices.size < RECENT_MARKET_PRICES_SIZE -> { // increase size
                val increaseBy = RECENT_MARKET_PRICES_SIZE - recentMarketPrices.size
                for (i in 0..increaseBy) {
                    recentMarketPrices.add(null)
                }
            }
            recentMarketPrices.size > RECENT_MARKET_PRICES_SIZE -> // reduce size
                return recentMarketPrices.subList(0, RECENT_MARKET_PRICES_SIZE - 1)
        }

        return recentMarketPrices
    }

}