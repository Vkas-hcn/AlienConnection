package com.beetle.chili.triggers.connection.aleis

import com.beetle.chili.triggers.connection.uasndje.MainActivity
import android.app.Application
import android.content.Context
import com.github.shadowsocks.Core

class App : Application() {

    companion object {
        lateinit var appComponent: Context
        lateinit var thisApplication: Application

        var vvState = false

    }

    override fun onCreate() {
        super.onCreate()
        appComponent = this
        thisApplication = this
        Core.init(this, MainActivity::class)
        Core.stopService()
        vvState = false

    }



}
