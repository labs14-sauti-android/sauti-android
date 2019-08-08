package com.labs.sauti.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.labs.sauti.R
import com.labs.sauti.model.TradeInfoData
import kotlinx.android.synthetic.main.item_recent_card_view.view.*


class CardViewRecentSearches @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.AppTheme
) : CardView(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.item_recent_card_view, this)
    }

    fun consumeTIData(tradeInfoData: TradeInfoData) {
        t_card_view_header.text = tradeInfoData.tradeinfoTopic
        t_card_view_header.paintFlags = Paint.UNDERLINE_TEXT_FLAG


        //TODO: Hardcoded value for now. Change later.
        for (i in 0..3) {
            val textView = TextView(context, null, R.style.CardViewTradeInfoContentTextStyling)
            textView.text = tradeInfoData.tradeinfoList[i]
            ll_card_list.addView(textView)
        }

        //Determine maxlines for header
        //Determine maxlines for listed items
        //Handles the list format
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}

