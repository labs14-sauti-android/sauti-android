package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.trade_info.*
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.internal.operators.single.SingleJust
import io.reactivex.schedulers.Schedulers


//Access to the DAO
class TradeInfoRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : TradeInfoCache {

    override fun getRegulatedCountries(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedCountries(language)

            .subscribeOn(Schedulers.io())
    }

    override fun saveRegulatedProhibiteds(prohibited: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedProhibited(
            prohibited.language!!,
            prohibited.regulatedCountry!!)
            .doOnSuccess {
                Completable.fromCallable{sautiRoomDatabase.tradeInfoDao().insertThenDeleteProcedures(it, prohibited)}.blockingAwait()
            }.onErrorResumeNext{
                sautiRoomDatabase.tradeInfoDao().insert(prohibited).flatMapSingle { Single.just(prohibited) }
            }.flatMapCompletable {
                Completable.complete()
            }
    }

    override fun searchRegulatedProhibiteds(
        language: String,
        regulatedCountry: String
    ): Single<TradeInfoData> {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedProhibited(language, regulatedCountry)
            .subscribeOn(Schedulers.io())
    }

    override fun saveRegulatedRestricteds(restricted: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedRestricted(
            restricted.language!!,
            restricted.regulatedCountry!!)
            .doOnSuccess {
                Completable.fromCallable{sautiRoomDatabase.tradeInfoDao().insertThenDeleteProcedures(it, restricted)}.blockingAwait()
            }.onErrorResumeNext {
                sautiRoomDatabase.tradeInfoDao().insert(restricted).flatMapSingle { Single.just(restricted) }
            }.flatMapCompletable {
                Completable.complete()
            }
    }

    override fun searchRegulatedRestricteds(
        language: String,
        regulatedCountry: String
    ): Single<TradeInfoData> {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedRestricted(language, regulatedCountry)
            .subscribeOn(Schedulers.io())
    }

    override fun saveRegulatedSensitives(sensitive: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedSensitives(
            sensitive.language!!,
            sensitive.regulatedCountry!!)
            .doOnSuccess {
                Completable.fromCallable {sautiRoomDatabase.tradeInfoDao().insertThenDeleteProcedures(it, sensitive)}.blockingAwait()
            }.onErrorResumeNext {
                sautiRoomDatabase.tradeInfoDao().insert(sensitive).flatMapSingle { Single.just(sensitive) }
            }.flatMapCompletable {
                Completable.complete()
            }
    }

    override fun searchRegulatedSensitives(
        language: String,
        regulatedCountry: String
    ): Single<TradeInfoData> {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedSensitives(language, regulatedCountry)
            .subscribeOn(Schedulers.io())
    }

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
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoProcedures(language, category, product, origin, dest, valueString)
            .subscribeOn(Schedulers.io())
    }

    //sautiRoomDatabase.tradeInfoDao().getTradeInfoProcedures(language, category, product, origin, dest, valueString)


}