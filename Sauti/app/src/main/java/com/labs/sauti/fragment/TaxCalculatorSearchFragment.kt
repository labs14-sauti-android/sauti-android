package com.labs.sauti.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics

import com.labs.sauti.R
import com.labs.sauti.model.TaxCalculationData
import com.labs.sauti.view_model.TaxCalculatorViewModel
import kotlinx.android.synthetic.main.fragment_tax_calculator_search.*
import javax.inject.Inject

// TODO fullscreen
class TaxCalculatorSearchFragment : Fragment() {

    private var onTaxCalculatorSearchCompletedListener: OnTaxCalculatorSearchCompletedListener? = null
    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var taxCalculatorViewModelFactory: TaxCalculatorViewModel.Factory

    private lateinit var taxCalculatorViewModel: TaxCalculatorViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tax_calculator_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        vs_products.visibility = View.GONE
//        vs_where_to_list.visibility = View.GONE
//        vs_where_from_list.visibility = View.GONE
//        ll_value.visibility = View.GONE
//        b_search.isEnabled = false
//
//        vs_categories.displayedChild = 1
//
//        b_search.setOnClickListener {
//            if (s_categories.selectedItem != null &&
//                s_products.selectedItem != null &&
//                s_where_to_list.selectedItem != null &&
//                s_where_from_list.selectedItem != null) {
//
//            }
//        }
    }

    fun categorySelected() {
        vs_products.visibility = View.GONE
        vs_where_to_list.visibility = View.GONE
        vs_where_from_list.visibility = View.GONE
        ll_value.visibility = View.GONE
        b_search.isEnabled = false

        if (s_categories.selectedItem is String && s_categories.selectedItem != "") {
            val category = s_categories.selectedItem as String

            vs_products.visibility = View.VISIBLE
            vs_products.displayedChild = 1
        }
    }

    fun productSelected() {
        vs_where_to_list.visibility = View.GONE
        vs_where_from_list.visibility = View.GONE
        ll_value.visibility = View.GONE
        b_search.isEnabled = false

        if (s_products.selectedItem is String && s_products.selectedItem != "") {
            val product = s_products.selectedItem as String

            vs_where_to_list.visibility = View.VISIBLE
            vs_where_to_list.displayedChild = 1
        }
    }

    fun whereToSelected() {
        vs_where_from_list.visibility = View.GONE
        ll_value.visibility = View.GONE
        b_search.isEnabled = false

        if (s_where_to_list.selectedItem is String && s_where_to_list.selectedItem != "") {
            val whereTo = s_where_to_list.selectedItem as String

            vs_where_from_list.visibility = View.VISIBLE
            vs_where_from_list.displayedChild = 1
        }
    }

    fun whereFromSelected() {
        b_search.isEnabled = false

        if (s_where_from_list.selectedItem is String && s_where_from_list.selectedItem != "") {
            b_search.isEnabled = true
            ll_value.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (parentFragment is OnTaxCalculatorSearchCompletedListener) {
            onTaxCalculatorSearchCompletedListener = parentFragment as OnTaxCalculatorSearchCompletedListener
        } else {
            throw RuntimeException("parentFragment must implement OnTaxCalculatorSearchCompletedListener")
        }

        if (parentFragment is OnFragmentFullScreenStateChangedListener) {
            onFragmentFullScreenStateChangedListener = parentFragment as OnFragmentFullScreenStateChangedListener
            onFragmentFullScreenStateChangedListener!!.onFragmetFullScreenStateChanged(true)
        } else {
            throw RuntimeException("parentFragment must implement OnFragmentFullScreenStateChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onTaxCalculatorSearchCompletedListener = null
        onFragmentFullScreenStateChangedListener?.onFragmetFullScreenStateChanged(false)
        onFragmentFullScreenStateChangedListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TaxCalculatorSearchFragment()
    }

    interface OnTaxCalculatorSearchCompletedListener {
        fun onTaxCalculatorSearchCompleted(taxCalculationData: TaxCalculationData)
    }

}
