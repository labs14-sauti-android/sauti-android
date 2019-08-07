package com.labs.sauti.fragment

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.labs.sauti.R
import com.labs.sauti.helper.LocaleHelper
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTranslatableTexts()
    }

    private fun setTranslatableTexts() {
        val ctx = LocaleHelper.createContext(context!!)

        t_report_incorrect_info.text = ctx.getString(R.string.report_incorrect_info)
        t_report_incorrect_info.typeface = Typeface.DEFAULT_BOLD
        t_did_you_notice_incorrect_information_on_the_sauti_app.text = ctx.getString(R.string.did_you_notice_incorrect_information_on_the_sauti_app)
        et_incorrect_information.hint = ctx.getString(R.string.enter_incorrect_information)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HelpFragment()
    }
}
