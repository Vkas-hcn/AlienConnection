package com.beetle.chili.triggers.connection.adkfieo

import androidx.annotation.Keep

@Keep
data class AdConfig(
    val sbjiortb: Int,
    val crhpjkr: Int,
    val sre: Map<String, String>,
    val lcl: Map<String, String>
)
@Keep
data class AdLjBean(
    val Bose1: String,
    val Bose2: String,
    val Bose3: String,
)
// 枚举类定义广告类型
enum class AdType {
    OPEN,
    NATIVE_HOME,
    NATIVE_RESULT,
    INTERSTITIAL_CONNECT,
    INTERSTITIAL_SERVICE,
    INTERSTITIAL_RESULT,
    OPEN_DIS,
    NATIVE_HOME_DIS,
    NATIVE_RESULT_DIS,
    INTERSTITIAL_SERVICE_DIS,
    INTERSTITIAL_RESULT_DIS
}

// 枚举类定义广告源
enum class AdSource {
    SRE, LCL
}