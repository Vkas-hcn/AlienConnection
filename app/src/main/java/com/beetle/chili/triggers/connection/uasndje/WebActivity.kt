package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.databinding.VvEeBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding
import com.beetle.chili.triggers.connection.databinding.VvWwBinding
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.ZongData
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WebActivity : AppCompatActivity() {
    val binding by lazy { VvWwBinding.inflate(layoutInflater) }
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
            putExtra("web", "web")
        }
        setResult(Activity.RESULT_OK, data)
        clickMainBtn()
        initData()
    }

    private fun clickMainBtn() {
        binding.appCompatTextView.setOnClickListener {
            finish()
        }
    }

    fun initData(){
        binding.webPp.webViewClient = WebViewClient()

        binding.webPp.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.alienProgressBar.progress = newProgress
                if (newProgress >= 100) binding.alienProgressBar.visibility = View.INVISIBLE
            }
        }
        binding.webPp.loadUrl(ZongData.ppUrl)
    }
}