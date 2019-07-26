package com.labs.sauti.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.labs.sauti.model.TradeInfoData
import kotlinx.android.synthetic.main.item_recent_trade_info.view.*
import com.labs.sauti.R


class TradeInfoViewRecentSearches @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.AppTheme
) : CardView(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.item_recent_trade_info, this)
    }

    fun consumeTIData(tradeInfoData: TradeInfoData) {
        t_trade_info_header.text = tradeInfoData.tradeinfoTopic
        t_trade_info_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG


        //TODO: Hardcoded value for now. Change later.
        for (i in 0..4) {
            val textView = TextView(context, null, R.style.CardViewTradeInfoContentTextStyling)
            textView.text = tradeInfoData.tradeinfoList[i]
            ll_trade_list.addView(textView)
        }

        //Determine maxlines for header
        //Determine maxlines for listed items
        //Handles the list format
    }



}

