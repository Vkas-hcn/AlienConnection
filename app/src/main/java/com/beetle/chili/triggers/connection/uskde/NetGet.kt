package com.beetle.chili.triggers.connection.uskde

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.beetle.chili.triggers.connection.aleis.App
import java.util.Locale
import kotlin.system.exitProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import android.webkit.WebSettings
import kotlinx.coroutines.delay
import org.json.JSONObject
object NetGet {
    private var getUrlCount = 0
    fun inspectCountry() {
        getUrlCount++
        val pair = getUrl()
        val url = URL(pair.first)

        CoroutineScope(Dispatchers.IO).launch {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", WebSettings.getDefaultUserAgent(App.appComponent))
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val responseText = reader.readText()
                    reader.close()

                    runCatching {
                        val json = JSONObject(responseText)
                        DataUtils.htp_country = json.getString(pair.second)
                    }
                } else {
                    // 非200响应代码时，延迟重试
                    retryInspectCountry()
                }
            } catch (e: Exception) {
                // 请求失败时，延迟重试
                retryInspectCountry()
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun retryInspectCountry() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            inspectCountry()
        }
    }

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



    interface Callback {
        fun onSuccess(response: String)
        fun onFailure(error: String)
    }

    fun get(context: Context, urlString: String, callback: Callback) {
        CoroutineScope(Dispatchers.IO).launch {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("XER", context.packageName)
                connection.setRequestProperty("PECSA", "ZZ")
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()
                    val processedResponse = processResponse(response)

                    withContext(Dispatchers.Main) {
                        callback.onSuccess(processedResponse)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback.onFailure("Error: $responseCode")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onFailure("Exception: ${e.message}")
                }
            } finally {
                connection?.disconnect()
            }
        }
    }
    // 处理响应字符串的方法
    fun processResponse(response: String): String {
        // 去掉头部 50 个字符
        val truncatedResponse = if (response.length > 50) response.substring(50) else ""
        // 大小写互换
        val swappedResponse = truncatedResponse.map {
            if (it.isUpperCase()) it.lowercaseChar() else it.uppercaseChar()
        }.joinToString("")
        // 使用 Base64 解码为字节数组并转换为字符串
        val decodedBytes = Base64.decode(swappedResponse, Base64.DEFAULT)
        return String(decodedBytes, Charsets.UTF_8)
    }
}