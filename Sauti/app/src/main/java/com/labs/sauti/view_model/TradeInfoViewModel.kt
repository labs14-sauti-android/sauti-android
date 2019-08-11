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
    private val tradeInfoCategory by lazy {MutableLiveData<String>()}



    fun getErrorLiveData(): LiveData<Throwable> = errorLiveData


    class Factory(private val sautiRepository: SautiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TradeInfoViewModel(sautiRepository) as T
        }
    }



}