package com.labs.sauti.cache

import com.labs.sauti.db.SautiRoomDatabase
import com.labs.sauti.model.trade_info.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


//Access to the DAO
class TradeInfoRoomCache(private val sautiRoomDatabase: SautiRoomDatabase) : TradeInfoCache {

    override fun getTwoRecentTradeInfoModels(): Single<MutableList<TradeInfoData>> {
        return sautiRoomDatabase.tradeInfoDao().getTwoMostRecentTradeInfo()
            .subscribeOn(Schedulers.io())
    }

    override fun getRegulatedCountries(language: String): Single<MutableList<String>> {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedCountries(language)
            .subscribeOn(Schedulers.io())
    }

    override fun saveRegulatedProhibiteds(prohibited: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getRegulatedProhibited(
            prohibited.language!!,
            prohibited.regulatedCountry!!)
            .doOnSuccess {
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(prohibited)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
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
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(restricted)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
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
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(sensitive)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
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
        return sautiRoomDatabase
            .tradeInfoDao()
            .getTradeInfoDestination(
                language,
                category,
                product,
                origin)
            .subscribeOn(Schedulers.io())
    }

    override fun getTIUserCurrency(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String
    ): Single<MutableList<String>> {
        return sautiRoomDatabase
            .tradeInfoDao()
            .getTaxCalculatorUserCurrency(
                language,
                category,
                product,
                origin)
            .subscribeOn(Schedulers.io())
    }

    override fun saveTIProcedures(borderProcedure: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao()
            .getTradeInfoProcedures(
                borderProcedure.language!!,
                borderProcedure.productCat!!,
                borderProcedure.product!!,
                borderProcedure.origin!!,
                borderProcedure.dest!!,
                borderProcedure.value!!)
            .doOnSuccess{
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(borderProcedure)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
            }
            .onErrorResumeNext{
                sautiRoomDatabase
                    .tradeInfoDao()
                    .insert(borderProcedure)
                    .flatMapSingle { Single.just(borderProcedure) }
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
        return sautiRoomDatabase
            .tradeInfoDao()
            .getTradeInfoProcedures(
                language,
                category,
                product,
                origin,
                dest,
                valueString)
            .subscribeOn(Schedulers.io())
    }

    override fun saveTIDocuments(requiredDocument: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoRequiredDocuments(
            requiredDocument.language!!,
            requiredDocument.productCat!!,
            requiredDocument.product!!,
            requiredDocument.origin!!,
            requiredDocument.dest!!,
            requiredDocument.value!!)
            .doOnSuccess {
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(requiredDocument)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
            }
            .onErrorResumeNext {
                sautiRoomDatabase.tradeInfoDao().insert(requiredDocument).flatMapSingle { Single.just(requiredDocument) }
            }
            .flatMapCompletable {
                Completable.complete()
            }

    }

    override fun searchTIDocuments(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        val valueString = if (value < 2000) "under2000USD" else "over2000USD"
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoRequiredDocuments(language, category, product, origin, dest, valueString)
    }

    override fun saveTIAgencies(agencies: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoBorderAgencies(
            agencies.language!!,
            agencies.productCat!!,
            agencies.product!!,
            agencies.origin!!,
            agencies.dest!!,
            agencies.value!!)
            .doOnSuccess{
                sautiRoomDatabase.tradeInfoDao().insertTradeInfo(agencies)
                    .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
            }
            .onErrorResumeNext{ sautiRoomDatabase.tradeInfoDao().insert(agencies).flatMapSingle{ Single.just(agencies) } }
            .flatMapCompletable{ Completable.complete() }
    }

    override fun searchTIAgencies(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        val valueString = if (value < 2000) "under2000USD" else "over2000USD"
        return sautiRoomDatabase
            .tradeInfoDao()
            .getTradeInfoBorderAgencies(
                language,
                category,
                product,
                origin,
                dest,
                valueString)
            .subscribeOn(Schedulers.io())
    }

    override fun saveTITaxes(taxes: TradeInfoData): Completable {
        return sautiRoomDatabase.tradeInfoDao().getTradeInfoTaxes(
            taxes.language!!,
            taxes.productCat!!,
            taxes.product!!,
            taxes.origin!!,
            taxes.dest!!,
            taxes.value!!,
            taxes.userCurrency!!,
            taxes.destinationCurrency!!)
            .doOnSuccess {
            sautiRoomDatabase.tradeInfoDao().insertTradeInfo(taxes)
                .andThen(sautiRoomDatabase.tradeInfoDao().delete(it)).blockingAwait()
            }
            .onErrorResumeNext { sautiRoomDatabase.tradeInfoDao().insert(taxes).flatMapSingle { Single.just(taxes) } }
            .flatMapCompletable { Completable.complete() }
    }

    override fun searchTITaxes(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double,
        userCurrency: String,
        destCurrency: String
    ): Single<TradeInfoData>{
        val valueString = if (value < 2000) "under2000USD" else "over2000USD"
        return sautiRoomDatabase
            .tradeInfoDao()
            .getTradeInfoTaxes(
                language,
                category,
                product,
                origin,
                dest,
                valueString,
                userCurrency,
                destCurrency
            ).subscribeOn(Schedulers.io())
    }
}