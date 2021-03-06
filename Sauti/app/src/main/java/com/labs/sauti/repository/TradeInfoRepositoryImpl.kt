package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import com.labs.sauti.cache.*
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.*
import com.labs.sauti.sp.SettingsSp
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class TradeInfoRepositoryImpl(
    private val sautiApiService: SautiApiService,
    private val settingsSp: SettingsSp,
    private val tradeInfoRoomCache: TradeInfoRoomCache,
    private val tradeInfoSearchRoomCache: TradeInfoSearchRoomCache
) : TradeInfoRepository {

    override fun getTwoRecentTaxCalculations(): Single<MutableList<TradeInfoData>> {
        return tradeInfoRoomCache.getTwoRecentTaxCalcModels()
            .subscribeOn(Schedulers.io())
    }

    override fun getTwoRecentTradeInfo(): Single<MutableList<TradeInfoData>> {
        return tradeInfoRoomCache.getTwoRecentTradeInfoModels()
            .subscribeOn(Schedulers.io())
    }

    override fun getSelectedLanguage(): Single<String> {
        return Single.just(settingsSp.getSelectedLanguage())
            .subscribeOn(Schedulers.io())
    }

    override fun setSelectedLanguage(language: String): Completable {
        return Completable.fromCallable {
            settingsSp.setSelectedLanguage(language)
        }
    }

    override fun getTradeInfoProductCategory(language: String): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoCategories(language)
            .onErrorResumeNext{
                tradeInfoRoomCache.getTIProductCategories(language)
            }
            .subscribeOn(Schedulers.io())
    }


    //TODO: Room
    override fun getTradeInfoProductProducts(language: String, category: String): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoProducts(language, category)
            .onErrorResumeNext {
                tradeInfoRoomCache.getTIProductProducts(language, category)
            }
            .doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room

    override fun getTradeInfoOrigin(language: String, category: String, product: String): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoOrigins(language, category, product)
            .onErrorResumeNext {
                tradeInfoRoomCache.getTIProductOrigin(language, category, product)
            }
            .doOnSuccess { it.sort() }
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room
    override fun getTradeInfoDestination(
        language: String,
        category: String,
        product: String,
        origin: String
    ): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoDests(language, category, product, origin)
            .onErrorResumeNext {
                tradeInfoRoomCache.getTIDests(language, category, product, origin)
            }
            .doOnSuccess { it.sort() }
            .subscribeOn(Schedulers.io())
    }

    override fun getTaxInfoCurrency(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String
    ) : Single<MutableList<ExchangeRateData>> {
        return sautiApiService.getExchangeRates()
            .onErrorResumeNext {
                tradeInfoRoomCache.getTIUserCurrency(language, category, product, origin, dest)
                    .map {
                        val exchangeList = mutableListOf<ExchangeRateData>()

                        it.forEach{s->
                            val add = ExchangeRateData(currency = s, rate = 0.0)
                            exchangeList.add(add)
                        }

                        exchangeList
                    }
            }
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room
    override fun searchTradeInfoBorderProcedures(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        return sautiApiService.searchTradeInfoBorderProcedures(language, category, product, origin, dest, value)
            .map {
                val valueString = if (value < 2000) "under2000USD" else "over2000USD"
                TradeInfoData(System.currentTimeMillis(),
                language = language,
                productCat =  category,
                product =  product,
                origin =  origin,
                dest = dest,
                value = valueString,
                procedures = it)
            }
            .doOnSuccess{
                tradeInfoRoomCache.saveTIProcedures(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchTIProcedures(language, category, product, origin, dest, value)
            }
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room
    override fun searchTradeInfoRequiredDocuments(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        return sautiApiService.searchTradeInfoRequiredDocuments(language, category, product, origin, dest, value)
            .map {
                val valueString = if (value < 2000) "under2000USD" else "over2000USD"
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    productCat =  category,
                    product =  product,
                    origin =  origin,
                    dest = dest,
                    value = valueString,
                    requiredDocumentData = it
                )
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveTIDocuments(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchTIDocuments(language, category, product, origin, dest, value)
            }
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room
    override fun searchTradeInfoBorderAgencies(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<TradeInfoData> {
        return sautiApiService.searchTradeInfoBorderAgencies(language, category, product, origin, dest, value)
            .map {
                val valueString = if (value < 2000) "under2000USD" else "over2000USD"
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    productCat =  category,
                    product =  product,
                    origin =  origin,
                    dest = dest,
                    value = valueString,
                    relevantAgencyData = it)
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveTIAgencies(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchTIAgencies(language, category, product, origin, dest, value)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun searchTradeInfoTaxes(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        valueCheck: Double,
        currencyUser: String,
        currencyTo: String,
        value: Double,
        exchangeRate: Double
    ): Single<TradeInfoData> {
        return sautiApiService.searchTradeInfoTaxes(language, category, product, origin, dest, valueCheck)
            .map {
                val valueString = if (valueCheck < 2000) "under2000USD" else "over2000USD"
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    productCat = category,
                    product = product,
                    origin = origin,
                    dest = dest,
                    value = valueString,
                    taxes = it,
                    userCurrency = currencyUser,
                    destinationCurrency = currencyTo,
                    userToDestRate = exchangeRate,
                    approximateValue = value
                    )
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveTITaxes(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchTITaxes(
                    language,
                    category,
                    product,
                    origin,
                    dest,
                    valueCheck,
                    currencyUser,
                    currencyTo
                )
            }
            .subscribeOn(Schedulers.io())
    }



    override fun getRegulatedGoodsCountries(language: String): Single<MutableList<String>> {
        return sautiApiService.getRegulatedGoodsCountries(language)
            .onErrorResumeNext {
                tradeInfoRoomCache.getRegulatedCountries(language)
            }. doOnSuccess {
                it.sort()
            }
            .subscribeOn(Schedulers.io())

    }

    override fun searchRegulatedGoods
                (language: String,
                 country: String,
                 regulatedType: String): Single<RegulatedGoodData> {
        return sautiApiService.searchRegulatedData(language, country)
            .subscribeOn(Schedulers.io())
    }

    override fun searchRegulatedProhibiteds(
        language: String,
        country: String
    ): Single<TradeInfoData> {
        return sautiApiService.searchRegulatedProhibiteds(language, country)
            .map {
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    regulatedCountry = country,
                    prohibiteds = it
                )
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveRegulatedProhibiteds(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchRegulatedProhibiteds(language, country)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun searchRegulatedRestricteds(
        language: String,
        country: String
    ): Single<TradeInfoData> {
        return sautiApiService.searchRegulatedRestricteds(language, country)
            .map {
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    regulatedCountry = country,
                    restricteds = it
                )
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveRegulatedRestricteds(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchRegulatedRestricteds(language, country)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun searchRegulatedSensitives(
        language: String,
        country: String
    ): Single<TradeInfoData> {
        return sautiApiService.searchRegulatedSensitives(language, country)
            .map {
                TradeInfoData(System.currentTimeMillis(),
                    language = language,
                    regulatedCountry = country,
                    sensitives = it
                )
            }
            .doOnSuccess {
                tradeInfoRoomCache.saveRegulatedSensitives(it).blockingAwait()
            }
            .onErrorResumeNext {
                tradeInfoRoomCache.searchRegulatedSensitives(language, country)
            }
            .subscribeOn(Schedulers.io())
    }
}