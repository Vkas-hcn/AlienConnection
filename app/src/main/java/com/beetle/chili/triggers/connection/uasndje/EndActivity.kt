package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class EndActivity : AppCompatActivity() {
    val binding by lazy { VvEeBinding.inflate(layoutInflater) }
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
        showVpnState()
        clickMainBtn()
    }

    private fun clickMainBtn() {
        binding.appCompatTextView.setOnClickListener {
            finish()
        }
        binding.tvFast.setOnClickListener {
            navigateToHome("fast")
        }

        binding.tvRe.setOnClickListener {
            navigateToHome("flushed")
        }
    }

    private fun showVpnState() {
        binding.vpnState = DataUtils.getVpnIsConnect()
        if (!DataUtils.getVpnIsConnect()) {
            DataUtils.editHisVpn(DataUtils.getNowVpn().vpnDate)
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
            DataUtils.endTime = "00:00:00"
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
}