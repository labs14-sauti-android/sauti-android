package com.labs.sauti.fragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.view_model.ExchangeRateViewModel
import kotlinx.android.synthetic.main.fragment_exchange_rate.*
import kotlinx.android.synthetic.main.item_recent_exchange_rate.view.*
import javax.inject.Inject

class ExchangeRateFragment : Fragment(), ExchangeRateSearchFragment.OnConversionCompletedListener {

    @Inject
    lateinit var exchangeRateViewModelFactory: ExchangeRateViewModel.Factory

    private lateinit var exchangeRateViewModel: ExchangeRateViewModel

    private val recentConversionResultRootViews = mutableListOf<View>()
    private var selectedConversionResultRootView: View? = null

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
        return inflater.inflate(R.layout.fragment_exchange_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_details.visibility = View.GONE

        ll_recent_exchange_rates.children.iterator().forEach {
            recentConversionResultRootViews.add(it)
        }

        exchangeRateViewModel.getRecentConversionResultsViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_exchange_rates_loading.displayedChild = 1
            } else {
                vs_recent_exchange_rates_loading.displayedChild = 0
                it.recentConversionResults?.let { conversionResults ->
                    handleRecentConversionResults(conversionResults)
                }
            }
        })

        exchangeRateViewModel.getRecentConversionResults()

        b_convert.setOnClickListener {
            openExchangeRateConvertFragment()
        }
    }

    private fun setConversionResultDetails(exchangeRateConversionResult: ExchangeRateConversionResult) {
        val amountStr = String.format("%.2f", exchangeRateConversionResult.amount)
        val resultStr = String.format("%.2f", exchangeRateConversionResult.result)
        t_details_result.text = "$amountStr ${exchangeRateConversionResult.fromCurrency} is $resultStr ${exchangeRateConversionResult.toCurrency}"
        val toPerFromStr = String.format("%.2f", exchangeRateConversionResult.toPerFrom)
        t_details_to_per_from.text = "(1${exchangeRateConversionResult.fromCurrency} = $toPerFromStr ${exchangeRateConversionResult.toCurrency})"
    }

    private fun handleRecentConversionResults(conversionResults: MutableList<ExchangeRateConversionResult>) {
        recentConversionResultRootViews.forEachIndexed { index, view ->
            if (index < conversionResults.size) {
                val conversionResult = conversionResults[index]
                val amountStr = String.format("%.2f", conversionResult.amount)
                val resultStr = String.format("%.2f", conversionResult.result)
                view.t_recent_result.text = "$amountStr ${conversionResult.fromCurrency} is $resultStr ${conversionResult.toCurrency}"
                val toPerFromStr = String.format("%.2f", conversionResult.toPerFrom)
                view.t_recent_to_per_from.text = "(1${conversionResult.fromCurrency} = $toPerFromStr ${conversionResult.toCurrency})"

                view.setOnClickListener {
                    if (selectedConversionResultRootView == null) {
                        setConversionResultDetails(conversionResult)
                        TransitionManager.beginDelayedTransition(fl_fragment_container)
                        ll_details.visibility = View.VISIBLE
                        selectedConversionResultRootView = it
                        return@setOnClickListener
                    }

                    if (it == selectedConversionResultRootView) {
                        TransitionManager.beginDelayedTransition(fl_fragment_container)
                        if (ll_details.visibility == View.VISIBLE) {
                            ll_details.visibility = View.GONE
                        } else {
                            ll_details.visibility = View.VISIBLE
                        }
                    } else {
                        setConversionResultDetails(conversionResult)
                        if (ll_details.visibility == View.GONE) {
                            TransitionManager.beginDelayedTransition(fl_fragment_container)
                            ll_details.visibility = View.VISIBLE
                        }
                    }

                    selectedConversionResultRootView = it
                }
            } else {
                view.t_recent_result.text = ""
                view.t_recent_to_per_from.text = ""

                view.setOnClickListener(null)
            }
        }
    }

    private fun openExchangeRateConvertFragment() {
        val exchangeRateSearchFragment = ExchangeRateSearchFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, exchangeRateSearchFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onConversionCompleted(exchangeRateConversionResult: ExchangeRateConversionResult) {
        setConversionResultDetails(exchangeRateConversionResult)

        // update recents from cache only
        exchangeRateViewModel.getRecentConversionResultsInCache()
        selectedConversionResultRootView = recentConversionResultRootViews[0]
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExchangeRateFragment()
    }
}
