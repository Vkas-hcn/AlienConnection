package com.beetle.chili.triggers.connection.uskde

import android.content.Context
import android.content.Intent
import android.util.Log
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.AlienBean
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DataUtils {
    private val sharedPreferences by lazy {
        App.appComponent.getSharedPreferences(
            "sp_alien",
            Context.MODE_PRIVATE
        )
    }
    var selectTime = System.currentTimeMillis()
    var endTime = "00:00:00"
    val o_ad_key = "bs_ad"
    val o_gz_key = "Bo_se"


    var nowVpn: String
        get() = sharedPreferences.getString("nowVpn", "").toString()
        set(value) {
            sharedPreferences.edit().putString("nowVpn", value).apply()
        }
    var clickVpn: String
        get() = sharedPreferences.getString("clickVpn", "").toString()
        set(value) {
            sharedPreferences.edit().putString("clickVpn", value).apply()
        }

    var hisVpn: String
        get() = sharedPreferences.getString("hisVpn", "").toString()
        set(value) {
            sharedPreferences.edit().putString("hisVpn", value).apply()
        }
    var online_list_vpn: String
        get() = sharedPreferences.getString("online_list_vpn", "").toString()
        set(value) {
            sharedPreferences.edit().putString("online_list_vpn", value).apply()
        }
    var htp_country: String
        get() = sharedPreferences.getString("htp_country", "").toString()
        set(value) {
            sharedPreferences.edit().putString("htp_country", value).apply()
        }
    var firebaseAd: String
        get() = sharedPreferences.getString("firebaseAd", "").toString()
        set(value) {
            sharedPreferences.edit().putString("firebaseAd", value).apply()
        }

    var firebaseGz: String
        get() = sharedPreferences.getString("firebaseGz", "").toString()
        set(value) {
            sharedPreferences.edit().putString("firebaseGz", value).apply()
        }

    var ouMState: String
        get() = sharedPreferences.getString("ouMState", "").toString()
        set(value) {
            sharedPreferences.edit().putString("ouMState", value).apply()
        }

    var bbbbbbDD: String
        get() = sharedPreferences.getString("bbbbbbDD", "").toString()
        set(value) {
            sharedPreferences.edit().putString("bbbbbbDD", value).apply()
        }

    var BID: String
        get() = sharedPreferences.getString("BID", "").toString()
        set(value) {
            sharedPreferences.edit().putString("BID", value).apply()
        }
    fun getCountryIcon(name: String): Int {
        return when (name.replace(" ", "").lowercase()) {
            "australia" -> R.drawable.australia
            "belgium" -> R.drawable.belgium
            "brazil" -> R.drawable.brazil
            "canada" -> R.drawable.canada
            "unitedkingdom" -> R.drawable.unitedkingdom
            "france" -> R.drawable.france
            "germany" -> R.drawable.germany
            "hongkong" -> R.drawable.hongkong
            "india" -> R.drawable.india
            "ireland" -> R.drawable.ireland
            "italy" -> R.drawable.italy
            "japan" -> R.drawable.japan
            "korea" -> R.drawable.koreasouth
            "netherlands" -> R.drawable.netherlands
            "newzealand" -> R.drawable.newzealand
            "norway" -> R.drawable.norway
            "russianfederation" -> R.drawable.russianfederation
            "singapore" -> R.drawable.singapore
            "sweden" -> R.drawable.sweden
            "switzerland" -> R.drawable.switzerland
            "unitedarabemirates" -> R.drawable.unitedarabemirates
            "unitedstates" -> R.drawable.unitedstates
            else -> R.drawable.icon_s_logo
        }
    }

    private var cachedHisBean: MutableList<VInForBean>? = null

    fun getHisListVpn(): MutableList<VInForBean>? {
        if (cachedHisBean == null) {
            cachedHisBean = try {
                Gson().fromJson(hisVpn, object : TypeToken<MutableList<VInForBean>>() {}.type)
            } catch (e: JsonSyntaxException) {
                null
            } catch (e: JsonParseException) {
                null
            }
        }
        cachedHisBean = cachedHisBean?.sortedByDescending { it.vpnDate }?.toMutableList()
        return cachedHisBean
    }

    fun getConnectionStats(): Pair<String, String?> {
        var totalConnectionSeconds = 0L
        val serverCount = mutableMapOf<String, Int>()

        // 定义连接时间的格式
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        // 获取当前日期的时间戳
        val currentDateTimestamp = System.currentTimeMillis()
        val currentDate = Date(currentDateTimestamp)
        val currentDateFormat = SimpleDateFormat("MM/dd yyyy", Locale.getDefault())
        val currentDateStr = currentDateFormat.format(currentDate)
        // 过滤出今天的数据
        val todayData = cachedHisBean?.filter {
            val vpnDate = Date(it.vpnDate.toLong())
            val vpnDateFormat = SimpleDateFormat("MM/dd yyyy", Locale.getDefault())
            vpnDateFormat.format(vpnDate) == currentDateStr
        }?.sortedByDescending { it.vpnDate }?.toMutableList()


        todayData?.forEach { item ->
            // 计算总连接时长，以秒为单位
            try {
                val duration = timeFormat.parse(item.connectTime)
                val hours = duration.hours.toLong()
                val minutes = duration.minutes.toLong()
                val seconds = duration.seconds.toLong()
                totalConnectionSeconds += hours * 3600 + minutes * 60 + seconds
            } catch (e: Exception) {
                Log.e("VInForBean", "Failed to parse connectTime: ${e.message}")
            }

            // 统计每个服务器的连接次数
            val serverName = item.getName()
            serverCount[serverName] = serverCount.getOrDefault(serverName, 0) + 1
        }

        // 找到连接次数最多的服务器
        val mostConnectedServer = serverCount.maxByOrNull { it.value }?.key

        // 格式化总连接时长为 "HH:mm:ss"
        val hours = totalConnectionSeconds / 3600
        val minutes = (totalConnectionSeconds % 3600) / 60
        val seconds = totalConnectionSeconds % 60
        val formattedTotalTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        return Pair(formattedTotalTime, mostConnectedServer)
    }

    fun addHisVpn() {
        var hisBean = getHisListVpn()
        if (hisBean == null) {
            hisBean = mutableListOf()
        }
        val vpn = getNowVpn()
        vpn.vpnDate = System.currentTimeMillis().toString()
        nowVpn = Gson().toJson(vpn)
        hisBean.add(vpn)
        hisVpn = Gson().toJson(hisBean)
        cachedHisBean = hisBean
    }

    fun editHisVpn(vpnDate: String) {
        val hisBean = getHisListVpn()
        hisBean?.firstOrNull { it.vpnDate == vpnDate }?.connectTime = endTime
        hisVpn = Gson().toJson(hisBean)
        cachedHisBean = hisBean
    }

    fun getNowVpn(): VInForBean {
        return try {
            val vpn = Gson().fromJson(nowVpn, VInForBean::class.java)
            if (vpn.host.isBlank()) {
                nowVpn = ""
            }
            Gson().fromJson(nowVpn, VInForBean::class.java)
        } catch (e: Exception) {
            nowVpn = Gson().toJson(getSmartList())
            Gson().fromJson(nowVpn, VInForBean::class.java)
        }
    }

    fun getVpnIsConnect(): Boolean {
        return App.vvState
    }

    fun getClickVpn(): VInForBean? {
        return try {
            val vpn = Gson().fromJson(clickVpn, VInForBean::class.java)
            vpn
        } catch (e: Exception) {
            null
        }
    }

    private fun getOnlineListVpn(): AlienBean? {
        try {
            val vpn = Gson().fromJson(online_list_vpn, AlienBean::class.java)
            if (vpn.data == null) {
                return null
            }
            if (vpn.data.smart_list.isEmpty()) {
                return null
            }
            if (vpn.data.server_list.isEmpty()) {
                return null
            }
            return vpn as AlienBean
        } catch (e: Exception) {
            return null
        }
    }

    fun getSmartList(): VInForBean {
        val dataBean = getOnlineListVpn() ?: return VInForBean(isSmart = true)
        val random = (0 until dataBean.data.smart_list.size).random()
        val smart = dataBean.data.smart_list[random]
        smart.isSmart = true
        return smart
    }

    fun getListVpn(): MutableList<VInForBean>? {
        val data = getOnlineListVpn()?.data?.server_list
        data?.forEach {
            it.isSmart = false
        }
        return data
    }

    suspend fun haveVpnData(
        loadingStartFun: () -> Unit,
        loadingEndFun: () -> Unit,
        nextFun: () -> Unit
    ) {
        if (getOnlineListVpn() == null) {
            loadingStartFun()
            getOnlineVpnData(App.appComponent)
            delay(2000)
            loadingEndFun()
        } else {
            nextFun()
        }
    }

    fun Context.shareText(text: String, subject: String = "") {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun extractCountryName(input: String): String? {
        val parts = input.split("-")
        return if (parts.size > 1) {
            parts[0]
        } else {
            null
        }
    }

    fun addTimes(vararg times: String): String {
        var totalSeconds = 0L

        for (time in times) {
            val parts = time.split(":")
            if (parts.size == 3) {
                val hours = parts[0].toLongOrNull() ?: 0L
                val minutes = parts[1].toLongOrNull() ?: 0L
                val seconds = parts[2].toLongOrNull() ?: 0L

                totalSeconds += hours * 3600 + minutes * 60 + seconds
            }
        }

        val hours = TimeUnit.SECONDS.toHours(totalSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    fun getOnlineVpnData(context: Context) {
        NetGet.get(context, ZongData.vpnOnline, object : NetGet.Callback {
            override fun onSuccess(response: String) {
                Log.e("TAG", "getOnlineVpnData-onSuccess: $response")
                online_list_vpn = response
            }

            override fun onFailure(error: String) {
                Log.e("TAG", "getOnlineVpnData-Failure: $error")

            }
        })

    }

}