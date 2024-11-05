package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.adapter.ServiceAdaoter
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvLlBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.google.gson.Gson

class ListActivity : AppCompatActivity() {
    val binding by lazy { VvLlBinding.inflate(layoutInflater) }
    private var vpnServerBeanList: MutableList<VInForBean>? = null
    private var serviceAdaoter: ServiceAdaoter? = null
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
    }

    private fun intData() {
        val bean = DataUtils.getNowVpn()
        binding.listSmart.imgCheck.setImageResource(if (bean.isSmart && App.vvState) R.drawable.item_c else R.drawable.item_dis)
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
            finish()
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
            .setPositiveButton("YES") { _, _ -> nextFun() }
            .setNegativeButton("NO", null)
            .show()
    }
}