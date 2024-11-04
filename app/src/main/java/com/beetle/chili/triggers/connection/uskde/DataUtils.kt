package com.beetle.chili.triggers.connection.uskde

import android.content.Context
import android.util.Log
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.AlienBean
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.uskde.DataUtils.getHisListVpn
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.TimeZone

object DataUtils {
    private val sharedPreferences by lazy {
        App.appComponent.getSharedPreferences(
            "sp_alien",
            Context.MODE_PRIVATE
        )
    }
    var selectTime = System.currentTimeMillis()
    var endTime = "00:00:00"
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

    fun getCountryIcon(name: String): Int {
        return when (name.replace(" ", "").lowercase()) {
            "australia" -> R.drawable.australia
            "belgium" -> R.drawable.belgium
            "brazil" -> R.drawable.brazil
            "canada" -> R.drawable.canada
            "eng" -> R.drawable.unitedkingdom
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
            try {
                cachedHisBean =
                    Gson().fromJson(hisVpn, object : TypeToken<MutableList<VInForBean>>() {}.type)
            } catch (e: JsonSyntaxException) {
                // 记录日志
                Log.e("VInForBean", "Failed to parse JSON: ${e.message}")
                cachedHisBean = null
            } catch (e: JsonParseException) {
                // 记录日志
                Log.e("VInForBean", "Failed to parse JSON: ${e.message}")
                cachedHisBean = null
            }
        }
        return cachedHisBean
    }

    fun addHisVpn() {
        var hisBean = getHisListVpn()
        if (hisBean == null) {
            hisBean = mutableListOf()
        }
        val vpn = getNowVpn()
        vpn.connectTime = System.currentTimeMillis().toString()
        hisBean.add(vpn)
        hisVpn = Gson().toJson(hisBean)
        cachedHisBean = hisBean
    }

    fun editHisVpn(connectTime: String) {
        val hisBean = getHisListVpn()
        hisBean?.firstOrNull { it.vpnDate == connectTime }?.connectTime = endTime
        hisVpn = Gson().toJson(hisBean)
        cachedHisBean = hisBean // 更新缓存
    }

    fun getNowVpn(): VInForBean {
        return try {
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

    fun getOnlineListVpn(): AlienBean {
        return try {
            val vpn = Gson().fromJson(online_list_vpn, AlienBean::class.java)
            vpn as AlienBean
        } catch (e: Exception) {
            Gson().fromJson(ZongData.vpnList, AlienBean::class.java)
        }
    }

    fun getSmartList(): VInForBean {
        val dataBean = getOnlineListVpn()
        val random = (0 until dataBean.data.smart_list.size).random()
        val smart = dataBean.data.smart_list[random]
        smart.isSmart = true
        return smart
    }

    fun getListVpn(): MutableList<VInForBean> {
        val data = getOnlineListVpn().data.server_list
        data.forEach {
            it.isSmart = false
        }
        return data
    }
}