package com.labs.sauti.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.labs.sauti.R
import com.labs.sauti.model.TradeInfoData
import com.labs.sauti.views.TradeInfoViewRecentSearches
import kotlinx.android.synthetic.main.fragment_trade_info.*

//TODO Troy mentioned adding a lightweight animation.
//TODO: Add a clicklistener to the items in required documents in class Lance wants that clickable

class TradeInfoFragment : Fragment() {

    //TODO: Remove Dummy Data
    lateinit var testTIbanned: TradeInfoData
    lateinit var testTIdocuments: TradeInfoData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: Remove dummy data creation
        testTIbanned = TradeInfoData(0,
            "These commodities are banned:",
            "These commodities are banned and cannot legally cross the border:",
            listOf("-Air Zinc",
                "-Batteries",
                "-Batteries",
                "-Khanga, kikoi, kitenge",
                "-Lithium",
                "-Maize",
                "-Manganese Dioxide",
                "-Matches",
                "-Mercuric Oxide",
                "-Rice",
                "-Silver Oxide",
                "-Sugar",
                "-Tobacco",
                "-Used clothing",
                "-Wheat")
        )

        //TODO: Remove dummy data creation
        testTIdocuments = TradeInfoData(1,
            "Required Documents:",
            "Required Documents...expanded",
            listOf("1. Import ",
                "2. Valid Invoice",
                "3:Simplified Certificate Of Origin (SCOO)",
                "4:National ID",
                "5:Yellow Fever Card"))


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO Hide the constraint layout unless clicked. Handled via clicklistener.
        cl_expanded_tradeinfo.visibility = View.GONE
        tiv_recent_first.setOnClickListener{
            cl_expanded_tradeinfo.visibility = View.VISIBLE
        }


        tiv_recent_first.initView(testTIbanned)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TradeInfoFragment()
    }

}
