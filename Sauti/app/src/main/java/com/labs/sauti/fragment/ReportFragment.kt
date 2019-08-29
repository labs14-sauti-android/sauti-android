package com.labs.sauti.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.helper.LocaleHelper
import com.labs.sauti.model.report.ReportForm
import com.labs.sauti.view_model.ReportViewModel
import kotlinx.android.synthetic.main.fragment_report.*
import javax.inject.Inject

class ReportFragment : Fragment() {

    @Inject
    lateinit var reportViewModelFactory: ReportViewModel.Factory

    private lateinit var reportViewModel: ReportViewModel

    private var hasGoodExperience: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getReportComponent().inject(this)
        reportViewModel = ViewModelProviders.of(this, reportViewModelFactory).get(ReportViewModel::class.java)
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

        b_yes.setOnClickListener {
            hasGoodExperience = true
            b_yes.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonHasGoodExperienceSelected))
            b_no.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonHasGoodExperienceNotSelected))
        }

        b_no.setOnClickListener {
            hasGoodExperience = false
            b_no.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonHasGoodExperienceSelected))
            b_yes.background.setTint(ContextCompat.getColor(context!!, R.color.colorButtonHasGoodExperienceNotSelected))
        }

        reportViewModel.getErrorLiveData().observe(this, Observer {
            Toast.makeText(context!!, "Failed to load borderNames. ${it.message}", Toast.LENGTH_LONG).show()
        })

        reportViewModel.getBorderNamesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_borders_loading.displayedChild = 1
            } else {
                vs_borders_loading.displayedChild = 0
                it.borderNames?.let { borders ->
                    handleBorders(borders)
                }
            }
        })

        reportViewModel.getBorders()

        reportViewModel.getSubmitReportFormViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_submit_loading.displayedChild = 1
            } else {
                vs_submit_loading.displayedChild = 0
                if (it.isSentSuccessfully) {
                    Toast.makeText(context, "Your report has been sent successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to send report, please try again", Toast.LENGTH_LONG).show()
                }
            }
        })

        b_submit.setOnClickListener {
            if (s_borders.selectedItem is String && hasGoodExperience != null) {
                reportViewModel.submitReportForm(
                    ReportForm(
                        s_borders.selectedItem as String,
                        hasGoodExperience!!
                    )
                )
            }
        }
    }

    private fun handleBorders(borders: List<String>) {
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item)
        adapter.add("")
        adapter.addAll(borders)

        s_borders.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportFragment().apply {
            }
    }
}
