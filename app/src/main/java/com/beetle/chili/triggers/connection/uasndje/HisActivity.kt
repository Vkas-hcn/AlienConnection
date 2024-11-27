package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.adapter.HistoryAdaoter
import com.beetle.chili.triggers.connection.adapter.ServiceAdaoter
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvHisBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HisActivity : AppCompatActivity() {
    val binding by lazy { VvHisBinding.inflate(layoutInflater) }
    private var vpnServerBeanList: MutableList<VInForBean>? = null
    private var serviceAdaoter: HistoryAdaoter? = null
    var endTimeJob: Job? = null
    var totalTime = "00:00:00"
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
            putExtra("end", "end")
        }
        setResult(Activity.RESULT_OK, data)
        clickMainBtn()
        initAdapter()
        PutDataUtils.postPointData("p_history_view")
    }

    private fun clickMainBtn() {
        binding.appCompatTextView.setOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        vpnServerBeanList = DataUtils.getHisListVpn()
        val (totalTimeEnd, mostConnectedServer) = DataUtils.getConnectionStats()
        totalTime = totalTimeEnd
        binding.tvIp2.text = totalTime
        val countryName = mostConnectedServer?.let { DataUtils.extractCountryName(it) }
        countryName?.let { DataUtils.getCountryIcon(it) }
            ?.let { binding.itemFfff.setImageResource(it) }
        binding.tvCccc.text = mostConnectedServer
        binding.hisList.layoutManager = LinearLayoutManager(this)
        serviceAdaoter = vpnServerBeanList?.let { HistoryAdaoter(it, this@HisActivity) }
        binding.hisList.adapter = serviceAdaoter
        if (vpnServerBeanList?.isEmpty() == true) {
            binding.hisList.visibility = android.view.View.GONE
            binding.tvNoData.visibility = android.view.View.VISIBLE
        }
    }

    fun getEndTIme(item: VInForBean, tvTime: TextView) {
        endTimeJob?.cancel()
        endTimeJob = lifecycleScope.launch {
            while (isActive) {
                tvTime.text = DataUtils.endTime
                DataUtils.editHisVpn(item.vpnDate)
                binding.tvIp2.text = DataUtils.addTimes(totalTime,DataUtils.endTime)
                delay(1000)
            }
        }
    }
}