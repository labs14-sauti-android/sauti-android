package com.labs.sauti.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.recyclerview.widget.GridLayoutManager

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.adapter.RecentExchangeRateConversionResultsAdapter
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionResult
import com.labs.sauti.view_model.ExchangeRateViewModel
import kotlinx.android.synthetic.main.fragment_exchange_rate.*
import kotlinx.android.synthetic.main.item_recent_exchange_rate.view.*
import java.text.DecimalFormat
import javax.inject.Inject

class ExchangeRateFragment : Fragment(), ExchangeRateConvertFragment.OnConversionCompletedListener,
OnFragmentFullScreenStateChangedListener{

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var exchangeRateViewModelFactory: ExchangeRateViewModel.Factory

    private lateinit var exchangeRateViewModel: ExchangeRateViewModel

    private lateinit var recentExchangeRateConversionResultsAdapter: RecentExchangeRateConversionResultsAdapter

    private var shouldSelectMostRecentExchangeRateView = false
    private var selectedConversionResultPosition = -1

    private val networkChangedReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkHelper.hasNetworkConnection(context!!)) {
                t_warning_no_network_connection.visibility = View.GONE

                onNetworkConnected()
            } else {
                t_warning_no_network_connection.visibility = View.VISIBLE
            }
        }
    }

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

        context!!.registerReceiver(networkChangedReceiver, IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        })

        setTranslatableTexts()

        recentExchangeRateConversionResultsAdapter = RecentExchangeRateConversionResultsAdapter(mutableListOf(),
            object: RecentExchangeRateConversionResultsAdapter.OnRecentExchangeRateConversionResultClickedListener {
                override fun onRecentExchangeRateConversionResultClicked(
                    position: Int,
                    recentExchangeRateConversionResult: ExchangeRateConversionResult
                ) {
                    onConversionResultClicked(position, recentExchangeRateConversionResult)
                }
            })

        // recycler view setup
        r_recent_exchange_rates.layoutManager = GridLayoutManager(context, 2)
        r_recent_exchange_rates.adapter = recentExchangeRateConversionResultsAdapter

        // recent conversion results
        exchangeRateViewModel.getRecentConversionResultsViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_exchange_rates_loading.displayedChild = 1
            } else {
                vs_recent_exchange_rates_loading.displayedChild = 0
                if (it.recentConversionResults == null) {
                    vs_recent_exchange_rates_empty.displayedChild = 1
                }
                it.recentConversionResults?.let { conversionResults ->
                    if (conversionResults.isEmpty()) {
                        vs_recent_exchange_rates_empty.displayedChild = 1
                    } else {
                        vs_recent_exchange_rates_empty.displayedChild = 0
                        handleRecentConversionResults(conversionResults)
                        if (shouldSelectMostRecentExchangeRateView) {
                            selectedConversionResultPosition = 0
                        }
                    }

                    shouldSelectMostRecentExchangeRateView = false
                }
            }
        })
        exchangeRateViewModel.getRecentConversionResults()

        // signed in user
        exchangeRateViewModel.getSignedInUserViewState().observe(this, Observer {
            if (!it.isLoading) {
                if (it.user?.userId != null) {
                    ll_favorite.visibility = View.VISIBLE
                } else{
                    ll_favorite.visibility = View.GONE
                }
            }
        })
        exchangeRateViewModel.getSignedInUser(NetworkHelper.hasNetworkConnection(context!!))

        // favorite
        exchangeRateViewModel.getIsFavoriteExchangeRateConversionViewState().observe(this, Observer {
            if (it.isLoading) {
                ll_favorite.isEnabled = false
            } else {
                ll_favorite.isEnabled = true
                if (it.isFavorite) {
                    i_favorite.setImageResource(R.drawable.ic_star_filled)
                } else {
                    i_favorite.setImageResource(R.drawable.ic_star_empty)
                }
            }
        })

        b_convert.setOnClickListener {
            openExchangeRateConvertFragment()
        }
    }

    private fun onNetworkConnected() {
        exchangeRateViewModel.syncFavoriteConversions()
    }

    private fun setTranslatableTexts() {
        val ctx = LocaleHelper.createContext(context!!)

        val todaysIntlExchangeRatesSR = SpannableString(ctx.getString(R.string.today_s_intl_exchange_rates))
        todaysIntlExchangeRatesSR.setSpan(UnderlineSpan(), 0, todaysIntlExchangeRatesSR.length, 0)
        t_details_todays_intl_exchange_rates.text = todaysIntlExchangeRatesSR
        t_details_todays_intl_exchange_rates.typeface = Typeface.DEFAULT_BOLD
    }

    private fun setConversionResultDetails(exchangeRateConversionResult: ExchangeRateConversionResult) {
        val decimalFormat = DecimalFormat("#,##0.00")
        val amountStr = decimalFormat.format(exchangeRateConversionResult.amount)
        val resultStr = decimalFormat.format(exchangeRateConversionResult.result)
        t_details_result.text = "$amountStr ${exchangeRateConversionResult.fromCurrency} is $resultStr ${exchangeRateConversionResult.toCurrency}"
        val toPerFromStr = decimalFormat.format(exchangeRateConversionResult.toPerFrom)
        t_details_to_per_from.text = "(1${exchangeRateConversionResult.fromCurrency} = $toPerFromStr ${exchangeRateConversionResult.toCurrency})"

        ll_favorite.setOnClickListener {
            exchangeRateViewModel.toggleFavorite(
                NetworkHelper.hasNetworkConnection(context!!),
                exchangeRateConversionResult.fromCurrency,
                exchangeRateConversionResult.toCurrency,
                exchangeRateConversionResult.amount
            )
        }

        if (ll_favorite.visibility == View.VISIBLE) {
            exchangeRateViewModel.isFavoriteConversion(
                NetworkHelper.hasNetworkConnection(context!!),
                exchangeRateConversionResult.fromCurrency,
                exchangeRateConversionResult.toCurrency,
                exchangeRateConversionResult.amount
            )
        }
    }

    private fun handleRecentConversionResults(conversionResults: List<ExchangeRateConversionResult>) {
        recentExchangeRateConversionResultsAdapter.setRecentExchangeRateConversionResults(conversionResults)
    }

    private fun onConversionResultClicked(position: Int, conversionResult: ExchangeRateConversionResult) {
        if (selectedConversionResultPosition == -1) {
            setConversionResultDetails(conversionResult)
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            cl_details.visibility = View.VISIBLE
            selectedConversionResultPosition = position
            return
        }

        if (position == selectedConversionResultPosition) {
            TransitionManager.beginDelayedTransition(fl_fragment_container)
            if (cl_details.visibility == View.VISIBLE) {
                cl_details.visibility = View.GONE
            } else {
                cl_details.visibility = View.VISIBLE
            }
        } else {
            setConversionResultDetails(conversionResult)
            if (cl_details.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(fl_fragment_container)
                cl_details.visibility = View.VISIBLE
            }
        }

        selectedConversionResultPosition = position
    }

    private fun openExchangeRateConvertFragment() {
        val exchangeRateConvertFragment = ExchangeRateConvertFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.fl_fragment_container, exchangeRateConvertFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onConversionCompleted(exchangeRateConversionResult: ExchangeRateConversionResult) {
        cl_details.visibility = View.VISIBLE
        setConversionResultDetails(exchangeRateConversionResult)

        // update recents from cache only
        exchangeRateViewModel.getRecentConversionResultsInCache()
        selectedConversionResultPosition = -1
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

    override fun onDestroy() {
        super.onDestroy()

        context!!.unregisterReceiver(networkChangedReceiver)
    }

    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExchangeRateFragment()
    }
}
