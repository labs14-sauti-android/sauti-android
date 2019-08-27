package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


//Access to the DAO
class TradeInfoRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : TradeInfoCache {

    override fun getTIProductCategories(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProductCategories(language)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIRegulatedGoodsCountries(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.regulatedGoodDao().getRegulatedGoodsCountries(language)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIRegulatedGoodsProhibited(
        language: String,
        country: String
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTIRegulatedGoodsRestricted(
        language: String,
        country: String
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTIRegulatedGoodsSensitive(
        language: String,
        country: String
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getTIProductProducts(
        language: String,
        category: String
    ): Single<MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTIProductOrigin(
        language: String,
        category: String,
        product: String
    ): Single<MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTIDests(
        language: String,
        category: String,
        product: String,
        origin: String
    ): Single<MutableList<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchTradeInfoBorderProcedures(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchTradeInfoRequiredDocuments(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchTradeInfoBorderAgencies(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchTradeInfoTaxCalculator(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoTaxes> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}