package com.beetle.chili.triggers.connection.wjfos

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.ReferrerDetails
import com.beetle.chili.triggers.connection.BuildConfig
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.logAlien
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.NetGet
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import org.json.JSONObject
import java.util.Currency
import java.util.Locale
import java.util.UUID
import kotlin.jvm.internal.Intrinsics

object PutDataUtils {
    private fun firstJsonData(
        isAd: Boolean = false,
        adBean: TbaAdBean? = null
    ): JSONObject {
        return JSONObject().apply {
            if (isAd) {
                put("vti%taxi", adBean?.ttd_load_city ?: "null")
                put("vtn%taxi", adBean?.ttd_show_city ?: "null")
            }

            //log_id
            put("quantum", UUID.randomUUID().toString())
            //device_model
            put("peugeot", "1")
            //app_version
            put("meridian", getAppVersion())
            //os
            put("stalk", "moiseyev")
            //bundle_id
            put("eater", App.appComponent.packageName)
            //client_ts
            put("miami", System.currentTimeMillis())
            //gaid
            put("czar", DataUtils.advertising_google)
            //system_language
            put("ct", "${Locale.getDefault().language}_${Locale.getDefault().country}")
            //os_version
            put("range", "1")
            //android_id
            put("forborne", "1")
            put("cowhide", "111")
            //distinct_id
            put("gannet", DataUtils.BID)
            put("fulton", "111")
            //os_country
            put("maraud", Locale.getDefault().country)
        }
    }


    private fun getTbaDataJson(name: String): String {
        return firstJsonData().apply {
            put("hero", name)
        }.toString()
    }

    private fun getTbaTimeDataJson(
        name: String,
        parameterName: String,
        parameterValue: Any?,
        parameterName2: String?,
        parameterValue2: Any?,
        parameterName3: String?,
        parameterValue3: Any?,
        parameterName4: String?,
        parameterValue4: Any?,
    ): String {
        val data = JSONObject()
        data.put(parameterName, parameterValue)
        if (parameterName2 != null) {
            data.put(parameterName2, parameterValue2)
        }
        return firstJsonData().apply {
            put("hero", name)
            put("lame", JSONObject().apply {
                put(parameterName, parameterValue)
                if (parameterName2 != null) {
                    put(parameterName2, parameterValue2)
                }
                if (parameterName3 != null) {
                    put(parameterName3, parameterValue3)
                }
                if (parameterName4 != null) {
                    put(parameterName4, parameterValue4)
                }
            })
        }.toString()
    }

    private fun getSessionJson(): String {
        return firstJsonData().apply {
            put("coal", JSONObject())
        }.toString()
    }

    private fun install(context: Context, referrerDetails: ReferrerDetails): String {
        return firstJsonData().apply {
            //build
            put("fop", "build/${Build.ID}")

            //referrer_url
            put("grapple", referrerDetails.installReferrer)

            //install_version
            put("wreathe", referrerDetails.installVersion)

            //user_agent
            put("durango", getWebDefaultUserAgent(context))

            //lat
            put("sold", getLimitTracking(context))

            //referrer_click_timestamp_seconds
            put("seater", referrerDetails.referrerClickTimestampSeconds)

            //install_begin_timestamp_seconds
            put("kennel", referrerDetails.installBeginTimestampSeconds)

            //referrer_click_timestamp_server_seconds
            put("trustee", referrerDetails.referrerClickTimestampServerSeconds)

            //install_begin_timestamp_server_seconds
            put("algae", referrerDetails.installBeginTimestampServerSeconds)

            //install_first_seconds
            put("cardinal", getFirstInstallTime(context))

            //last_update_seconds
            put("somber", getLastUpdateTime(context))


            put("hero", "ocelot")

        }.toString()
    }

