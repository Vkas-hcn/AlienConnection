package com.beetle.chili.triggers.connection.uskde

import com.beetle.chili.triggers.connection.BuildConfig

object ZongData {
    val ppUrl = if (BuildConfig.DEBUG) {
        "https://www.baidu.com/"
    } else {
        "https://api.v2rayss.com/v2/v2ray/v2ray_list.php"
    }
    val vpnOnline = if (BuildConfig.DEBUG) {
        "https://test.shieldproect.com/cZM/MkX/RQF/"
    } else {
        "https://api.shieldproect.com/cZM/MkX/RQF/"
    }
}