package com.labs.sauti.view_model

import android.view.View
import android.webkit.WebStorage
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.model.trade_info.RegulatedGood
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.repository.SautiRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class  TradeInfoViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val tradeInfoLanguage: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoCategory: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoFirstSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoSecondSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoThirdSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoFourthSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()
    private val tradeInfoFifthSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()


    private val searchRegulatedGoodLiveData by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoProcedure by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoDocuments by lazy { MutableLiveData<TradeInfo>() }
    private val searchTradeInfoAgencies by lazy { MutableLiveData<TradeInfo>() }

    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getTradeInfoLangueLiveData() : LiveData<String> = tradeInfoLanguage
    fun getTradeInfoCategory() : LiveData<String> = tradeInfoCategory
    fun getTradeInfoFirstSpinnerContent(): LiveData<List<String>> = tradeInfoFirstSpinnerContent
    fun getTradeInfoSecondSpinnerContent() : LiveData<List<String>> = tradeInfoSecondSpinnerContent
    fun getTradeInfoThirdSpinnerContent() : LiveData<List<String>> = tradeInfoThirdSpinnerContent
    fun getTradeInfoFourthSpinnerContent() : LiveData<List<String>> = tradeInfoFourthSpinnerContent
    fun getTradeInfoFifthSpinnerContent() : LiveData<List<String>> = tradeInfoFifthSpinnerContent


    fun getSearchRegulatedGoodsLiveData(): LiveData<TradeInfo> = searchRegulatedGoodLiveData
    fun getSearchTradeInfoProcedure(): LiveData<TradeInfo> = searchTradeInfoProcedure
    fun getSearchTradeInfoDocuments(): LiveData<TradeInfo> = searchTradeInfoDocuments
    fun getSearchTradeInfoAgencies(): LiveData<TradeInfo> = searchTradeInfoAgencies

    fun searchRegulatedGoods(language: String, country: String, regulatedType: String) {

        when(regulatedType) {
            "Prohibited goods"->(
                    addDisposable(sautiRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.prohibiteds.forEach { pro ->
                                list.add(pro.name)
                            }

                            TradeInfo("These commodities are prohibited",
                                "Prohibited Goods",
                                list)
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
                    addDisposable(sautiRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.restricteds.forEach { rest ->
                                list.add(rest.name)
                            }

                            TradeInfo("These commodities are restricted:",
                                "Restricted Goods",
                                list)
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
                    addDisposable(sautiRepository.searchRegulatedGoods(language, country)
                        .map
                        {
                            val list = mutableListOf<String>()
                            it.sensitives.forEach { sensitive ->
                                list.add(sensitive.name)
                            }

                            TradeInfo("These commodities are sensitive:",
                                "Sensitive Goods",
                                list)
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
            addDisposable(sautiRepository.getRegulatedGoodsCountries(language).subscribeOn(Schedulers.io()).subscribe(
                {
                    tradeInfoFirstSpinnerContent.postValue(it)
                },
                {
                    errorLiveData.postValue(it)
                })
            )
        } else {
            addDisposable(sautiRepository.getTradeInfoProductCategory(language).subscribeOn(Schedulers.io()).subscribe(
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

            addDisposable(sautiRepository.getTradeInfoProductProducts(language, firstChoice).subscribeOn(Schedulers.io()).subscribe(
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

        addDisposable(sautiRepository.getTradeInfoOrigin(language, category, product)
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

        addDisposable(sautiRepository.getTradeInfoDestination(language, category, product, origin)
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

    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(sautiRepository) as T
        }
    }

}




//        when(tradeInfoCategory.value) {
//            "Border Procedures" -> (
//                addDisposable(sautiRepository.getSelectedLanguage().subscribeOn(Schedulers.io()).subscribe(
//                    { s ->
//                        tradeInfoLanguage.postValue(s.toUpperCase())
//                        sautiRepository.getTradeInfoProductCategory(s.toUpperCase()).subscribe(
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
//            "Border Procedures" ->{ addDisposable(sautiRepository.getTradeInfoProductCategory(tradeInfoLanguage.value.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
//                {tradeInfoFirstSpinnerContent.postValue(it)},
//                {errorLiveData.postValue(it)}
//            ))
//            }
//
//
//        }
//    }

//fun getFirstSpinnerContent()


