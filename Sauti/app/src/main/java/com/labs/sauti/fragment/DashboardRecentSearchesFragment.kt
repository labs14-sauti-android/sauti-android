package com.labs.sauti.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.adapter.DashboardFavoritesAdapter
import com.labs.sauti.view_model.DashboardRecentSearchesViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_recent_searches.*
import javax.inject.Inject

class DashboardRecentSearchesFragment : Fragment() {

    private var onRecentSearchClickListener: OnRecentSearchClickListener? = null

    @Inject
    lateinit var dashboardRecentSearchesViewModelFactory: DashboardRecentSearchesViewModel.Factory

    private lateinit var dashboardRecentSearchesViewModel: DashboardRecentSearchesViewModel

    private lateinit var dashboardFavoritesAdapter: DashboardFavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context!!.applicationContext as SautiApp).dashboardComponent.inject(this)
        dashboardRecentSearchesViewModel = ViewModelProviders
            .of(this, dashboardRecentSearchesViewModelFactory)
            .get(DashboardRecentSearchesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_recent_searches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardFavoritesAdapter = DashboardFavoritesAdapter(mutableListOf()) {
            onRecentSearchClickListener?.onRecentSearchClick(it)
        }

        r_recent_searches.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        r_recent_searches.adapter = dashboardFavoritesAdapter

        dashboardRecentSearchesViewModel.getRecentSearchesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_recent_searches_loading.displayedChild = 1
            } else {
                vs_recent_searches_loading.displayedChild = 0
                it.recentSearches?.let {recentSearches ->
                    dashboardFavoritesAdapter.setFavorites(recentSearches)
                }
            }
        })
        dashboardRecentSearchesViewModel.getRecentSearches()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRecentSearchClickListener) {
            onRecentSearchClickListener = context
        } else {
            throw RuntimeException("Context must implement OnRecentSearchClickListener")
        }
    }



    override fun onDetach() {
        super.onDetach()

        onRecentSearchClickListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DashboardRecentSearchesFragment()
    }

    interface OnRecentSearchClickListener {
        fun onRecentSearchClick(recentSearch: Any)
    }

}
