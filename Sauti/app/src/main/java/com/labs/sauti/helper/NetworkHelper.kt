package com.labs.sauti.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager



object NetworkHelper {
    const val CLASS_2G = "2G"
    const val CLASS_3G = "3G"
    const val CLASS_4G = "4g"
    const val CLASS_UNKNOWN = "unknown"

    fun hasNetworkConnection(context: Context): Boolean {
        val cm = getSystemService(context, ConnectivityManager::class.java) ?: return false
        val networkInfo = cm.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }

    fun getNetworkClass(context: Context): String {
        val mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (mTelephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> return CLASS_2G
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> return CLASS_3G
            TelephonyManager.NETWORK_TYPE_LTE -> return CLASS_4G
            else -> return CLASS_UNKNOWN
        }
    }

    /**
     * @param transport eg NetworkCapabilities.TRANSPORT_WIFI
     * */
    fun hasTransport(context: Context, transport: Int): Boolean {
        val cm = getSystemService(context, ConnectivityManager::class.java) ?: return false
        val allNetworks = cm.allNetworks ?: return false
        for (network in allNetworks) {
            if (cm.getNetworkCapabilities(network).hasTransport(transport)) return true
        }
        return false
    }
}