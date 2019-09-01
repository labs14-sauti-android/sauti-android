package com.labs.sauti.fragment

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProviders
import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.model.TaxCalculationData
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import com.labs.sauti.view_model.TradeInfoViewModel
import kotlinx.android.synthetic.main.fragment_tax_calculator.*
import java.text.DecimalFormat
import javax.inject.Inject

class TaxCalculatorFragment : Fragment(), TaxCalculatorSearchFragment.OnTaxCalculatorSearchCompletedListener,
OnFragmentFullScreenStateChangedListener {

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    @Inject
    lateinit var tradeInfoViewModelFactory: TradeInfoViewModel.Factory

    private lateinit var tradeInfoViewModel: TradeInfoViewModel

    val df by lazy { DecimalFormat("#,###.##") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            (it.applicationContext as SautiApp).getTaxCalculatorComponent().inject(this)
            tradeInfoViewModel= ViewModelProviders.of(this, tradeInfoViewModelFactory).get(TradeInfoViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tax_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ll_details.visibility = View.GONE

        fab_tax_calculator_search.setOnClickListener {
            val taxCalculatorSearchFragment = TaxCalculatorSearchFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container_tax_calculator, taxCalculatorSearchFragment)
                .addToBackStack(null)
                .commit()
        }
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

    override fun onTaxCalculatorSearchCompleted(tradeInfoTaxes: TradeInfoTaxes) {
        t_tax_calculator_no_recent.visibility = View.GONE

        l_tax_calculator_list.removeAllViews()

        tradeInfoTaxes.getTaxesConversions()

        cl_expanded_tax_calculator.visibility = View.VISIBLE
        t_tax_calculator_header.text =
            """Taxes for ${df.format(tradeInfoTaxes.initialAmount)} ${tradeInfoTaxes.currentCurrency} of ${tradeInfoTaxes.taxProduct}"""
        t_tax_calculator_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        tradeInfoTaxes.taxList.forEach{
            val textview = TextView(context)
            TextViewCompat.setTextAppearance(textview, R.style.TaxCalculatorDetailsListStyling)
            textview.text = it.taxTitle
            l_tax_calculator_list.addView(textview)
        }

        if(l_tax_calculator_list.childCount == 0) {
            val textview = TextView(context)
            TextViewCompat.setTextAppearance(textview, R.style.TaxCalculatorDetailsListStyling)
            textview.text = "There are no taxes for this product"
            l_tax_calculator_list.addView(textview)

            t_tax_calculator_total.visibility = View.INVISIBLE
        } else {
            t_tax_calculator_total.setTextAppearance(R.style.TradeInfoDetailsSubHeaderStyling)
            t_tax_calculator_total.text = "Total: " + df.format(tradeInfoTaxes.totalAmount) + tradeInfoTaxes.endCurrency
            t_tax_calculator_total.visibility = View.VISIBLE
        }

        tiv_tax_calculator_recent_first.consumeTITaxes(tradeInfoTaxes)

        tiv_tax_calculator_recent_first.visibility = View.VISIBLE
        tiv_tax_calculator_recent_second.visibility = View.INVISIBLE

    }

    fun showeTaxCalculationDetails(taxes: TradeInfo) {

    }


    override fun onFragmetFullScreenStateChanged(isFullScreen: Boolean) {
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(isFullScreen)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TaxCalculatorFragment()

    }
}
