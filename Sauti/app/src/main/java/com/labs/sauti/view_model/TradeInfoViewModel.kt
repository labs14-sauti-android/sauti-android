package com.labs.sauti.view_model

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SautiRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class  TradeInfoViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val tradeInfoLanguage: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoCategory: MutableLiveData<String> = MutableLiveData()
    private val tradeInfoFirstSpinnerContent: MutableLiveData<List<String>> = MutableLiveData()


    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getTradeInfoLangueLiveData() : LiveData<String> = tradeInfoLanguage
    fun getTradeInfoCategory() : LiveData<String> = tradeInfoCategory
    fun getTradeInfoFirstSpinnerContent(): LiveData<List<String>> = tradeInfoFirstSpinnerContent



    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(sautiRepository) as T
        }
    }


    //This sets the trade info and checks the language then places that in the
    //viewmodel so it can be pulled later.
    fun setTradeInfoCategory(cat: String) {
        tradeInfoCategory.value = cat

    }



    fun setLanguage(lang : String) {
        tradeInfoLanguage.value = lang.toUpperCase()
    }

    fun setFirstSpinnerContent(cat: String? = null) {

        if (cat == null) {
            tradeInfoCategory.value = "Border Procedures"
        } else {
            tradeInfoCategory.value = cat as String
        }

        val language = tradeInfoLanguage.value as String

        when(tradeInfoCategory.value) {
            "Border Procedures" -> (
                    addDisposable(sautiRepository.getTradeInfoProductCategory(language).subscribeOn(Schedulers.io()).subscribe(
                        {
                            tradeInfoFirstSpinnerContent.postValue(it)
                        },
                        {
                            errorLiveData.postValue(it)
                        }
                    ))
                    )
            "Required Documents"->(
                    addDisposable(sautiRepository.getTradeInfoProductCategory(language).subscribeOn(Schedulers.io()).subscribe(
                        {
                            tradeInfoFirstSpinnerContent.postValue(it)
                        },
                        {
                            errorLiveData.postValue(it)
                        }
                    ))
                    )
            "Border Agencies"->{}
            "Regulated Goods"->{}
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

    }

    //Check the tradeinfo
    fun loadFirstSpinnerContent() {

        val lang = tradeInfoLanguage.value.toString()
        val cat = tradeInfoCategory.value.toString()
        when(tradeInfoCategory.value.toString()) {
            "Border Procedures" ->{ addDisposable(sautiRepository.getTradeInfoProductCategory(tradeInfoLanguage.value.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {tradeInfoFirstSpinnerContent.postValue(it)},
                {errorLiveData.postValue(it)}
            ))
            }


        }
    }

    //fun getFirstSpinnerContent()





}