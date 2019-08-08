package com.labs.sauti.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
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
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.view_model.ExchangeRateViewModel
import kotlinx.android.synthetic.main.fragment_exchange_rate.*
import kotlinx.android.synthetic.main.item_recent_exchange_rate.view.*
import javax.inject.Inject

class ExchangeRateFragment : Fragment(), ExchangeRateConvertFragment.OnConversionCompletedListener,
OnFragmentFullScreenStateChangedListener{

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var exchangeRateViewModelFactory: ExchangeRateViewModel.Factory

    private lateinit var exchangeRateViewModel: ExchangeRateViewModel

    private var shouldSelectMostRecentExchangeRateView = false
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

        setTranslatableTexts()

        ll_details.visibility = View.GONE

        exchangeRateViewModel.getRecentConversionResultsViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_exchange_rates_loading.displayedChild = 1
            } else {
                vs_recent_exchange_rates_loading.displayedChild = 0
                if (it.recentConversionResults == null) {
                    vs_recent_exchange_rates_empty.displayedChild = 1
                }
                it.recentConversionResults?.let { conversionResults ->
                    if (conversionResults.size == 0) {
                        vs_recent_exchange_rates_empty.displayedChild = 1
                    } else {
                        vs_recent_exchange_rates_empty.displayedChild = 0
                        handleRecentConversionResults(conversionResults)
                    }

                    shouldSelectMostRecentExchangeRateView = false
                }
            }
        })

        exchangeRateViewModel.getRecentConversionResults()

        b_convert.setOnClickListener {
            openExchangeRateConvertFragment()
        }
    }

    private fun setTranslatableTexts() {
        val ctx = LocaleHelper.createContext(context!!)

        val todaysIntlExchangeRatesSR = SpannableString(ctx.getString(R.string.today_s_intl_exchange_rates))
        todaysIntlExchangeRatesSR.setSpan(UnderlineSpan(), 0, todaysIntlExchangeRatesSR.length, 0)
        t_details_todays_intl_exchange_rates.text = todaysIntlExchangeRatesSR
        t_details_todays_intl_exchange_rates.typeface = Typeface.DEFAULT_BOLD
    }

    private fun setConversionResultDetails(exchangeRateConversionResult: ExchangeRateConversionResult) {
        val amountStr = String.format("%.2f", exchangeRateConversionResult.amount)
        val resultStr = String.format("%.2f", exchangeRateConversionResult.result)
        t_details_result.text = "$amountStr ${exchangeRateConversionResult.fromCurrency} is $resultStr ${exchangeRateConversionResult.toCurrency}"
        val toPerFromStr = String.format("%.2f", exchangeRateConversionResult.toPerFrom)
        t_details_to_per_from.text = "(1${exchangeRateConversionResult.fromCurrency} = $toPerFromStr ${exchangeRateConversionResult.toCurrency})"
    }

    private fun handleRecentConversionResults(conversionResults: MutableList<ExchangeRateConversionResult>) {
        ll_recent_exchange_rates.removeAllViews()

        conversionResults.forEachIndexed conversionResultsBreak@{ index, conversionResult ->
            if (index > MAX_RECENT_ITEMS) return@conversionResultsBreak

            val itemView = LayoutInflater.from(context!!).inflate(R.layout.item_recent_exchange_rate, ll_details, false)
            ll_recent_exchange_rates.addView(itemView)

            if (index == 0 && shouldSelectMostRecentExchangeRateView) selectedConversionResultRootView = itemView

            val localeCtx = LocaleHelper.createContext(context!!)
            itemView.t_recent_todays_intl_exchange_rates.text = localeCtx.getString(R.string.today_s_intl_exchange_rates)
            itemView.t_recent_todays_intl_exchange_rates.typeface = Typeface.DEFAULT_BOLD

            val amountStr = String.format("%.2f", conversionResult.amount)
            val resultStr = String.format("%.2f", conversionResult.result)
            itemView.t_recent_result.text = "$amountStr ${conversionResult.fromCurrency} is $resultStr ${conversionResult.toCurrency}"
            val toPerFromStr = String.format("%.2f", conversionResult.toPerFrom)
            itemView.t_recent_to_per_from.text = "(1${conversionResult.fromCurrency} = $toPerFromStr ${conversionResult.toCurrency})"

            itemView.setOnClickListener {
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
        }
    }

    private fun openExchangeRateConvertFragment() {
        val exchangeRateConvertFragment = ExchangeRateConvertFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, exchangeRateConvertFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onConversionCompleted(exchangeRateConversionResult: ExchangeRateConversionResult) {
        ll_details.visibility = View.VISIBLE
        setConversionResultDetails(exchangeRateConversionResult)

        // update recents from cache only
        exchangeRateViewModel.getRecentConversionResultsInCache()
        shouldSelectMostRecentExchangeRateView = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = context
        } else {
            throw RuntimeException("Context must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentFullScreenStateChangedListener = null
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    companion object {
        private const val MAX_RECENT_ITEMS = 2

        @JvmStatic
        fun newInstance() =
            ExchangeRateFragment()
    }
}
