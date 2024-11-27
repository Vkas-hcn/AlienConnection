package com.beetle.chili.triggers.connection.adkfieo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.beetle.chili.triggers.connection.BuildConfig
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.AlienBean
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.NetGet
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils.getAppVersion
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader

object GetMobData {
    private const val TAG = "AdReader"
    private const val FILE_NAME = "ad.json"
    private const val GZ_NAME = "gz.json"


    //打印日志封装
    fun logAlien(msg: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag(TAG).e(msg)
        }
    }

    fun base64Decode(base64Str: String): String {
        return String(Base64.decode(base64Str, Base64.DEFAULT))
    }

    fun getLjData(): AdLjBean {
        return runCatching {
            if (DataUtils.firebaseGz.isNotEmpty()) {
                Gson().fromJson(DataUtils.firebaseGz, AdLjBean::class.java)
            } else {
                Gson().fromJson(readAdJson(App.appComponent, GZ_NAME), AdLjBean::class.java)
            }
        }.onFailure { exception ->
        }.getOrNull() ?: Gson().fromJson(
            readAdJson(App.appComponent, GZ_NAME),
            AdLjBean::class.java
        )
    }

    fun getAdBlackData(): Boolean {
        val adBlack = when (getLjData().Bose1) {
            "1" -> {
                DataUtils.bbbbbbDD != "gummy"
            }

            "2" -> {
                false
            }

            else -> {
                DataUtils.bbbbbbDD != "gummy"
            }
        }
        if (!DataUtils.black_state && !adBlack) {
            PutDataUtils.postPointData("u_whitelist")
            DataUtils.black_state = true
        }
        return adBlack
    }

    fun getConnectTime(): Pair<Int, Int> {
        val default = 10
        val num = getLjData().Bose3 ?: ""
        val parts = num.split("&")
        val firstNumber = parts.getOrNull(0)?.toIntOrNull() ?: default
        val secondNumber = parts.getOrNull(1)?.toIntOrNull() ?: default
        return Pair(firstNumber, secondNumber)
    }

    private fun readAdJson(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val content = bufferedReader.use { it.readText() }
            content
        } catch (e: Exception) {
            logAlien("Error reading file: ${e.message}")
            null
        }
    }

    fun parseJsonToAdConfig(): AdConfig {
        // 获取服务器数据或本地数据
        val jsonData = DataUtils.firebaseAd.ifBlank { readAdJson(App.appComponent, FILE_NAME) }
        val jsonObject = JSONObject(jsonData)

        // 解析服务器数据
        val sbjiortb = jsonObject.getInt("sbjiortb")
        val crhpjkr = jsonObject.getInt("crhpjkr")
        val sreMap = jsonObject.getJSONObject("sre").toMap()
        val lclMap = jsonObject.getJSONObject("lcl").toMap()

        // 若服务器数据不完整，从本地数据中补充缺失的广告 ID
        val localJsonData = readAdJson(App.appComponent, FILE_NAME)
        val localJsonObject = JSONObject(localJsonData)

        // 使用本地数据补充服务器数据
        val finalSreMap = mergeMaps(sreMap, localJsonObject.getJSONObject("sre").toMap())
        val finalLclMap = mergeMaps(lclMap, localJsonObject.getJSONObject("lcl").toMap())
        return AdConfig(
            sbjiortb = sbjiortb,
            crhpjkr = crhpjkr,
            sre = finalSreMap,
            lcl = finalLclMap
        )
    }

    // 合并两个 Map，以优先使用 primaryMap 的值，secondaryMap 补充缺失的键值
    fun mergeMaps(
        primaryMap: Map<String, String>,
        secondaryMap: Map<String, String>
    ): Map<String, String> {
        return secondaryMap + primaryMap // primaryMap 的值优先覆盖
    }

    // 扩展 JSONObject 转为 Map<String, String>
    fun JSONObject.toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        this.keys().forEach { key ->
            map[key] = this.optString(key) // 使用 optString 避免 key 不存在引发异常
        }
        return map
    }

    @SuppressLint("HardwareIds")
    fun blackData(): Map<String, Any> {
        // TODO hmd package name
        return mapOf(
            "eater" to "com.secure.shield.wave.protect.fast",
            "stalk" to "moiseyev",
            "meridian" to getAppVersion(),
            "gannet" to DataUtils.BID,
        )
    }



    fun getBlackList(context: Context) {
        if (DataUtils.bbbbbbDD.isNotEmpty()) {
            return
        }
        NetGet.getMapData(
            "https://trinket.shieldproect.com/coop/belgian/sing",
            blackData(),
            object : NetGet.Callback {
                override fun onSuccess(response: String) {
                    logAlien("The blacklist request is successful：$response")
                    DataUtils.bbbbbbDD = response
                }

                override fun onFailure(error: String) {
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(10000)
                        logAlien("The blacklist request failed：$error")
                        getBlackList(context)
                    }
                }
            })
    }

    fun getOpenAdType(): AdType {
        return if (App.vvState) {
            AdType.OPEN
        } else {
            AdType.OPEN_DIS
        }
    }


    fun getHomeAdType(): AdType {
        return if (App.vvState) {
            AdType.NATIVE_HOME
        } else {
            AdType.NATIVE_HOME_DIS
        }
    }

    fun getResultAdType(): AdType {
        return if (App.vvState) {
            AdType.NATIVE_RESULT
        } else {
            AdType.NATIVE_RESULT_DIS
        }
    }

    fun getConnectAdType(): AdType {
        return AdType.INTERSTITIAL_CONNECT
    }

    fun getServiceAdType(): AdType {
        return if (App.vvState) {
            AdType.INTERSTITIAL_SERVICE
        } else {
            AdType.INTERSTITIAL_SERVICE_DIS
        }
    }

    fun getEndAdType(): AdType {
        return if (App.vvState) {
            AdType.INTERSTITIAL_RESULT
        } else {
            AdType.INTERSTITIAL_RESULT_DIS
        }
    }
}