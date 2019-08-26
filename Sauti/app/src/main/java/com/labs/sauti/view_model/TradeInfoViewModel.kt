package com.labs.sauti.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.exchange_rate.ExchangeRateData
import com.labs.sauti.model.trade_info.RequiredDocument
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import com.labs.sauti.repository.TradeInfoRepository
import io.reactivex.schedulers.Schedulers


class  TradeInfoViewModel(private val tradeInfoRepository: TradeInfoRepository): BaseViewModel() {

    private val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val tradeInfoLanguage: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoCategory: MutableLiveData<String> = MutableLiveData()
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

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getTradeInfoLangueLiveData() : LiveData<String> = tradeInfoLanguage
    fun getTradeInfoCategory() : LiveData<String> = tradeInfoCategory
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

    fun searchTaxCalculations(language: String, category: String, product: String, origin: String, dest: String, value: Double, currencyFrom: String, currencyTo: String, rate: Double) {
        addDisposable(tradeInfoRepository.searchTradeInfoTaxes(language, category, product, origin, dest, value)
            .map {
                TradeInfoTaxes(product,
                    currencyFrom,
                    currencyTo,
                    value,
                    it,
                    rate)
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
                    tradeInfoAgencies = it,
                    tradeInfoCountry = destChoice
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
                    tradeInfoProcedure = it
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
                val docsList = mutableListOf<RequiredDocument>()
                it.forEach{doc ->
                    docsList.add(doc)
                }

                TradeInfo(tradeinfoTopic = "Required Documents", tradeinfoTopicExpanded = "Push to View More Information About The Document", tradeInfoDocs = docsList)
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
                    addDisposable(tradeInfoRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.prohibiteds.forEach { pro ->
                                list.add(pro.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are prohibited",
                                list, regulatedType = "Prohibited Goods")
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
                    addDisposable(tradeInfoRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.restricteds.forEach { rest ->
                                list.add(rest.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are restricted",
                                list,
                                regulatedType = "Restricted Goods")
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
                    addDisposable(tradeInfoRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.sensitives.forEach { sensitive ->
                                list.add(sensitive.name)
                            }

                            TradeInfo("Regulated Goods",
                                "These commodities are sensitive",
                                list,
                                regulatedType = "Sensitive Goods")
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
        tradeInfoLanguage.value = lang.toUpperCase()
    }

    fun setFirstSpinnerContent(cat: String? = null) {

        if (cat == null) {
            tradeInfoCategory.value = "Border Procedures"
        } else {
            tradeInfoCategory.value = cat
        }

        val language = tradeInfoLanguage.value as String


        //TODO: String will not be hardcoded and turned into resource with translations
        if(tradeInfoCategory.value == "Regulated Goods") {
            addDisposable(tradeInfoRepository.getRegulatedGoodsCountries(language).subscribeOn(Schedulers.io()).subscribe(
                {
                    tradeInfoFirstSpinnerContent.postValue(it)
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

    fun  setTaxCalcCurrencySpinnerContent(){
        addDisposable(tradeInfoRepository.getTaxInfoCurrency()
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




//        when(tradeInfoCategory.value) {
//            "Border Procedures" -> (
//                addDisposable(tradeInfoRepository.getSelectedLanguage().subscribeOn(Schedulers.io()).subscribe(
//                    { s ->
//                        tradeInfoLanguage.postValue(s.toUpperCase())
//                        tradeInfoRepository.getTradeInfoProductCategory(s.toUpperCase()).subscribe(
//                            {tradeInfoFirstSpinnerContent.postValue(it)},
//                            {errorLiveData.postValue(it)}
//                        )
//                    },
//                    {
//                        errorLiveData.postValue(it)
//                    }
//                ))
//                    )
//            "Required Documents"->( tradeInfoFirstSpinnerContent.postValue(listOf())
//                    )
//            "Border Agencies"->{}
//            "Regulated Goods"->{}
//        }



//Check the tradeinfo
//    fun loadFirstSpinnerContent() {
//
//        val lang = tradeInfoLanguage.value.toString()
//        val cat = tradeInfoCategory.value.toString()
//        when(tradeInfoCategory.value.toString()) {
//            "Border Procedures" ->{ addDisposable(tradeInfoRepository.getTradeInfoProductCategory(tradeInfoLanguage.value.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {tradeInfoFirstSpinnerContent.postValue(it)},
//                {errorLiveData.postValue(it)}
//            ))
//            }
//
//
//        }
//    }

//fun getFirstSpinnerContent()


