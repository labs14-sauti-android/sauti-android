package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import com.labs.sauti.model.trade_info.toTradeInfo
import com.labs.sauti.model.trade_info.toTradeInfoTaxes
import com.labs.sauti.repository.TradeInfoRepository
import io.reactivex.schedulers.Schedulers


class  TradeInfoViewModel(private val tradeInfoRepository: TradeInfoRepository): BaseViewModel() {

    private val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val tradeInfoLanguage: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoCategory: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoBorderCountries : MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoFirstSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoSecondSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoThirdSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoFourthSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoFifthSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val taxCalcCurrencySpinnerContent: MutableLiveData<MutableList<ExchangeRateData>> = MutableLiveData()
    private val taxCalcConversionTextContent: MutableLiveData<String> = MutableLiveData()


    private val searchRegulatedGoodLiveData by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoProcedure by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoDocuments by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoAgencies by lazy { MutableLiveData<TradeInfo>() }
    private val searchTaxCalculator by lazy { MutableLiveData<TradeInfoTaxes>() }
    private val searchTradeInfoRecents by lazy {  MutableLiveData<List<TradeInfo>>() }
    private val searchTaxCalcRecents by lazy { MutableLiveData<List<TradeInfoTaxes>>() }


    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getTradeInfoBorderCountries() : LiveData<List<String>> = tradeInfoBorderCountries
    fun getTradeInfoFirstSpinnerContent(): LiveData<List<String>> = tradeInfoFirstSpinnerContent
    fun getTradeInfoSecondSpinnerContent() : LiveData<List<String>> = tradeInfoSecondSpinnerContent
    fun getTradeInfoThirdSpinnerContent() : LiveData<List<String>> = tradeInfoThirdSpinnerContent
    fun getTradeInfoFourthSpinnerContent() : LiveData<List<String>> = tradeInfoFourthSpinnerContent
    fun getTradeInfoFifthSpinnerContent() : LiveData<List<String>> = tradeInfoFifthSpinnerContent
    fun getTaxCalcCurrentSpinnerContent(): LiveData<MutableList<ExchangeRateData>> = taxCalcCurrencySpinnerContent
    fun getTaxCalcConversionTextConent(): LiveData<String> = taxCalcConversionTextContent


    fun getSearchRegulatedGoodsLiveData(): LiveData<TradeInfo> = searchRegulatedGoodLiveData
    fun getSearchTradeInfoProcedure(): LiveData<TradeInfo> = searchTradeInfoProcedure
    fun getSearchTradeInfoDocuments(): LiveData<TradeInfo> = searchTradeInfoDocuments
    fun getSearchTradeInfoAgencies(): LiveData<TradeInfo> = searchTradeInfoAgencies
    fun getSearchTaxCalculations(): LiveData<TradeInfoTaxes> = searchTaxCalculator
    fun getSearchTradeInfoRecents(): LiveData<List<TradeInfo>> = searchTradeInfoRecents
    fun getSearchTaxCalcRecents(): LiveData<List<TradeInfoTaxes>> = searchTaxCalcRecents

