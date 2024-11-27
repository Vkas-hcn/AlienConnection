package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.adapter.ServiceAdaoter
import com.beetle.chili.triggers.connection.adkfieo.AdManager
import com.beetle.chili.triggers.connection.adkfieo.AdType
import com.beetle.chili.triggers.connection.adkfieo.GetMobData
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvLlBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.wekgisa.LoadingDialog
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ListActivity : AppCompatActivity() {
    val binding by lazy { VvLlBinding.inflate(layoutInflater) }
    private var vpnServerBeanList: MutableList<VInForBean>? = null
    private var serviceAdaoter: ServiceAdaoter? = null
    private lateinit var loadingDialog: LoadingDialog
    private var jobService: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intData()
        clickBtn()
        initAdapter()
        PutDataUtils.postPointData("p_list_view", "sate", PutDataUtils.getVpnConnectName())
    }

    private fun intData() {
        loadingDialog = LoadingDialog(this)
        val bean = DataUtils.getNowVpn()
        binding.listSmart.imgCheck.setImageResource(if (bean.isSmart && App.vvState) R.drawable.item_c else R.drawable.item_dis)
        AdManager.loadAd(this, GetMobData.getServiceAdType())
    }

    private fun initAdapter() {
        vpnServerBeanList = DataUtils.getListVpn()
        binding.noData = vpnServerBeanList == null
        binding.listList.layoutManager = LinearLayoutManager(this)
        serviceAdaoter = vpnServerBeanList?.let { ServiceAdaoter(it, this) }
        binding.listList.adapter = serviceAdaoter
        binding.listSmart.itemLayout.setOnClickListener {
            val jsonBean = DataUtils.getSmartList()
            if (App.vvState) {
                showDisConnectFun {
                    DataUtils.clickVpn = Gson().toJson(jsonBean)
                    endThisPage()
                }
            } else {
                DataUtils.nowVpn = Gson().toJson(jsonBean)
                endThisPage()
            }
        }
    }

    private fun clickBtn() {
        binding.appCompatTextView.setOnClickListener {
            showServiceIntAd {
                nextBackFun()
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            showServiceIntAd {
                nextBackFun()
            }
        }
    }

    fun endThisPage() {
        val data = Intent().apply {
            putExtra("end", "list")
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun showDisConnectFun(nextFun: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Tip")
            .setMessage("Do you want to switch servers? You need to disconnect from the current server before switching. Confirm to continue?")
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("YES") { _, _ ->
                nextFun()
                PutDataUtils.postPointData("p_switch_server")
            }
            .setNegativeButton("NO", null)
            .show()
    }

    private fun nextBackFun() {
        val data = Intent().apply {
            putExtra("end", "backlist")
        }
        setResult(Activity.RESULT_OK, data)
        loadingDialog.hideLoading()
        finish()
    }

    private fun showServiceIntAd(nextFun: () -> Unit) {
        jobService?.cancel()
        jobService = null
        jobService = lifecycleScope.launch {
            PutDataUtils.postPointData("p_list_back")
            if (GetMobData.getAdBlackData() || AdManager.caoState(GetMobData.getServiceAdType())) {
                jobService?.cancel()
                jobService = null
                nextFun()
                return@launch
            }
            if (AdManager.canShowAd(this@ListActivity, GetMobData.getServiceAdType()) != 2) {
                AdManager.loadAd(this@ListActivity, GetMobData.getServiceAdType())
            }
            loadingDialog.showLoading()
            try {
                withTimeout(5000L) {
                    while (isActive) {
                        if (AdManager.canShowAd(
                                this@ListActivity,
                                GetMobData.getServiceAdType()
                            ) == 2
                        ) {
                            AdManager.showAd(this@ListActivity, GetMobData.getServiceAdType()) {
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
}