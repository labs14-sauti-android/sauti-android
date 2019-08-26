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
import com.google.android.material.snackbar.Snackbar

import com.labs.sauti.R
import com.labs.sauti.SautiApp
import com.labs.sauti.adapter.DashboardFavoritesAdapter
import com.labs.sauti.helper.NetworkHelper
import com.labs.sauti.view_model.DashboardFavoritesViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_favorites.*
import javax.inject.Inject

class DashboardFavoritesFragment : Fragment() {

    private var onFavoriteClickListener: OnFavoriteClickListener? = null

    @Inject
    lateinit var dashboardFavoritesViewModelFactory: DashboardFavoritesViewModel.Factory

    private lateinit var dashboardFavoritesViewModel: DashboardFavoritesViewModel

    private lateinit var dashboardFavoritesAdapter: DashboardFavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context!!.applicationContext as SautiApp).dashboardComponent.inject(this)
        dashboardFavoritesViewModel = ViewModelProviders.of(this, dashboardFavoritesViewModelFactory)
            .get(DashboardFavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardFavoritesAdapter = DashboardFavoritesAdapter(mutableListOf()) {
            onFavoriteClickListener?.onFavoriteClick(it)
        }
        r_favorites.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        r_favorites.adapter = dashboardFavoritesAdapter

        dashboardFavoritesViewModel.getErrorLiveData().observe(this, Observer {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        })

        dashboardFavoritesViewModel.getSignedInUserViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_sign_up_favorites.displayedChild = 0
                vs_sign_up_loading.displayedChild = 1
            } else {
                vs_sign_up_favorites.displayedChild = 0
                vs_sign_up_loading.displayedChild = 0
                if (it.user?.userId != null) {
                    dashboardFavoritesViewModel.getFavorites(NetworkHelper.hasNetworkConnection(context!!))
                }
            }
        })

        dashboardFavoritesViewModel.getFavoritesViewState().observe(this, Observer {
            if (it.isLoading) {
                vs_sign_up_favorites.displayedChild = 1
                vs_favorites_loading.displayedChild = 1
            } else {
                vs_sign_up_favorites.displayedChild = 1
                vs_favorites_loading.displayedChild = 0
                it.favorites?.let {favorites->
                    dashboardFavoritesAdapter.setFavorites(favorites)
                }
            }
        })

        dashboardFavoritesViewModel.getSignedInUser(NetworkHelper.hasNetworkConnection(context!!))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnFavoriteClickListener) {
            onFavoriteClickListener = context
        } else {
            throw RuntimeException("Context must implement OnFavoriteClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()

        onFavoriteClickListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DashboardFavoritesFragment()
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(favorite: Any)
    }
}
