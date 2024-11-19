package com.beetle.chili.triggers.connection.aleis

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import com.beetle.chili.triggers.connection.uasndje.MainActivity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.beetle.chili.triggers.connection.BuildConfig
import com.beetle.chili.triggers.connection.adkfieo.AdManager
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.logAlien
import com.beetle.chili.triggers.connection.uasndje.StartActivity
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.github.shadowsocks.Core
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class App : Application(), Application.ActivityLifecycleCallbacks {
    var adActivity: Activity? = null
    var acFlashTotal = 0
    var exitAppTime = 0L
    var isFlashAppBackGround: Boolean = false

    companion object {
        lateinit var appComponent: Context
        lateinit var thisApplication: Application
        var isHotStart: Boolean = false
        var vvState = false
        var paPageName = ""
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = this
        thisApplication = this
        Core.init(this, MainActivity::class)
        Core.stopService()
        vvState = false
        registerActivityLifecycleCallbacks(this)
        if (isMainProcess(this)) {
            AdManager.init(this)
            Firebase.initialize(this)
            FirebaseApp.initializeApp(this)
            DataUtils.BID.let {
                if (it.isBlank()) {
                    DataUtils.BID = UUID.randomUUID().toString()
                }
            }
            GetMobData.getBlackList(this)
            initAdJust(this)
            CoroutineScope(Dispatchers.IO).launch {
                val adId = runCatching {
                    AdvertisingIdClient.getAdvertisingIdInfo(appComponent)?.id
                }.getOrNull() ?: ""
                Log.d("AdId", "Google Advertising ID: $adId")
                DataUtils.advertising_google = adId
            }

        }
    }

    private fun isMainProcess(context: Context): Boolean {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in runningApps) {
            if (appProcess.pid == pid && packageName == appProcess.processName) {
                return true
            }
        }
        return false
    }

    private fun toSplash(activity: Activity) {

        if (activity is StartActivity) {
            activity.finish()
        }
        val intent = Intent(activity, StartActivity::class.java)
        activity.startActivity(intent)
        if (adActivity != null) {
            adActivity?.finish()
        }
        logAlien("toSplash")
        isHotStart = true
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        acFlashTotal++

        if (activity is AdActivity) {
            adActivity = activity
        } else {
            paPageName = activity.javaClass.simpleName
        }
        if (isFlashAppBackGround) {
            isFlashAppBackGround = false
            if ((System.currentTimeMillis() - exitAppTime) / 1000 > 3) {
                toSplash(activity)
            }
        }
    }


    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(activity: Activity) {
        acFlashTotal--
        if (acFlashTotal == 0) {
            isFlashAppBackGround = true
            exitAppTime = System.currentTimeMillis()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    @SuppressLint("HardwareIds")
    private fun initAdJust(application: Application) {
        Adjust.addSessionCallbackParameter(
            "customer_user_id",
            DataUtils.BID
        )
        val appToken: String
        val environment: String

        if (BuildConfig.DEBUG) {
            appToken = "ih2pm2dr3k74"
            environment = AdjustConfig.ENVIRONMENT_SANDBOX
        } else {
            appToken = "pc37fmeecd8g"
            environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        }
        val config = AdjustConfig(application, appToken, environment)
        config.needsCost = true
        config.setOnAttributionChangedListener { attribution ->
            Log.e("TAG", "adjust=${attribution}")
            if (DataUtils.adjv == "1" && attribution.network.isNotEmpty() && attribution.network.contains(
                    "organic",
                    true
                ).not()
            ) {
                DataUtils.adjv = "1"
            }
        }
        Adjust.onCreate(config)
    }
}
