package com.labs.sauti.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.TextViewCompat
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.RequiredDocument
import com.labs.sauti.model.trade_info.TradeInfo
import com.labs.sauti.model.trade_info.TradeInfoTaxes
import kotlinx.android.synthetic.main.item_recent_card_view.view.*
import java.text.DecimalFormat


class CardViewRecentSearches @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.AppTheme
) : CardView(context, attrs, defStyleAttr) {

    private var focusObject : Any? = null
    val df by lazy { DecimalFormat("#,###.##") }

    init {
        inflate(getContext(), R.layout.item_recent_card_view, this)
    }



    fun consumeTITaxes(tradeInfoTaxes: TradeInfoTaxes) {

        if(ll_card_list.childCount > 0) {
            ll_card_list.removeAllViews()
        }

        if(t_taxcalc_total.visibility == View.VISIBLE){
            t_taxcalc_total.visibility = View.GONE
        }

        t_card_view_category.text = "Tax Calculator"
        t_card_view_category.setBackgroundResource(R.color.colorTaxCalculator)

        t_card_view_header.text =
            """Taxes for ${df.format(tradeInfoTaxes.initialAmount)} ${tradeInfoTaxes.currentCurrency} ${tradeInfoTaxes.taxProduct}"""

        tradeInfoTaxes.taxList.forEach{
            val textview = TextView(context)
            TextViewCompat.setTextAppearance(textview, R.style.CardViewTaxCalcListTextStyling)
            textview.text = it.taxTitle
            ll_card_list.addView(textview)
        }

        if(ll_card_list.childCount == 0) {
            val textview = TextView(context)
            TextViewCompat.setTextAppearance(textview, R.style.CardViewTaxCalcListTextStyling)
            textview.text = "There are no taxes for this product"
        } else {
            t_taxcalc_total.text = "Total: " + df.format(tradeInfoTaxes.totalAmount) + tradeInfoTaxes.endCurrency
            t_taxcalc_total.visibility = View.VISIBLE
        }

    }

    fun consumeTIBorderAgencies(tradeInfo: TradeInfo) {
        if(ll_card_list.childCount > 0) {
            ll_card_list.removeAllViews()
        }

        if(t_taxcalc_total.visibility == View.VISIBLE){
            t_taxcalc_total.visibility = View.GONE
        }

        t_card_view_header.text = tradeInfo.tradeinfoTopic
        t_card_view_category.text = "Trade Info"
        t_card_view_category.setBackgroundResource(R.color.colorTradeInfo)

        if(tradeInfo.tradeInfoAgencies!!.size > 0) {
            val agencyList = tradeInfo.tradeInfoAgencies as MutableList
            for(i in 0..2) {
                val textView = TextView(context, null, 0)
                textView.setTextAppearance(R.style.CardViewTradeInfoContentTextStyling)
                textView.text = (i + 1).toString() + ":  " + agencyList[i].agencyName
                ll_card_list.addView(textView)
            }
        }

    }

    fun consumeTIBorderProcedures(tradeInfo: TradeInfo) {
        if(ll_card_list.childCount > 0) {
            ll_card_list.removeAllViews()
        }

        if(t_taxcalc_total.visibility == View.VISIBLE){
            t_taxcalc_total.visibility = View.GONE
        }

        t_card_view_header.text = tradeInfo.tradeinfoTopic
        t_card_view_category.text = "Trade Info"
        t_card_view_category.setBackgroundResource(R.color.colorTradeInfo)

        if(tradeInfo.tradeInfoProcedure!!.size > 0) {

            val textView = TextView(context, null, 0)
            textView.setTextAppearance(R.style.CardViewTradeInfoContentTextStyling)
            textView.text = "1: " + tradeInfo.tradeInfoProcedure?.get(0)?.description
            ll_card_list.addView(textView)

        }




    }

    fun consumeTIRequiredDocuments(tradeInfo: TradeInfo) {
        if(ll_card_list.childCount > 0) {
            ll_card_list.removeAllViews()
        }

        if(t_taxcalc_total.visibility == View.VISIBLE){
            t_taxcalc_total.visibility = View.GONE
        }

        t_card_view_header.text = tradeInfo.tradeinfoTopic
        //TODO:  Change to translatable.
        t_card_view_category.text = "Trade Info"
        t_card_view_category.setBackgroundResource(R.color.colorTradeInfo)

        if(tradeInfo.tradeInfoDocs!!.size > 0) {
            val docList = tradeInfo.tradeInfoDocs as MutableList<RequiredDocument>
            for(i in 0..3) {
                val textView = TextView(context, null, 0)
                textView.setTextAppearance(R.style.CardViewTradeInfoContentTextStyling)
                textView.text = (i + 1).toString() + ":  " + docList[i].docTitle
                ll_card_list.addView(textView)
            }
        }

    }

    fun consumeTIRegulatedGood(tradeInfo: TradeInfo) {
        if(ll_card_list.childCount > 0) {
            ll_card_list.removeAllViews()
        }

        if(t_taxcalc_total.visibility == View.VISIBLE){
            t_taxcalc_total.visibility = View.GONE
        }

        t_card_view_header.text = tradeInfo.tradeinfoTopicExpanded
        //TODO:  Change to translatable.
        t_card_view_category.text = "Trade Info"
        t_card_view_category.setBackgroundResource(R.color.colorTradeInfo)

        //TODO: Hardcoded value for now. Change later.
        for (i in 0..3) {
            val textView = TextView(context, null, 0)
            textView.setTextAppearance(R.style.CardViewTradeInfoContentTextStyling)
            textView.text = """-${tradeInfo.tradeinfoList!![i]}"""
            ll_card_list.addView(textView)
        }

    }

/*    fun consumeTIData(tradeInfo: Any) {

        if(tradeInfo is TradeInfo) {
            t_card_view_header.text = tradeInfo.tradeinfoTopic

            //TODO: Hardcoded value for now. Change later.
            for (i in 0..2) {
                val textView = TextView(context, null, R.style.CardViewTradeInfoContentTextStyling)
                textView.text = tradeInfo.tradeinfoList[i]
                ll_card_list.addView(textView)
            }
            focusObject = tradeInfo
        }


        //Determine maxlines for header
        //Determine maxlines for listed items
        //Handles the list format
    }*/

    fun getFocusObject() = focusObject



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}