    private fun getAdJson(
        adValue: AdValue,
        responseInfo: ResponseInfo,
        adBean: TbaAdBean,
    ): String {

        return firstJsonData(true, adBean).apply {
            //ad_pre_ecpm
            put("feb", adValue.valueMicros)
            //currency
            put("ingather", adValue.currencyCode)
            //ad_network
            put(
                "goldman",
                responseInfo.mediationAdapterClassName
            )
            //ad_source
            put("evoke", "admob")
            //ad_code_id
            put("glycerin", adBean.adId)
            //ad_pos_id
            put("regard", adBean.adType)
            //ad_rit_id
            put("stucco", "")
            //ad_sense
            put("impede", "")
            //ad_format
            put("quod", adBean.adWhere)
            //precision_type
            put("casebook", getPrecisionType(adValue.precisionType))
            //ad_load_ip
            put("button", adBean.ttd_load_ip ?: "")
            //ad_impression_ip
            put("carlsbad", adBean.ttd_show_ip ?: "")

            put("hero", "kresge")

        }.toString()
    }

    private fun getAppVersion(): String {
        try {
            val packageInfo = App.appComponent.packageManager.getPackageInfo(
                App.appComponent.packageName,
                0
            )

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    private fun getLimitTracking(context: Context): String {
        return try {
            if (AdvertisingIdClient.getAdvertisingIdInfo(context).isLimitAdTrackingEnabled) {
                "vernier"
            } else {
                "sake"
            }
        } catch (e: Exception) {
            "sake"
        }
    }

    private fun getWebDefaultUserAgent(context: Context): String {
        return try {
            WebSettings.getDefaultUserAgent(context)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getFirstInstallTime(context: Context): Long {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.firstInstallTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getLastUpdateTime(context: Context): Long {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.lastUpdateTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getPrecisionType(precisionType: Int): String {
        return when (precisionType) {
            0 -> {
                "UNKNOWN"
            }

            1 -> {
                "ESTIMATED"
            }

            2 -> {
                "PUBLISHER_PROVIDED"
            }

            3 -> {
                "PRECISE"
            }

            else -> {
                "UNKNOWN"
            }
        }
    }


    fun emitInstallData(context: Context, referrerDetails: ReferrerDetails) {
        val json = install(context, referrerDetails)
        Log.e("TBA", "json-install--->${json}")
        try {
            NetGet.postPutData(
                json,
                object : NetGet.Callback {
                    override fun onSuccess(response: String) {
                        Log.e("TAG", "install事件上报-成功->")
                        DataUtils.installState = "1"
                    }

                    override fun onFailure(error: String) {
                        Log.e("TAG", "install事件上报-失败=$error")
                    }
                })
        } catch (e: Exception) {
            Log.e("TAG", "install事件上报-失败=$e")

        }
    }

    fun emitSessionData() {
        val json = getSessionJson()
        Log.e("TBA", "json-getSessionJson--->${json}")
        try {
            NetGet.postPutData(
                json,
                object : NetGet.Callback {
                    override fun onSuccess(response: String) {
                        Log.e("TAG", "Session事件上报-成功->")
                    }

                    override fun onFailure(error: String) {
                        Log.e("TAG", "Session事件上报-失败=$error")

                    }
                })
        } catch (e: Exception) {
            Log.e("TAG", "Session事件上报-失败=$e")

        }
    }

    fun emitAdData(
        adValue: AdValue,
        responseInfo: ResponseInfo,
        adBean: TbaAdBean,
    ) {
        val json = getAdJson(adValue, responseInfo, adBean)
        Log.e("TBA", "json-${adBean.adWhere}-getADJson--->${json}")
        try {
            NetGet.postPutData(
                json,
                object : NetGet.Callback {
                    override fun onSuccess(response: String) {
                        Log.e("TAG", "${adBean.adWhere}-广告事件上报-成功->${response}")
                    }

                    override fun onFailure(error: String) {
                        Log.e("TAG", "${adBean.adWhere}-广告事件上报-失败=$error")

                    }
                })
        } catch (e: Exception) {
            Log.e("TAG", "S${adBean.adWhere}-广告事件上报-失败=$e")
        }
    }

    fun postPointData(
        name: String,
        key: String? = null,
        keyValue: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
        key3: String? = null,
        keyValue3: Any? = null,
        key4: String? = null,
        keyValue4: Any? = null
    ) {
        Intrinsics.checkNotNullParameter(name, "name")
        val pointJson = if (key != null && keyValue != null) {
            getTbaTimeDataJson(
                name,
                key,
                keyValue,
                key2,
                keyValue2,
                key3,
                keyValue3,
                key4,
                keyValue4
            )
        } else {
            getTbaDataJson(name)
        }
        logAlien("${name}-打点--Json--->${pointJson}")
        try {
            NetGet.postPutData(pointJson, object : NetGet.Callback {
                override fun onSuccess(response: String) {
                    logAlien("${name}-打点事件上报-成功->${response}")
                }

                override fun onFailure(error: String) {
                    logAlien("${name}-打点事件上报-失败=$error")
                    logAlien("${name}-打点事件上报-失败=$error")
                }
            })
        } catch (e: java.lang.Exception) {
            logAlien("${name}-打点事件上报-失败=$e")
        }
    }

    fun beforeLoadLinkSettingsTTD(ufDetailBean: TbaAdBean): TbaAdBean {
        var data = false
        if (App.vvState) {
            ufDetailBean.ttd_load_ip = DataUtils.getNowVpn().host
            ufDetailBean.ttd_load_city = DataUtils.getNowVpn().city
        } else {
            data = true
        }
        if (data) {
            ufDetailBean.ttd_load_ip = DataUtils.llIp
            ufDetailBean.ttd_load_city = "none"
        }
        return ufDetailBean
    }


    fun afterLoadLinkSettingsTTD(ufDetailBean: TbaAdBean): TbaAdBean {
        var data = false
        if (App.vvState) {
            ufDetailBean.ttd_show_ip = DataUtils.getNowVpn().host
            ufDetailBean.ttd_show_city = DataUtils.getNowVpn().city
        } else {
            data = true
        }
        if (data) {
            logAlien("afterLoadLinkSettingsTTD=${DataUtils.llIp}")
            ufDetailBean.ttd_show_ip = DataUtils.llIp
            ufDetailBean.ttd_show_city = "none"
        }
        return ufDetailBean
    }

    fun toBuriedPointAdValueTTD(
        adValue: AdValue,
        responseInfo: ResponseInfo,
    ) {
        val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
        adRevenue.setRevenue(
            adValue.valueMicros / 1000000.0,
            adValue.currencyCode
        )
        adRevenue.setAdRevenueNetwork(responseInfo.mediationAdapterClassName)
        Adjust.trackAdRevenue(adRevenue)
        val data = GetMobData.getLjData().Bose2 ?: ""
        if (data.isNotBlank()) {
            AppEventsLogger.newLogger(App.appComponent).logPurchase(
                (adValue.valueMicros / 1000000.0).toBigDecimal(), Currency.getInstance("USD")
            )
        } else {
            Log.d("TBA", "purchase打点--value=${adValue.valueMicros}")
        }
    }

    fun abcAsk(adBean: TbaAdBean) {
        postPointData(
            "abc_ask",
            "inform",
            adBean.adType,
            "page",
            App.paPageName,
            "ID",
            "${adBean.adId}+${App.vvState}",
            "IP",
            adBean.ttd_load_ip
        )
        if (App.vvState) {
            postPointData(
                "abc_connect_ask",
                "inform",
                adBean.adType,
                "page",
                App.paPageName,
                "ID",
                "${adBean.adId}+${App.vvState}",
                "IP",
                adBean.ttd_load_ip
            )
        }
    }

    fun abcGett(adBean: TbaAdBean) {
        postPointData(
            "abc_gett",
            "inform",
            adBean.adType,
            "page",
            App.paPageName,
            "ID",
            "${adBean.adId}+${App.vvState}",
            "IP",
            adBean.ttd_load_ip
        )
    }

    fun abcAskdis(adBean: TbaAdBean, errorString: String) {
        postPointData(
            "abc_askdis",
            "inform",
            adBean.adType,
            "ID",
            "${adBean.adId}+${App.vvState}",
            "IP",
            adBean.ttd_load_ip,
            "reason",
            errorString
        )
    }

    fun abcView(adBean: TbaAdBean) {
        postPointData(
            "abc_view",
            "inform",
            adBean.adType,
            "page",
            App.paPageName,
            "ID",
            "${adBean.adId}+${App.vvState}",
            "pp",
            adBean.ttd_load_ip
        )
    }

}