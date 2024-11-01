package com.github.shadowsocks

import android.app.Application

class AppCore:Application() {
    companion object{
        lateinit var instance: AppCore
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}