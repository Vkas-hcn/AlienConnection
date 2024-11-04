package com.beetle.chili.triggers.connection.uasndje

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
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
        initAdapter()
    }

    private fun intData() {
        val bean = DataUtils.getNowVpn()
        binding.listSmart.imgCheck.setImageResource(if (bean.isSmart && App.vvState) R.drawable.item_c else R.drawable.item_dis)
    }

    private fun initAdapter() {
        vpnServerBeanList = DataUtils.getListVpn()
        binding.listList.layoutManager = LinearLayoutManager(this)
        serviceAdaoter = vpnServerBeanList?.let { ServiceAdaoter(it, this) }
        binding.listList.adapter = serviceAdaoter
    }
}