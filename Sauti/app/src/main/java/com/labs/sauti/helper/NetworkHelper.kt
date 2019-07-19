package com.labs.sauti.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService

class NetworkHelper(private val context: Context) {

    companion object {
        const val TYPE_WIFI = "WIFI"
        const val TYPE_MOBILE = "MOBILE"
    }

    fun hasNetworkConnection(): Boolean {
        val cm = getSystemService(context, ConnectivityManager::class.java) ?: return false
        val networkInfo = cm.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }

    fun getNetworkConnectionTypes(): List<String> {
        val outTypes = mutableListOf<String>()
        val cm = getSystemService(context, ConnectivityManager::class.java) ?: return outTypes
        val allNetworks = cm.allNetworks ?: return outTypes
        for (network in allNetworks) {

            if (cm.getNetworkCapabilities(network).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                outTypes.add(TYPE_WIFI)
            } else if (cm.getNetworkCapabilities(network).hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                outTypes.add(TYPE_MOBILE)
            }
        }
        return outTypes
    }
}