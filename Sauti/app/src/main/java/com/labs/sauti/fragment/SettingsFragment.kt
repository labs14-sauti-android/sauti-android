package com.labs.sauti.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.view_model.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    private lateinit var settingsViewModel: SettingsViewModel

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
            when (it) {
                "English" -> {

                }
                "Swahili" -> {

                }
                "Luganda" -> {

                }
            }
        })

        settingsViewModel.getSelectedLanguage()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }
}
