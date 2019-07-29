package com.labs.sauti.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.labs.sauti.R

class SearchSpinnerCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(getContext(), R.layout.item_search_spinner, this)
    }

    //TODO: Add logic for visibility and adding spinner choices. 
}