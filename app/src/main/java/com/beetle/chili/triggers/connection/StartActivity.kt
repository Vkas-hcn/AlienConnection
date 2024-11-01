package com.beetle.chili.triggers.connection

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beetle.chili.triggers.connection.databinding.ActivityMainBinding
import com.beetle.chili.triggers.connection.databinding.VvSsBinding

class StartActivity : AppCompatActivity() {
    val binding by lazy { VvSsBinding.inflate(layoutInflater) }
    private val handler = Handler(Looper.getMainLooper())
    private val progressBarMax = 100
    private val progressIncrement = 5
    private var currentProgress = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 设置进度条的最大值
        binding.alienProgressBar.max = progressBarMax
        // 启动进度条更新
        startProgress()

    }

    private fun startProgress() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentProgress < progressBarMax) {
                    currentProgress += progressIncrement
                    binding.alienProgressBar.progress = currentProgress
                    handler.postDelayed(this, 100)
                } else {
                    navigateToHome()
                }
            }
        }
        handler.post(runnable)
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}