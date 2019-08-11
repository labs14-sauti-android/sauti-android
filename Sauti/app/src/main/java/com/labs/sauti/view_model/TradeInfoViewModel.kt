package com.labs.sauti.view_model

import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.labs.sauti.repository.SautiRepository


class  TradeInfoViewModel(private val sautiRepository: SautiRepository): BaseViewModel() {

    private val errorLiveData by lazy { MutableLiveData<Throwable>() }
    private val tradeInfoLanguage by lazy { MutableLiveData<String>() }
    private val tradeInfoCategory by lazy {MutableLiveData<String>()}



    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData
    fun getTradeInfoLangueLiveData() : LiveData<String> = tradeInfoLanguage


    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(sautiRepository) as T
        }
    }


    //This sets the trade info and checks the language then places that in the
    //viewmodel so it can be pulled later.
    fun setTradeInfoCategory(cat: String) {
        tradeInfoCategory.postValue(cat)
        addDisposable(sautiRepository.getSelectedLanguage().subscribe(
            {
                tradeInfoLanguage.postValue(it.toUpperCase())
            },
            {
                errorLiveData.postValue(it)
            }
        ))
    }

    //Check the tradeinfo
    fun getFirstSpinnerContent() {
        val lang = tradeInfoLanguage.value.toString()
        val category = tradeInfoCategory.value.toString()
        when(category) {

        }
    }




}