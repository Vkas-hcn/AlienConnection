package com.beetle.chili.triggers.connection.adkfieo

import android.app.Activity
import android.content.Context
import android.graphics.Outline
import android.os.SystemClock
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.logAlien
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.uasndje.EndActivity
import com.beetle.chili.triggers.connection.uasndje.MainActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.uskde.DataUtils

object AdManager : LifecycleObserver {
    private val CACHE_DURATION = TimeUnit.HOURS.toMillis(1)
    private val CACHE_DURATION_TEST = TimeUnit.MINUTES.toMillis(1)
    // 各种广告类型的缓存实例
    private var appOpenAd: AppOpenAd? = null
    private var nativeHomeAd: NativeAd? = null
    private var nativeResultAd: NativeAd? = null
    private var interstitialConnectAd: InterstitialAd? = null
    private var interstitialServiceAd: InterstitialAd? = null
    private var interstitialResultAd: InterstitialAd? = null

    private var appOpenAdDis: AppOpenAd? = null
    private var nativeHomeAdDis: NativeAd? = null
    private var nativeResultAdDis: NativeAd? = null
    private var interstitialServiceAdDis: InterstitialAd? = null
    private var interstitialResultAdDis: InterstitialAd? = null

    // 记录各广告类型的上次加载时间
    private val lastLoadTime = mutableMapOf<AdType, Long>()
    private val nowLoadState = mutableMapOf<AdType, Boolean>()

    private lateinit var adConfig: AdConfig
    var openTypeIp = ""
    var homeTypeIp = ""
    var resultTypeIp = ""
    var contTypeIp = ""
    var listTypeIp = ""
    var endTypeIp = ""
    fun init(context: Context) {
        MobileAds.initialize(context)
    }

    private fun getAdUnitId(source: AdSource, key: String): String? {
        return when (source) {
            AdSource.SRE -> adConfig.sre[key]
            AdSource.LCL -> adConfig.lcl[key]
        }
    }

    // 判断缓存是否过期
    private fun isCacheExpired(adType: AdType): Boolean {
        val lastTime = lastLoadTime[adType] ?: 0
        return SystemClock.elapsedRealtime() - lastTime > CACHE_DURATION
    }

    // 根据广告类型获取对应的广告实例
    private fun getAd(adType: AdType): Any? {
        return when (adType) {
            AdType.OPEN -> appOpenAd
            AdType.NATIVE_HOME -> nativeHomeAd
            AdType.NATIVE_RESULT -> nativeResultAd
            AdType.INTERSTITIAL_CONNECT -> interstitialConnectAd
            AdType.INTERSTITIAL_SERVICE -> interstitialServiceAd
            AdType.INTERSTITIAL_RESULT -> interstitialResultAd

            AdType.OPEN_DIS -> appOpenAdDis
            AdType.NATIVE_HOME_DIS -> nativeHomeAdDis
            AdType.NATIVE_RESULT_DIS -> nativeResultAdDis
            AdType.INTERSTITIAL_SERVICE_DIS -> interstitialServiceAdDis
            AdType.INTERSTITIAL_RESULT_DIS -> interstitialResultAdDis
        }
    }

    fun setAd(adType: AdType, ad: Any) {
        when (adType) {
            AdType.OPEN -> appOpenAd = ad as AppOpenAd
            AdType.NATIVE_HOME -> nativeHomeAd = ad as NativeAd
            AdType.NATIVE_RESULT -> nativeResultAd = ad as NativeAd
            AdType.INTERSTITIAL_CONNECT -> interstitialConnectAd = ad as InterstitialAd
            AdType.INTERSTITIAL_SERVICE -> interstitialServiceAd = ad as InterstitialAd
            AdType.INTERSTITIAL_RESULT -> interstitialResultAd = ad as InterstitialAd

            AdType.OPEN_DIS -> appOpenAdDis = ad as AppOpenAd
            AdType.NATIVE_HOME_DIS -> nativeHomeAdDis = ad as NativeAd
            AdType.NATIVE_RESULT_DIS -> nativeResultAdDis = ad as NativeAd
            AdType.INTERSTITIAL_SERVICE_DIS -> interstitialServiceAdDis = ad as InterstitialAd
            AdType.INTERSTITIAL_RESULT_DIS -> interstitialResultAdDis = ad as InterstitialAd
        }
    }

