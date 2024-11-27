package com.beetle.chili.triggers.connection.uasndje

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.adkfieo.AdManager
import com.beetle.chili.triggers.connection.adkfieo.AdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.getConnectAdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.getHomeAdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.getOpenAdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData.logAlien
import com.beetle.chili.triggers.connection.adkfieo.Postadmin
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.NetGet
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class StartActivity : AppCompatActivity() {
    val binding by lazy { VvSsBinding.inflate(layoutInflater) }
    private val handler = Handler(Looper.getMainLooper())
    private val progressBarMax = 100
    private val progressIncrement = 1
    private var currentProgress = 0
    private var fileBaseJob: Job? = null
    private var jobOpenTdo: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ouMSetAdSdk()
        lifecycleScope.launch(Dispatchers.IO) {
            haveRefDataChangingBean(this@StartActivity)
            NetGet.inspectCountry()
            DataUtils.getOnlineVpnData(this@StartActivity)
            PutDataUtils.emitSessionData()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.alienProgressBar.max = progressBarMax
        startProgress()
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }

    private fun startProgress() {
        getFileBaseData()
        val runnable = object : Runnable {
            override fun run() {
                if (currentProgress < progressBarMax) {
                    currentProgress += progressIncrement
                    binding.alienProgressBar.progress = currentProgress
                    handler.postDelayed(this, 120)
                }
            }
        }
        handler.post(runnable)
    }

    private fun getFileBaseData() {
        fileBaseJob = lifecycleScope.launch {
            var isCa = false
            val auth = Firebase.remoteConfig
            auth.fetchAndActivate().addOnSuccessListener {
                DataUtils.firebaseAd = GetMobData.base64Decode(auth.getString(DataUtils.o_ad_key))
                DataUtils.firebaseGz = GetMobData.base64Decode(auth.getString(DataUtils.o_gz_key))
                logAlien("initFirebase======: ${auth.getString(DataUtils.o_gz_key)}")
                logAlien("initFirebase: ${DataUtils.firebaseGz}")
                GetMobData.getLjData().Bose2.let {
                    if (it.isNotBlank()) {
                        logAlien("initFaceBook: $it")
                        FacebookSdk.setApplicationId(it)
                        FacebookSdk.sdkInitialize(App.appComponent)
                        AppEventsLogger.activateApp(App.thisApplication)
                    }
                }

                isCa = true
            }
            try {
                withTimeout(4000L) {
                    while (true) {
                        if (!isActive) {
                            break
                        }
                        if (isCa) {
                            getIsOuMFinish()
                            cancel()
                            fileBaseJob = null
                        }
                        delay(500)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                cancel()
                fileBaseJob = null
                getIsOuMFinish()
            }
        }
    }

    private fun loadingAd() {
        AdManager.loadAd(this, getOpenAdType())
        AdManager.loadAd(this, getHomeAdType())
        showFinishAd()
    }

    private fun showFinishAd() {
        jobOpenTdo?.cancel()
        jobOpenTdo = null
        jobOpenTdo = lifecycleScope.launch {
            delay(1000)
            if (AdManager.caoShow()) {
                onCountdownFinished()
                return@launch
            }
            try {
                withTimeout(10000L) {
                    while (isActive) {
                        if (AdManager.canShowAd(this@StartActivity, getOpenAdType()) == 2) {
                            AdManager.showAd(this@StartActivity, getOpenAdType()) {
                                onCountdownFinished()
                            }
                            break
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                onCountdownFinished()
            }
        }
    }

    private fun onCountdownFinished() {
        jobOpenTdo?.cancel()
        jobOpenTdo = null
        binding.alienProgressBar.progress = 100
        navigateToHome()
    }

    private fun getIsOuMFinish() {
        GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if (DataUtils.ouMState == "1") {
                    loadingAd()
                    break
                }
                delay(500)
            }
        }
    }

    private fun navigateToHome() {
        lifecycleScope.launch {
            delay(200)
            if (lifecycle.currentState.name == Lifecycle.State.RESUMED.name) {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun ouMSetAdSdk() {
        if (DataUtils.ouMState == "1") {
            return
        }
        val debugSettings =
            ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("E40FFA71B6A6FDF9954A2AB978DD556D")
                .build()
        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()
        val consentInformation: ConsentInformation =
            UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params, {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) {
                    if (consentInformation.canRequestAds()) {
                        DataUtils.ouMState = "1"
                    }
                }
            },
            {
                DataUtils.ouMState = "1"
            }
        )
    }

    private fun haveRefDataChangingBean(context: Context) {
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(p0: Int) {
                    when (p0) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            DataUtils.admin_ref_data =
                                referrerClient.installReferrer.installReferrer ?: ""
                            val timeElapsed =
                                ((System.currentTimeMillis() - App.appTimeStart) / 1000).toInt()
                            lifecycleScope.launch(Dispatchers.IO) {
                                Postadmin().getAdminData(this@StartActivity)
                                PutDataUtils.postPointData("u_rf", "time", timeElapsed)
                            }
                            PutDataUtils.emitInstallData(
                                context,
                                referrerClient.installReferrer
                            )
                        }
                    }
                    referrerClient.endConnection()
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }.onFailure { e ->
        }

    }

}