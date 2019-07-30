package com.labs.sauti.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.labs.sauti.R
import kotlinx.android.synthetic.main.item_search_spinner.view.*

class SearchSpinnerCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(getContext(), R.layout.item_search_spinner, this)
    }

    //TODO: Add logic for visibility and adding spinner choices.

    fun addSearchHeader(header: String) {
        t_search_param.text = header
    }

    fun addSpinnerContents(list : List<String>) {
        ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            s_search_param.adapter = adapter
        }
    }

    fun progressBarSVisibility() {
        if(p_search_param.isVisible) {
            p_search_param.visibility = View.GONE
        }
    }

}