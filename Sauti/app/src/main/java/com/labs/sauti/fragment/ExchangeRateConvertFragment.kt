package com.labs.sauti.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.view_model.ExchangeRateViewModel
import kotlinx.android.synthetic.main.fragment_exchange_rate_convert.*
import javax.inject.Inject

class ExchangeRateConvertFragment : Fragment() {

    // TODO fullscreen

    private var onConversionCompletedListener: OnConversionCompletedListener? = null

    @Inject
    lateinit var exchangeRateViewModelFactory: ExchangeRateViewModel.Factory

    private lateinit var exchangeRateViewModel: ExchangeRateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getExchangeRateComponent().inject(this)
        exchangeRateViewModel = ViewModelProviders.of(this, exchangeRateViewModelFactory).get(ExchangeRateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_rate_convert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exchangeRateViewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()

            vs_from_currencies.displayedChild = 0
            vs_convert_loading.displayedChild = 0
        })

        exchangeRateViewModel.getCurrenciesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_from_currencies.displayedChild = 1
                vs_to_currencies.displayedChild = 1
            } else {
                vs_from_currencies.displayedChild = 0
                vs_to_currencies.displayedChild = 0

                val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
                adapter.add("")
                adapter.addAll(it.currencies!!)

                s_from_currencies.adapter = adapter
                s_to_currencies.adapter = adapter
            }
        })

        exchangeRateViewModel.getCurrencies()

        exchangeRateViewModel.getConversionViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_convert_loading.displayedChild = 1
            } else {
                vs_convert_loading.displayedChild = 0

                onConversionCompletedListener?.onConversionCompleted(it.conversionResult!!)

                fragmentManager!!.popBackStack()
            }
        })

        b_convert.setOnClickListener {
            convert()
        }
    }

    private fun setTranslatableTexts() {
        val ctx = LocaleHelper.createContext(context!!)

        t_how_much_do_you_want_to_exchange.text = ctx.getString(R.string.how_much_do_you_want_to_exchange)
    }

    private fun convert() {
        val amountStr = et_amount.text.toString()
        val amount = if (amountStr.isEmpty()) 0.0 else amountStr.toDouble()

        exchangeRateViewModel.convert(
            s_from_currencies.selectedItem as String,
            s_to_currencies.selectedItem as String,
            amount
        )
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (parentFragment is OnConversionCompletedListener) {
            onConversionCompletedListener = parentFragment as OnConversionCompletedListener
        } else {
            throw RuntimeException("parentFragment must implement OnConversionCompletedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onConversionCompletedListener = null
    }

    companion object {
        fun newInstance() =
            ExchangeRateConvertFragment()
    }

    interface OnConversionCompletedListener {
        fun onConversionCompleted(exchangeRateConversionResult: ExchangeRateConversionResult)
    }

}
