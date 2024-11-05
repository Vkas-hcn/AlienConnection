package com.beetle.chili.triggers.connection.blkfh

import androidx.annotation.Keep
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Keep
data class AlienBean(
    val code: Int,
    val `data`: Data,
    val msg: String
)

@Keep
data class Data(
    @SerializedName("TVJAd")
    val server_list: MutableList<VInForBean>,
    @SerializedName("PUPOKDOVe")
    val smart_list: MutableList<VInForBean>
)

@Keep
data class VInForBean(
    @SerializedName("jse")
    var host: String = "",

    @SerializedName("fPFraKf")
    var port: Int = 0,
    @SerializedName("ABtbZQk")
    var password: String = "",

    @SerializedName("AHXUfig")
    var methode: String = "",

    @SerializedName("drsagpZSH")
    var city: String = "",

    @SerializedName("bGblZeK")
    var country_name: String = "",

    var isSmart: Boolean = false,

    var connectTime: String = "",
    var vpnDate: String = ""
) : Serializable {
    fun getName(): String {
        return if (isSmart) "Smart Server" else "$country_name-$city"
    }

    fun getFistName(): String {
        return if (isSmart) "Smart Server" else country_name
    }

    fun getLastName(): String {
        return city
    }

    fun getIcon(): Int {
        return DataUtils.getCountryIcon(getFistName())
    }

    fun getTag(): String {
        return "01"
    }

    fun getVpnDateTime(): String {
        // 格式 10/24 2024
        val dateFormat = SimpleDateFormat("MM/dd yyyy", Locale.getDefault())
        val timestamp = vpnDate.toLong()
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

}