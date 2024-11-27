package com.beetle.chili.triggers.connection.adkfieo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.NetGet
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils
import com.beetle.chili.triggers.connection.wjfos.PutDataUtils.getAppVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Postadmin {
    @SuppressLint("HardwareIds")
    fun adminData(): String {
        return JSONObject().apply {
            put("SHcEfzvia", "com.secure.shield.wave.protect.fast\t")
            put("tydhH", DataUtils.BID)
            put("VFntwRQjQ", DataUtils.admin_ref_data)
            put("pOXSCSAih", getAppVersion())
        }.toString()
    }
    suspend fun getAdminData(context: Context) = withContext(Dispatchers.IO) {
        val params = adminData()
        Log.e("TAG", "getAdminData: -params-$params")

        val maxRetries = 2
        var attempt = 0
        val timeStart = System.currentTimeMillis()
        val appTimeEnd = ((System.currentTimeMillis() - App.appTimeStart) / 1000).toInt()
        PutDataUtils.postPointData("u_admin_ask", "time", appTimeEnd)
        while (attempt <= maxRetries) {
            try {
                val response = postAdminDataWithRetry(
                    context,
                    "https://wace.shieldproect.com/api/asdev/",
                    params
                )
                val timeEnd = ((System.currentTimeMillis() - timeStart) / 1000).toInt()
                PutDataUtils.postPointData("u_admin_result", "time", timeEnd)
                DataUtils.admin_1_data = getAdminBackData(response)
                DataUtils.admin_2_data = getAdminRefData(response)
                postu_buyingData()
                Log.e(
                    "TAG",
                    "getAdminData-Success: $response---${DataUtils.admin_1_data}----${DataUtils.admin_2_data}"
                )
                break

            } catch (e: Exception) {
                Log.e("TAG", "getAdminData-Exception: $e")
                if (attempt >= maxRetries) {
                    Log.e("TAG", "getAdminData-FinalFailure: Max retries reached.")
                }
                attempt++
            }
        }
    }

    private suspend fun postAdminDataWithRetry(context: Context, url: String, params: Any): String =
        suspendCoroutine { continuation ->
            postAdminData(
                context,
                url,
                params,
                object : NetGet.Callback {
                    override fun onSuccess(response: String) {
                        continuation.resume(response)
                    }

                    override fun onFailure(error: String) {
                        continuation.resumeWithException(Exception(error))
                    }
                }
            )
        }


    private fun getAdminBackData(jsonString: String): String {
        val outerJson = JSONObject(jsonString)
        val confString = outerJson.getJSONObject("MgjgHP").getString("conf")
        val confJson = JSONObject(confString)
        return confJson.getString("Bo_t")
    }

    private fun getAdminRefData(jsonString: String): String {
        val outerJson = JSONObject(jsonString)
        val confString = outerJson.getJSONObject("MgjgHP").getString("conf")
        val confJson = JSONObject(confString)
        return confJson.getString("Bo_s")
    }

    private fun postu_buyingData() {
        if (DataUtils.admin_2_data == "1") {
            PutDataUtils.postPointData("u_buying")
        }
    }
    fun getAdminPingData(): Boolean {
        return DataUtils.admin_1_data == "2"
    }

    private fun postAdminData(context: Context, url: String, body: Any, callback: NetGet.Callback) {
        val jsonBodyString = JSONObject(body.toString()).toString()
        val timestamp = System.currentTimeMillis().toString()
        val xorEncryptedString = xorWithTimestamp(jsonBodyString, timestamp)
        val base64EncodedString = Base64.encodeToString(
            xorEncryptedString.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP
        )
        var responseHeaders: Map<String, String> = emptyMap()
        val request = object : StringRequest(
            Method.POST,
            url,
            { response ->
                try {
                    val timestampResponse = responseHeaders["timestamp"] ?: throw IllegalArgumentException("Timestamp missing in headers")
                    val decodedBytes = Base64.decode(response.toString(), Base64.DEFAULT)
                    val decodedString = String(decodedBytes, Charsets.UTF_8)
                    val finalData = xorWithTimestamp(decodedString, timestampResponse)
                    val jsonResponse = JSONObject(finalData)
                    callback.onSuccess(jsonResponse.toString())
                } catch (e: Exception) {
                    callback.onFailure("Decryption failed: ${e.message}")
                }
            },
            { error ->
                if (error is TimeoutError) {
                    callback.onFailure("Request timed out. Please try again later.")
                } else {
                    callback.onFailure(error.toString())
                }
            }
        ) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                responseHeaders = response.headers!! // 保存 headers
                val parsed = String(response.data, Charsets.UTF_8) // 将响应数据解析为字符串
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
            }
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["timestamp"] = timestamp  // 将时间戳放入 header
                return headers
            }

            override fun getBody(): ByteArray {
                return base64EncodedString.toByteArray(StandardCharsets.UTF_8)
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            5000, // timeout in milliseconds
            2, // number of retries
            2.0f // backoff multiplier
        )
        Volley.newRequestQueue(context).add(request)
    }

    private fun xorWithTimestamp(text: String, timestamp: String): String {
        val cycleKey = timestamp.toCharArray()
        val keyLength = cycleKey.size
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % keyLength].toInt()).toChar()
        }.joinToString("")
    }
}