    private fun getCurrentAdSource(): AdSource {
        return if (App.vvState) AdSource.SRE else AdSource.LCL
    }

    fun loadAd(context: Context, adType: AdType) {

        if (nowLoadState[adType] == true) {
            logAlien("$adType 广告加载中")
            return
        }
        val blackData = GetMobData.getAdBlackData()
        if (blackData && adType != AdType.OPEN && adType != AdType.OPEN_DIS && adType != AdType.NATIVE_RESULT && adType != AdType.NATIVE_RESULT_DIS) {
            logAlien("黑名单屏蔽$adType 广告，不在加载 ")
            return
        }
        if (App.vvState && (getLoadIp(adType).isNotEmpty()) && getLoadIp(adType) != DataUtils.getNowVpn().host) {
            logAlien("${adType}-ip不一致-重新加载-load_ip=" + getLoadIp(adType) + "-now-ip=" + DataUtils.getNowVpn().host)
            clearAd(adType)
            clearLoadIp(adType)
            loadAd(context, adType)
            return
        }
        if (getAd(adType) != null && isCacheExpired(adType)) {
            logAlien("=$adType 广告过期，重新加载 ")
            clearAd(adType)
            loadAd(context, adType)
            return
        }
        if (getAd(adType) != null && !isCacheExpired(adType)) {
            logAlien("=$adType 广告已存在，不在加载 ")
            return
        }
        adConfig = GetMobData.parseJsonToAdConfig()
        val adSource = getCurrentAdSource()
        val adUnitId = when (adType) {
            AdType.OPEN -> getAdUnitId(adSource, "op")
            AdType.NATIVE_HOME -> getAdUnitId(adSource, "homenv")
            AdType.NATIVE_RESULT -> getAdUnitId(adSource, "resultnv")
            AdType.INTERSTITIAL_CONNECT -> getAdUnitId(adSource, "cnctiv")
            AdType.INTERSTITIAL_SERVICE -> getAdUnitId(adSource, "servivback")
            AdType.INTERSTITIAL_RESULT -> getAdUnitId(adSource, "resivback")

            AdType.OPEN_DIS -> getAdUnitId(adSource, "op")
            AdType.NATIVE_HOME_DIS -> getAdUnitId(adSource, "homenv")
            AdType.NATIVE_RESULT_DIS -> getAdUnitId(adSource, "resultnv")
            AdType.INTERSTITIAL_SERVICE_DIS -> getAdUnitId(adSource, "servivback")
            AdType.INTERSTITIAL_RESULT_DIS -> getAdUnitId(adSource, "resivback")
        }
        nowLoadState[adType] = true
        logAlien("${adType}-广告，开始加载-id=${adUnitId}")
        when (adType) {
            AdType.OPEN -> {
                openTypeIp = DataUtils.getNowVpn().host
                loadAppOpenAd(context, adUnitId!!, adType)
            }

            AdType.NATIVE_HOME -> {
                homeTypeIp = DataUtils.getNowVpn().host
                loadNativeAd(context, adUnitId!!, adType)
            }

            AdType.NATIVE_RESULT -> {
                resultTypeIp = DataUtils.getNowVpn().host
                loadNativeAd(context, adUnitId!!, adType)
            }

            AdType.INTERSTITIAL_CONNECT -> {
                contTypeIp = DataUtils.getNowVpn().host
                loadInterstitialAd(context, adUnitId!!, adType)
            }

            AdType.INTERSTITIAL_SERVICE -> {
                listTypeIp = DataUtils.getNowVpn().host
                loadInterstitialAd(context, adUnitId!!, adType)
            }

            AdType.INTERSTITIAL_RESULT -> {
                endTypeIp = DataUtils.getNowVpn().host
                loadInterstitialAd(context, adUnitId!!, adType)
            }

            AdType.OPEN_DIS -> loadAppOpenAd(context, adUnitId!!, adType)
            AdType.NATIVE_HOME_DIS -> loadNativeAd(context, adUnitId!!, adType)
            AdType.NATIVE_RESULT_DIS -> loadNativeAd(context, adUnitId!!, adType)
            AdType.INTERSTITIAL_SERVICE_DIS -> loadInterstitialAd(context, adUnitId!!, adType)
            AdType.INTERSTITIAL_RESULT_DIS -> loadInterstitialAd(context, adUnitId!!, adType)
        }
    }


