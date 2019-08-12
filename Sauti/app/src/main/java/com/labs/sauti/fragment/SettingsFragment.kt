package com.labs.sauti.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.view_model.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private var onLanguageChangedListener: OnLanguageChangedListener? = null

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    private lateinit var settingsViewModel: SettingsViewModel

    private var shouldRestartActivity = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).getSettingsComponent().inject(this)
        settingsViewModel = ViewModelProviders.of(this, settingsViewModelFactory).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.getSelectedLanguageLiveData().observe(this, Observer {
            unSelectLanguages()
            val locale = Locale(it)
            Locale.setDefault(locale)
            val config = context!!.resources.configuration
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            // TODO refresh main fragments

            when (it) {
                "en" -> {
                    fl_language_english.background.setTint(ContextCompat.getColor(context!!, R.color.colorSelectedLanguage))
                    t_language_english.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
                }
                "sw" -> {
                    fl_language_swahili.background.setTint(ContextCompat.getColor(context!!, R.color.colorSelectedLanguage))
                    t_language_swahili.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
                }
                "lg" -> {
                    fl_language_luganda.background.setTint(ContextCompat.getColor(context!!, R.color.colorSelectedLanguage))
                    t_language_luganda.setTextColor(ContextCompat.getColor(context!!, R.color.colorText))
                }
            }

            if (shouldRestartActivity) onLanguageChangedListener?.onLanguageChanged()
        })

        settingsViewModel.getSelectedLanguage()

        fl_language_english.setOnClickListener {
            shouldRestartActivity = true
            settingsViewModel.setSelectedLanguage("en")
        }

        fl_language_swahili.setOnClickListener {
            shouldRestartActivity = true
            settingsViewModel.setSelectedLanguage("sw")
        }

        fl_language_luganda.setOnClickListener {
            shouldRestartActivity = true
            settingsViewModel.setSelectedLanguage("lu")
        }
    }

    private fun unSelectLanguages() {
        ll_languages.children.iterator().forEach {
            it.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
            ((it as ViewGroup).children.iterator().next() as TextView).setTextColor(ContextCompat.getColor(context!!, R.color.colorTextDim))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnLanguageChangedListener) {
            onLanguageChangedListener = context
        } else {
            throw RuntimeException("Context must implement OnLanguageChangedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onLanguageChangedListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }

    interface OnLanguageChangedListener {
        fun onLanguageChanged()
    }
}