    fun searchRecentTaxCalculator(){
        addDisposable(tradeInfoRepository.getTwoRecentTaxCalculations()
            .map {
                val uiList = mutableListOf<TradeInfoTaxes>()
                it.forEach{pre->
                    val convert = pre.toTradeInfoTaxes()
                    uiList.add(convert)
                }
                uiList

            }
            .subscribe(
                {
                    searchTaxCalcRecents.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    fun searchRecentTradeInfo(){
        addDisposable(tradeInfoRepository.getTwoRecentTradeInfo()
            .map {
                val uiList = mutableListOf<TradeInfo>()
                it.forEach{tiData->
                    val convert = tiData.toTradeInfo()
                    uiList.add(convert)
                }
                uiList
            }
            .subscribe(
                {
                    searchTradeInfoRecents.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    //TODO: Finish tax calc
    fun searchTaxCalculations(language: String, category: String, product: String, origin: String, dest: String, value: Double, currencyUser: String, currencyTo: String, rate: Double, valueCheck: Double) {
        addDisposable(tradeInfoRepository.searchTradeInfoTaxes(language, category, product, origin, dest, valueCheck, currencyUser, currencyTo, value, rate)
            .map {
                val search = TradeInfoTaxes(product,
                    currencyUser,
                    currencyTo,
                    value,
                    taxList = it.taxes!!,
                    rate = it.userToDestRate!!)

                search.getTaxesConversions()

                search
            }
            .subscribe(
                {
                    searchTaxCalculator.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    fun searchBorderAgencies(language: String, category: String, product: String, origin: String, dest: String, value: Double, destChoice: String){
        addDisposable(tradeInfoRepository.searchTradeInfoBorderAgencies(language, category, product, origin, dest, value)
            .map {
                TradeInfo("Border Agencies",
                    "Push to View More Information About The Agency",
                    tradeInfoAgencies = it.relevantAgencyData,
                    tradeInfoCountry = destChoice,
                    tradeInfoID = it.id
                    )
            }
            .subscribe(
                {
                    searchTradeInfoAgencies.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    fun searchBorderProcedures(language: String, category: String, product: String, origin: String, dest: String, value: Double, destChoice: String) {
        addDisposable(tradeInfoRepository.searchTradeInfoBorderProcedures(language, category, product, origin, dest, value)
            .map {
                TradeInfo("Border Procedures",
                    """To $destChoice""",
                    tradeInfoProcedure = it.procedures,
                    tradeInfoID =  it.id
                )
            }
            .subscribe(
                {
                    searchTradeInfoProcedure.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    fun searchRequiredDocuments(language: String, category: String, product: String, origin: String, dest: String, value: Double) {
        addDisposable(tradeInfoRepository.searchTradeInfoRequiredDocuments(language, category, product, origin, dest, value)
            .map
            {
                TradeInfo(tradeinfoTopic = "Required Documents",
                    tradeinfoTopicExpanded = "Push to View More Information About The Document",
                    tradeInfoDocs = it.requiredDocumentData,
                    tradeInfoID = it.id
                )
            }
            .subscribe(
                {
                    searchTradeInfoDocuments.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                }
            ))
    }

    fun searchRegulatedGoods(language: String, country: String, regulatedType: String) {

        when(regulatedType) {
            "Prohibited goods"->(
                    addDisposable(tradeInfoRepository.searchRegulatedProhibiteds(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.prohibiteds?.forEach { pro ->
                                list.add(pro.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are prohibited",
                                list,
                                regulatedType = "Prohibited Goods",
                                tradeInfoID = it.id)
                        }
                        .subscribe(
                            {
                                searchRegulatedGoodLiveData.postValue(it)
                            },
                            {
                                errorLiveData.postValue(it)
                            }
                        ))
                    )

            "Restricted goods"-> (
                    addDisposable(tradeInfoRepository.searchRegulatedRestricteds(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.restricteds?.forEach { rest ->
                                list.add(rest.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are restricted",
                                list,
                                regulatedType = "Restricted Goods",
                                tradeInfoID = it.id)
                        }
                        .subscribe(
                            {
                                searchRegulatedGoodLiveData.postValue(it)
                            },
                            {
                                errorLiveData.postValue(it)
                            }
                        ))
                    )
            "Sensitive goods"->(
                    addDisposable(tradeInfoRepository.searchRegulatedSensitives(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.sensitives?.forEach { sensitive ->
                                list.add(sensitive.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are sensitive",
                                list,
                                regulatedType = "Sensitive Goods",
                                tradeInfoID = it.id)
                        }
                        .subscribe(
                            {
                                searchRegulatedGoodLiveData.postValue(it)
                            },
                            {
                                errorLiveData.postValue(it)
                            }
                        ))
                    )
        }

    }

    fun setLanguage(lang : String) {
        tradeInfoLanguage.value = lang
    }

    fun setFirstSpinnerContent(cat: String? = null) {

         tradeInfoCategory.value = cat


        val language = tradeInfoLanguage.value as String


        //TODO: String will not be hardcoded and turned into resource with translations
        if(cat == "Regulated Goods") {
            addDisposable(tradeInfoRepository.getRegulatedGoodsCountries(language).subscribeOn(Schedulers.io()).subscribe(
                {
                    //TODO: Setup a whole new obversable just for Regulated goods.
                    tradeInfoBorderCountries.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
            )
        } else {
            addDisposable(tradeInfoRepository.getTradeInfoProductCategory(language).subscribeOn(Schedulers.io()).subscribe(
                {
                    tradeInfoFirstSpinnerContent.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
            )
        }
    }

    fun setSecondSpinnerContent(firstChoice : String) {

        if(tradeInfoCategory.value == "Regulated Goods") {

            val regulationType = listOf("Prohibited goods", "Restricted goods", "Sensitive goods")
            tradeInfoSecondSpinnerContent.postValue(regulationType)

        } else {
            val language = tradeInfoLanguage.value as String

            addDisposable(tradeInfoRepository.getTradeInfoProductProducts(language, firstChoice).subscribeOn(Schedulers.io()).subscribe(
                {
                    tradeInfoSecondSpinnerContent.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
            )
        }

    }

    fun setThirdSpinnerContent(language: String, category: String, product: String) {

        addDisposable(tradeInfoRepository.getTradeInfoOrigin(language, category, product)
            .subscribeOn(Schedulers.io()).subscribe(
            {
                tradeInfoThirdSpinnerContent.postValue(it)
            },
            {
                errorLiveData.postValue(it)
            })
        )
    }


    fun setFourthSpinnerContent(language: String, category: String, product: String, origin: String) {

        addDisposable(tradeInfoRepository.getTradeInfoDestination(language, category, product, origin)
            .subscribeOn(Schedulers.io()).subscribe(
                {
                    tradeInfoFourthSpinnerContent.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
        )
    }

    fun setFifthSpinnerContent() {

        val itemValue = listOf("Greater than USD 2000 \n(approx KES 206540/UGX 7390200)", "Less than USD 2000 \n(approx KES 206540/UGX 7390200)")
        tradeInfoFifthSpinnerContent.postValue(itemValue)

    }

    fun  setTaxCalcCurrencySpinnerContent(language: String, category: String, product: String, origin: String, dest: String){
        addDisposable(tradeInfoRepository.getTaxInfoCurrency(language, category, product, origin, dest)
/*            .map {
                val currencies = mutableListOf<String>()
                it.forEach{ cur ->
                    currencies.add(cur.currency as String)
                }
                currencies
            }*/
            .subscribe(
                {
                    taxCalcCurrencySpinnerContent.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
        )
    }

    fun setTaxCalcConversionTextConent(currencyTo: String) {
        taxCalcConversionTextContent.postValue(currencyTo)

    }

    class Factory(private val tradeInfoRepository: TradeInfoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(tradeInfoRepository) as T
        }
    }

}