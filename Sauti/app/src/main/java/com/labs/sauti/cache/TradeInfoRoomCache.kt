package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.trade_info.Procedure
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoData
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


//Access to the DAO
class TradeInfoRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : TradeInfoCache {

    override fun getTIProductCategories(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProductCategories(language)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIProductProducts(
        language: String,
        category: String
    ): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProducts(language,category)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIProductOrigin(
        language: String,
        category: String,
        product: String
    ): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoOrigin(language, category, product)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIDests(
        language: String,
        category: String,
        product: String,
        origin: String
    ): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoDestination(language, category, product, origin)
            .subscribeOn(Schedulers.io())
    }

    override fun saveTIProcedures(borderProcedure: TradeInfoData): Completable {

        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProcedures(
            borderProcedure.language!!,
            borderProcedure.productCat!!,
            borderProcedure.product!!,
            borderProcedure.origin!!,
            borderProcedure.dest!!,
            borderProcedure.value!!)
            .doOnSuccess{
                Completable.fromCallable { sautiRoomDatabase.tradeInfoDao().insertThenDeleteProcedures(it, borderProcedure) }.blockingAwait()
            }.onErrorResumeNext{
                sautiRoomDatabase.tradeInfoDao().insert(borderProcedure).flatMapSingle { Single.just(borderProcedure) }
            }
            .flatMapCompletable {
                Completable.complete()
            }
    }

    override fun searchTIProcedures(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        val valueString = if (value < 2000) "under2000USD" else "over2000USD"
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProceduresList(language, category, product, origin, dest, valueString)
            .map {
                TradeInfoData(
                    language = language,
                    productCat =  category,
                    product =  product,
                    origin =  origin,
                    dest = dest,
                    value = valueString,
                    procedures = it
                )
            }
            .doOnError{
                val x = 1
                val y = it
            }
            .subscribeOn(Schedulers.io())
    }

    //sautiRoomDatabase.tradeInfoDao().getTradeInfoProcedures(language, category, product, origin, dest, valueString)


}