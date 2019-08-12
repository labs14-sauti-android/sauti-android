package com.labs.sauti.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.view_model.HelpViewModel
import kotlinx.android.synthetic.main.fragment_help.*
import javax.inject.Inject

class HelpFragment : Fragment() {

    @Inject
    lateinit var helpViewModelFactory: HelpViewModel.Factory

    lateinit var helpViewModel: HelpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getHelpComponent().inject(this)
        helpViewModel = ViewModelProviders.of(this, helpViewModelFactory).get(HelpViewModel::class.java)
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

        helpViewModel.getIncorrectInformationViewState().observe(this, Observer {
            if (it.isLoading) {
                et_incorrect_information.isEnabled = false
                vs_submit_loading.displayedChild = 1
            } else {
                et_incorrect_information.isEnabled = true
                vs_submit_loading.displayedChild = 0
                if (it.isSentSuccessfully) {
                    Toast.makeText(context, "Your report has been sent successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to send report, please try again", Toast.LENGTH_LONG).show()
                }
            }
        })

        b_submit.setOnClickListener {
            helpViewModel.submitIncorrectInformation(et_incorrect_information.text.toString())
        }
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
