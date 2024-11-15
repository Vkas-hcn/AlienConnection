package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.ContentValues
import android.content.ContextParams
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.adkfieo.AdManager
import com.beetle.chili.triggers.connection.adkfieo.AdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.wekgisa.LoadingDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EndActivity : AppCompatActivity() {
    val binding by lazy { VvEeBinding.inflate(layoutInflater) }
    private var jobEnd: Job? = null
    private var jobResultJob: Job? = null
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val data = Intent().apply {
            // Add any data you want to return
            putExtra("end", "end")
        }
        setResult(Activity.RESULT_OK, data)
        loadingDialog = LoadingDialog(this)
        showVpnState()
        clickMainBtn()
        showResultAd()
    }

    private fun clickMainBtn() {
        binding.appCompatTextView.setOnClickListener {
            nextFUn()
        }
        onBackPressedDispatcher.addCallback {
            nextFUn()
        }
        binding.tvFast.setOnClickListener {
            navigateToHome("fast")
        }

        binding.tvRe.setOnClickListener {
            navigateToHome("flushed")
        }
    }

    private fun nextFUn() {
        showEndIntAd {
            finish()
            loadingDialog.hideLoading()
        }
    }

    private fun showVpnState() {
        binding.vpnState = DataUtils.getVpnIsConnect()
        if (!DataUtils.getVpnIsConnect()) {
            DataUtils.editHisVpn(DataUtils.getNowVpn().vpnDate)
        } else {
            DataUtils.endTime = "00:00:00"
        }
        if (DataUtils.clickVpn.isNotBlank()) {
            DataUtils.nowVpn = DataUtils.clickVpn
            DataUtils.clickVpn = ""
        }
        if (DataUtils.getVpnIsConnect()) {
            binding.endTime.text = getString(R.string.connect_succeeded)
            getEndTIme()
            showInformation()
        } else {
            binding.endTimeValue.text = DataUtils.endTime
            binding.endTime.text = getString(R.string.disconnected_succeed)
        }
    }

    private fun getEndTIme() {
        lifecycleScope.launch {
            while (isActive) {
                binding.endTimeValue.text = DataUtils.endTime
                delay(1000)
            }
        }
    }

    private fun showInformation() {
        val nowBean = DataUtils.getNowVpn()
        binding.tvIp2.text = nowBean.host
        binding.tvCou2.text = nowBean.country_name
        binding.tvCity2.text = nowBean.city
    }

    private fun navigateToHome(key: String) {
        val data = Intent().apply {
            putExtra("end", key)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun showEndIntAd(nextFun: () -> Unit) {
        jobEnd?.cancel()
        jobEnd = null
        jobEnd = lifecycleScope.launch {
            if (GetMobData.getAdBlackData()) {
                jobEnd?.cancel()
                jobEnd = null
                nextFun()
                return@launch
            }
            if (AdManager.canShowAd(this@EndActivity, GetMobData.getEndAdType()) != 2) {
                AdManager.loadAd(this@EndActivity, GetMobData.getEndAdType())
            }
            loadingDialog.showLoading()
            try {
                withTimeout(5000L) {
                    while (isActive) {
                        if (AdManager.canShowAd(
                                this@EndActivity,
                                GetMobData.getEndAdType()
                            ) == 2
                        ) {
                            AdManager.showAd(this@EndActivity, GetMobData.getEndAdType()) {
                                nextFun()
                            }
                            break
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                nextFun()
            }
        }
    }

    private fun showResultAd() {
        jobResultJob?.cancel()
        jobResultJob = null
        binding.adLayout.isVisible = true
        binding.imgOcAd.isVisible = true
        if (AdManager.canShowAd(this@EndActivity, GetMobData.getResultAdType()) == 1) {
            binding.adLayoutAdmob.isVisible = false
            AdManager.loadAd(this, GetMobData.getResultAdType())
        }
        jobResultJob = lifecycleScope.launch {
            delay(300)
            while (isActive) {
                if (AdManager.canShowAd(this@EndActivity, GetMobData.getResultAdType()) == 2) {
                    AdManager.showAd(this@EndActivity, GetMobData.getResultAdType()) {

                    }
                    jobResultJob?.cancel()
                    jobResultJob = null
                    break
                }
                delay(500L)
            }
        }
    }
}