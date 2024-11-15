package com.beetle.chili.triggers.connection.aleis

import android.app.Activity
import android.app.ActivityManager
import com.beetle.chili.triggers.connection.uasndje.MainActivity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import com.beetle.chili.triggers.connection.adkfieo.AdManager
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.logAlien
import com.beetle.chili.triggers.connection.uasndje.StartActivity
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.github.shadowsocks.Core
import com.google.android.gms.ads.AdActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
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
                if(it.isBlank()){
                    DataUtils.BID=UUID.randomUUID().toString()
                }
            }
            GetMobData.getBlackList(this)
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
        }
        if (isFlashAppBackGround) {
            isFlashAppBackGround = false
            if ((System.currentTimeMillis() - exitAppTime) / 1000 > 3) {
                toSplash(activity)
            }
        }
    }



    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {
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
}