    private fun loadAppOpenAd(context: Context, adUnitId: String, adType: AdType) {

        AppOpenAd.load(
            context, adUnitId, AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    setAd(adType, ad)
                    lastLoadTime[adType] = SystemClock.elapsedRealtime()
                    logAlien("${adType}-广告，加载成功")
                    nowLoadState[adType] = false
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // 处理加载失败
                    logAlien("${adType}-广告，加载失败=${adError.message}")
                    nowLoadState[adType] = false
                }
            }
        )
    }

    private fun loadNativeAd(context: Context, adUnitId: String, adType: AdType) {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                logAlien("${adType}-广告，加载成功")
                setAd(adType, ad)
                nowLoadState[adType] = false
                lastLoadTime[adType] = SystemClock.elapsedRealtime()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logAlien("${adType}-广告，加载失败=${adError.message}")
                    nowLoadState[adType] = false
                }
            })
            .build()
            .loadAd(AdRequest.Builder().build())
    }

    private fun loadInterstitialAd(context: Context, adUnitId: String, adType: AdType) {
        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    setAd(adType, ad)
                    lastLoadTime[adType] = SystemClock.elapsedRealtime()
                    logAlien("${adType}-广告，加载成功")
                    nowLoadState[adType] = false
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // 处理加载失败
                    logAlien("${adType}-广告，加载失败=${adError.message}")
                    nowLoadState[adType] = false


                }
            })
    }

    fun canShowAd(activity: Activity, adType: AdType): Int {
        val ad = getAd(adType)
        if (ad == null || !isAppInForeground(activity)) return 1
        return 2
    }

    // 展示广告
    fun showAd(activity: Activity, adType: AdType, onAdClosed: () -> Unit) {
        val ad = getAd(adType)
        if (App.vvState && (getLoadIp(adType).isNotEmpty()) && getLoadIp(adType) != DataUtils.getNowVpn().host) {
            logAlien("${adType}-ip不一致-不能展示-load_ip=" + getLoadIp(adType) + "-now-ip=" + DataUtils.getNowVpn().host)
            clearAd(adType)
            clearLoadIp(adType)
            loadAd(activity, adType)
            return
        }
        if (!isAppInForeground(activity)) {
            onAdClosed()
            return
        }
        when (adType) {
            AdType.OPEN, AdType.OPEN_DIS -> {
                (ad as? AppOpenAd)?.apply {
                    fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdClosed()
                            clearAd(adType)
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            logAlien("${adType}-广告，展示")

                        }
                    }
                    show(activity)
                }
            }

            AdType.NATIVE_HOME, AdType.NATIVE_HOME_DIS -> {
                setDisplayHomeNativeAdFlash(activity as MainActivity, adType, onAdClosed)
            }

            AdType.NATIVE_RESULT, AdType.NATIVE_RESULT_DIS -> {
                setDisplayEndNativeAdFlash(activity as EndActivity, adType, onAdClosed)
            }

            AdType.INTERSTITIAL_CONNECT, AdType.INTERSTITIAL_SERVICE, AdType.INTERSTITIAL_RESULT, AdType.INTERSTITIAL_SERVICE_DIS, AdType.INTERSTITIAL_RESULT_DIS -> {
                (ad as? InterstitialAd)?.apply {
                    fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdClosed()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            logAlien("${adType}-广告，展示")
                            clearAd(adType)
                        }
                    }
                    show(activity)
                }
            }
        }
    }

    private fun clearAd(adType: AdType) {
        when (adType) {
            AdType.OPEN -> appOpenAd = null
            AdType.NATIVE_HOME -> nativeHomeAd = null
            AdType.NATIVE_RESULT -> nativeResultAd = null
            AdType.INTERSTITIAL_CONNECT -> interstitialConnectAd = null
            AdType.INTERSTITIAL_SERVICE -> interstitialServiceAd = null
            AdType.INTERSTITIAL_RESULT -> interstitialResultAd = null
            AdType.OPEN_DIS -> appOpenAdDis = null
            AdType.NATIVE_HOME_DIS -> nativeHomeAdDis = null
            AdType.NATIVE_RESULT_DIS -> nativeResultAdDis = null
            AdType.INTERSTITIAL_SERVICE_DIS -> interstitialServiceAdDis
            AdType.INTERSTITIAL_RESULT_DIS -> interstitialResultAdDis = null
        }
    }

    private fun isAppInForeground(context: Context): Boolean {
        val appState =
            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val tasks = appState.getRunningTasks(1)
        return tasks.isNotEmpty() && tasks[0].topActivity?.packageName == context.packageName
    }

    private fun setDisplayHomeNativeAdFlash(
        activity: MainActivity,
        adType: AdType,
        onAdClosed: () -> Unit
    ) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (getAd(adType) as NativeAd).let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED

                if (state) {
                    activity.binding.imgOcAd.isVisible = true

                    if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                        adData.destroy()
                        return@let
                    }
                    val adView = activity.layoutInflater.inflate(
                        R.layout.admob_navtive,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    logAlien("${adType}-广告，展示")
                    clearAd(adType)
                    onAdClosed()
                }
            }
        }
    }

    private fun setDisplayEndNativeAdFlash(
        activity: EndActivity,
        adType: AdType,
        onAdClosed: () -> Unit
    ) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (getAd(adType) as NativeAd).let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED

                if (state) {
                    activity.binding.imgOcAd.isVisible = true

                    if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                        adData.destroy()
                        return@let
                    }
                    val adView = activity.layoutInflater.inflate(
                        R.layout.admob_navtive,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    logAlien("${adType}-广告，展示")
                    clearAd(adType)
                    onAdClosed()
                }
            }
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        nativeAd.mediaContent?.let {
            adView.mediaView?.apply { setImageScaleType(ImageView.ScaleType.CENTER_CROP) }?.mediaContent =
                it
        }
        adView.mediaView?.clipToOutline = true
        adView.mediaView?.outlineProvider = setNativeC()
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as TextView).text = nativeAd.callToAction
        }
        if (nativeAd.headline == null) {
            adView.headlineView?.visibility = View.INVISIBLE
        } else {
            adView.headlineView?.visibility = View.VISIBLE
            (adView.headlineView as TextView).text = nativeAd.headline
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }

    class setNativeC : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val sView = view ?: return
            val sOutline = outline ?: return
            sOutline.setRoundRect(
                0,
                0,
                sView.width,
                sView.height,
                8f
            )
        }
    }

    private fun getLoadIp(adType: AdType): String {
        return when (adType) {
            AdType.OPEN -> {
                openTypeIp
            }

            AdType.INTERSTITIAL_CONNECT -> {
                contTypeIp
            }

            AdType.NATIVE_HOME -> {
                homeTypeIp
            }

            AdType.NATIVE_RESULT -> {
                resultTypeIp
            }

            AdType.INTERSTITIAL_SERVICE -> {
                listTypeIp
            }

            AdType.INTERSTITIAL_RESULT -> {
                endTypeIp
            }

            else -> {
                ""
            }
        }
    }

    private fun clearLoadIp(adType: AdType) {
        when (adType) {
            AdType.OPEN -> {
                openTypeIp = ""
            }

            AdType.INTERSTITIAL_CONNECT -> {
                contTypeIp = ""
            }

            AdType.NATIVE_HOME -> {
                homeTypeIp = ""
            }

            AdType.NATIVE_RESULT -> {
                resultTypeIp = ""
            }

            AdType.INTERSTITIAL_SERVICE -> {
                listTypeIp = ""
            }

            AdType.INTERSTITIAL_RESULT -> {
                endTypeIp = ""
            }

            else -> {}
        }
    }
}



