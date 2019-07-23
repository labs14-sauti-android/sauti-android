package com.labs.sauti.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.labs.sauti.R

class MarketplaceFragment : BaseFragment() {
    override fun getFragmentType(): Type = Type.MARKETPLACE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marketplace, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MarketplaceFragment()
    }
}
