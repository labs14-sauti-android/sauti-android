package com.labs.sauti.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.labs.sauti.R
import com.labs.sauti.model.TaxCalculationData
import kotlinx.android.synthetic.main.fragment_tax_calculator.*

class TaxCalculatorFragment : Fragment(), TaxCalculatorSearchFragment.OnTaxCalculatorSearchCompletedListener,
OnFragmentFullScreenStateChangedListener {

    private var onFragmentFullScreenStateChangedListener: OnFragmentFullScreenStateChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onTaxCalculatorSearchCompleted(taxCalculationData: TaxCalculationData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
