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
            .doOnSuccess{
                it.sort()
            }
            .subscribeOn(Schedulers.io())
    }


    //TODO: Room
    override fun getTradeInfoProductProducts(language: String, category: String): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoProducts(language, category)
            .subscribeOn(Schedulers.io())
    }

    //TODO: Room

    override fun getTradeInfoOrigin(language: String, category: String, product: String): Single<MutableList<String>> {
        return sautiApiService.getTradeInfoOrigins(language, category, product)
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
            .subscribeOn(Schedulers.io())
    }

    override fun getTaxInfoCurrency() : Single<MutableList<ExchangeRateData>> {
        return sautiApiService.getExchangeRates()
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
            .doOnSuccess {
                tradeInfoRoomCache.saveTIProcedures(it).blockingAwait()
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
    ): Single<MutableList<RequiredDocument>> {
        return sautiApiService.searchTradeInfoRequiredDocuments(language, category, product, origin, dest, value)
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
    ): Single<MutableList<BorderAgency>> {
        return sautiApiService.searchTradeInfoBorderAgencies(language, category, product, origin, dest, value)
            .subscribeOn(Schedulers.io())
    }

    override fun searchTradeInfoTaxes(
        language: String,
        category: String,
        product: String,
        origin: String,
        dest: String,
        value: Double
    ): Single<MutableList<Taxes>> {
        return sautiApiService.searchTradeInfoTaxes(language, category, product, origin, dest, value)
            .subscribeOn(Schedulers.io())
    }


    override fun getRegulatedGoodsCountries(language: String): Single<MutableList<String>> {
        return sautiApiService.getRegulatedGoodsCountries(language)
            .subscribeOn(Schedulers.io())

    }

    //TODO: Save in room and error cases when not online
    override fun searchRegulatedGoods(language: String, country: String): Single<RegulatedGoodData> {
        return sautiApiService.searchRegulatedData(language, country)
            .subscribeOn(Schedulers.io())
    }

}