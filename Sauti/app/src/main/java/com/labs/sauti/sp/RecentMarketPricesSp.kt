package com.labs.sauti.sp

import android.content.Context
import com.google.gson.Gson
import com.labs.sauti.model.MarketPrice
import com.google.gson.reflect.TypeToken


// TODO should this be in the DB?
class RecentMarketPricesSp(private val context: Context, private val gson: Gson) {

    companion object {
        private const val SP_NAME = "recent_market_prices"
        private const val RECENT_MARKET_PRICES_SIZE = 10
        private const val KEY_RECENT_MARKET_PRICES = "recent_market_prices"

    }

    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    fun insertRecentMarketPrice(marketPrice: MarketPrice) {
        var recentMarketPrices = getRecentMarketPrices()

        recentMarketPrices.add(0, marketPrice) // insert at 0

        if (recentMarketPrices.size > RECENT_MARKET_PRICES_SIZE) {
            recentMarketPrices =  recentMarketPrices.subList(0, RECENT_MARKET_PRICES_SIZE - 1)
        }

        val editor = sp.edit()
        editor.putString(KEY_RECENT_MARKET_PRICES, gson.toJson(recentMarketPrices))
        editor.apply()
    }

    fun getRecentMarketPrices(): MutableList<MarketPrice> {
        val recentMarketPricesStr = sp.getString(KEY_RECENT_MARKET_PRICES, "[]")

        val listType = object : TypeToken<MutableList<MarketPrice>>() {}.type
        return gson.fromJson(recentMarketPricesStr, listType)
    }

}