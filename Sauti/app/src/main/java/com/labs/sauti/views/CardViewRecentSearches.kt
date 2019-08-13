package com.labs.sauti.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.labs.sauti.R
import com.labs.sauti.model.trade_info.TradeInfo
import kotlinx.android.synthetic.main.item_recent_card_view.view.*


class CardViewRecentSearches @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.AppTheme
) : CardView(context, attrs, defStyleAttr) {

    //TODO: Remove just for presentation
    private var focusObject : Any? = null

    init {
        inflate(getContext(), R.layout.item_recent_card_view, this)
    }

    fun consumeTIData(tradeInfo: Any) {

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
    }

    fun getFocusObject() = focusObject



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}

