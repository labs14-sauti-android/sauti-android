package com.labs.sauti.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.labs.sauti.R
import com.labs.sauti.helper.LocaleHelper
import kotlinx.android.synthetic.main.fragment_report.*

class ReportFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTranslatableTexts()
    }

    private fun setTranslatableTexts() {
        val ctx = LocaleHelper.createContext(context!!)

        t_we_will_now_ask_some_questions_about_your_border_experience.text = ctx.getString(R.string.we_will_now_ask_some_questions_about_your_border_experience)
        t_we_will_now_ask_some_questions_about_your_border_experience.typeface = Typeface.DEFAULT_BOLD
        t_which_border_did_you_cross.text = ctx.getString(R.string.which_border_did_you_cross)
        t_did_you_have_a_good_experience.text = ctx.getString(R.string.did_you_have_a_good_experience)
        b_yes.text = ctx.getString(R.string.yes)
        b_yes.typeface = Typeface.DEFAULT_BOLD
        b_no.text = ctx.getString(R.string.no)
        b_no.typeface = Typeface.DEFAULT_BOLD
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportFragment().apply {
            }
    }
}
