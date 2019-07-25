package com.labs.sauti.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.view_model.MarketPriceViewModel
import kotlinx.android.synthetic.main.fragment_market_price_search.*
import javax.inject.Inject

// TODO button color when disabled. or just hide the button?
// TODO use ViewSwitcher instead of VISIBLE/GONE when loading
class MarketPriceSearchFragment : Fragment() {
    private var onMarketPriceSearchCompletedListener: OnMarketPriceSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var marketPricesViewModelFactory: MarketPriceViewModel.Factory

    private lateinit var marketPricesViewModel: MarketPriceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getMarketPriceComponent().inject(this)
        marketPricesViewModel = ViewModelProviders.of(this, marketPricesViewModelFactory).get(MarketPriceViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market_price_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_countries.visibility = View.GONE
        ll_markets.visibility = View.GONE
        ll_categories.visibility = View.GONE
        ll_commodities.visibility = View.GONE
        b_search.isEnabled = false

        // countries
        marketPricesViewModel.getCountriesLiveData().observe(this, Observer {
            pb_countries.visibility = View.GONE
            ll_countries.visibility = View.VISIBLE

            val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
            it.add(0, "")
            adapter.addAll(it)

            s_countries.adapter = adapter
            s_countries.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    countrySelected()
                }
            }
        })
        pb_countries.visibility = View.VISIBLE
        marketPricesViewModel.getCountries()

        // markets
        marketPricesViewModel.getMarketsLiveData().observe(this, Observer {
            pb_markets.visibility = View.GONE
            ll_markets.visibility = View.VISIBLE

            val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
            it.add(0, "")
            adapter.addAll(it)

            s_markets.adapter = adapter
            s_markets.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    marketSelected()
                }
            }
        })

        // categories
        marketPricesViewModel.getCategoriesLiveData().observe(this, Observer {
            pb_categories.visibility = View.GONE
            ll_categories.visibility = View.VISIBLE

            val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
            it.add(0, "")
            adapter.addAll(it)

            s_categories.adapter = adapter
            s_categories.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    categorySelected()
                }
            }
        })

        // commodities
        marketPricesViewModel.getProductsLiveData().observe(this, Observer {
            pb_commodities.visibility = View.GONE
            ll_commodities.visibility = View.VISIBLE

            val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
            it.add(0, "")
            adapter.addAll(it)

            s_commodities.adapter = adapter
            s_commodities.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    commoditySelected()
                }
            }
        })

        marketPricesViewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        })

        marketPricesViewModel.getSearchMarketPriceLiveData().observe(this, Observer {
            vs_search_loading.displayedChild = 0

            onMarketPriceSearchCompletedListener?.onMarketPriceSearchCompleted(it)

            onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
            fragmentManager!!.popBackStack()
        })

        b_search.setOnClickListener {
            if (s_countries.selectedItem is String &&
                s_markets.selectedItem is String &&
                s_categories.selectedItem is String &&
                s_commodities.selectedItem is String) {
                vs_search_loading.displayedChild = 1

                marketPricesViewModel.searchMarketPrice(
                    s_countries.selectedItem as String,
                    s_markets.selectedItem as String,
                    s_categories.selectedItem as String,
                    s_commodities.selectedItem as String
                )
            }
        }
    }

    private fun countrySelected() {
        ll_markets.visibility = View.GONE
        ll_categories.visibility = View.GONE
        ll_commodities.visibility = View.GONE
        b_search.isEnabled = false

        if (s_countries.selectedItem is String && s_countries.selectedItem != "") {
            val country = s_countries.selectedItem as String

            pb_markets.visibility = View.VISIBLE
            marketPricesViewModel.getMarkets(country)
        }
    }

    private fun marketSelected() {
        ll_categories.visibility = View.GONE
        ll_commodities.visibility = View.GONE
        b_search.isEnabled = false

        if (s_markets.selectedItem is String && s_markets.selectedItem != "") {
            val country = s_countries.selectedItem as String
            val market = s_markets.selectedItem as String

            pb_categories.visibility = View.VISIBLE
            marketPricesViewModel.getCategories(country, market)
        }
    }

    private fun categorySelected() {
        ll_commodities.visibility = View.GONE
        b_search.isEnabled = false

        if (s_categories.selectedItem is String && s_categories.selectedItem != "") {
            val country = s_countries.selectedItem as String
            val market = s_markets.selectedItem as String
            val category = s_categories.selectedItem as String

            pb_commodities.visibility = View.VISIBLE
            marketPricesViewModel.getProducts(country, market, category)
        }
    }

    private fun commoditySelected() {
        b_search.isEnabled = false

        if (s_commodities.selectedItem is String && s_commodities.selectedItem != "") {
            b_search.isEnabled = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnMarketPriceSearchCompletedListener) {
            onMarketPriceSearchCompletedListener = parentFragment as OnMarketPriceSearchCompletedListener
        } else {
            throw RuntimeException("parentFragment must implement OnSearchCompletedListener")
        }

        if (parentFragment is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
        } else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onMarketPriceSearchCompletedListener = null

        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
        onFragmentFullScreenStateChangedListener = null
    }

    interface OnMarketPriceSearchCompletedListener {
        fun onMarketPriceSearchCompleted(marketPrice: MarketPriceData)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MarketPriceSearchFragment()
    }
}
