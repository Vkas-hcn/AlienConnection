package com.beetle.chili.triggers.connection.uskde

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.beetle.chili.triggers.connection.aleis.App
import java.util.Locale
import kotlin.system.exitProcess

object NetGet {
    private var getUrlCount = 0

    private fun getUrl(): Pair<String, String> {
        if (getUrlCount <= 3) {
            return Pair("https://api.infoip.io/", "country_short")
        } else if (getUrlCount <= 6) {
            return Pair("https://ipapi.co/json", "country_code")
        }
        getUrlCount = 0
        return Pair("https://api.infoip.io/", "country_short")
    }


    fun inspectConnect(activity: Activity): Boolean {
        if (inspectNetwork().not()) {
            AlertDialog.Builder(activity).create().apply {
                setCancelable(false)
                setOnKeyListener { dialog, keyCode, event -> true }
                setMessage("No network available. Please check your connection")
                setButton(AlertDialog.BUTTON_NEGATIVE, "OK") { d, _ -> dismiss() }
                show()
            }
            return true
        }
        val country = DataUtils.htp_country.ifEmpty { Locale.getDefault().country }
        if (arrayOf("CN", "HK", "MO", "IR").any { country.contains(it, true) }) {
            AlertDialog.Builder(activity).create().apply {
                setCancelable(false)
                setOnKeyListener { dialog, keyCode, event -> true }
                setMessage("This service is restricted in your region")
                setButton(AlertDialog.BUTTON_NEGATIVE, "Confirm") { d, _ -> dismiss() }
                setOnDismissListener {
                    activity.finish()
                    exitProcess(0)
                }
                show()
            }
            return true
        }
        return false
    }

    private fun inspectNetwork(): Boolean {
        (App.appComponent.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            getNetworkCapabilities(activeNetwork)?.apply {
                return (hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            }
        }
        return false
    }
}