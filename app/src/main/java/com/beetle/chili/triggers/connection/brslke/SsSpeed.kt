package com.beetle.chili.triggers.connection.brslke

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.beetle.chili.triggers.connection.imsued.SpeedImp


class SsSpeed(val imp: SpeedImp) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        imp.speedLong(p1?.getStringExtra("download") ?: "0", p1?.getStringExtra("upload") ?: "0")
    }
}