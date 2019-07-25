package com.labs.sauti.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.labs.sauti.R
import com.labs.sauti.model.TradeInfoData
import kotlinx.android.synthetic.main.item_recent_trade_info.view.*

class TradeInfoViewRecentSearches @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : CardView(context, attrs, defStyleAttr) {


    fun initView(tradeInfoData: TradeInfoData) {
        t_trade_info_header.text = tradeInfoData.tradeinfoTopic

        //TODO: Hardcoded value for now. Change later.
        for (i in 0..5) {
            val textView = TextView(context, null, R.style.CardViewTradeInfoContentTextStyling)
            textView.text = tradeInfoData.tradeinfoList[i]
            ll_trade_list.addView(textView)
        }


        //Determine maxlines for header
        //Determine maxlines for listed items
        //Handles the list format

    }
}

