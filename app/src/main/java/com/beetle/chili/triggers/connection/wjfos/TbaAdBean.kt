package com.beetle.chili.triggers.connection.wjfos

import androidx.annotation.Keep

@Keep
data class TbaAdBean(
    var adWhere: String,
    var adId: String,
    var adType: String,
    var ttd_load_ip: String = "",
    var ttd_load_city: String = "",
    var ttd_show_ip: String = "",
    var ttd_show_city: String = ""
)


