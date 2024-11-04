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
    val server_list: MutableList<VInForBean>,
    val smart_list: MutableList<VInForBean>
)

@Keep
data class VInForBean(
    var host: String = "",
    var port: Int = 0,
    var password: String = "",
    var methode: String = "",

    var city: String = "",
    var country_name: String = "",
    var isSmart: Boolean = false,

    var connectTime: String = "",
    var vpnDate: String = ""
) : Serializable {
    fun getName(): String {
        return if (isSmart) "Smart Server" else "$country_name - $city"
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
        // 格式10/24 2024
        val dateFormat = SimpleDateFormat("MM/dd yyyy", Locale.getDefault())
        val date = Date(vpnDate)
        return dateFormat.format(date)
    }

}