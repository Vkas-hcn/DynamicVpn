package com.hcn.dynamicvpn.utils

import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.reflect.TypeToken
import com.hcn.dynamicvpn.R
import com.hcn.dynamicvpn.bean.DynamicVpnBean
import com.hcn.dynamicvpn.data.DataUtils
import com.squareup.moshi.Json
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

object DynamicUtils {
    fun getFastIpDynamic(): DynamicVpnBean {
        val ufVpnBean: MutableList<DynamicVpnBean> = getLocalServerData()
        val intersectionList =
            findFastAndOrdinaryIntersection(ufVpnBean).takeIf { it.isNotEmpty() } ?: ufVpnBean
        return intersectionList.shuffled().first().apply {
            dynamic_best = true
            dynamic_country = "Fast Service"
        }
    }

    private fun findFastAndOrdinaryIntersection(ufVpnBeans: MutableList<DynamicVpnBean>): MutableList<DynamicVpnBean> {
        val intersectionList: MutableList<DynamicVpnBean> = mutableListOf()
        val fastServerData = getLocalFastServerData()
        intersectionList.addAll(ufVpnBeans.filter { fastServerData.contains(it.dynamic_city) })
        return intersectionList
    }


    fun getLocalServerData(): MutableList<DynamicVpnBean> {
        return GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_vpn.json"),
            object : TypeToken<MutableList<DynamicVpnBean>?>() {}.type
        )
    }


    private fun getLocalFastServerData(): MutableList<String> {
        return  GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_fast.json"),
            object : TypeToken<MutableList<String>>() {}.type
        )
    }

//    fun getIpInformation11() {
//        val sb = StringBuffer()
//        try {
//            val url = URL("https://ip.seeip.org/geoip/")
//            val conn = url.openConnection() as HttpURLConnection
//            conn.requestMethod = "GET"
//            conn.connectTimeout = 10000
//            val code = conn.responseCode
//            if (code == 200) {
//                val `is` = conn.inputStream
//                val b = ByteArray(1024)
//                var len: Int
//                while (`is`.read(b).also { len = it } != -1) {
//                    sb.append(String(b, 0, len, Charset.forName("UTF-8")))
//                }
//                `is`.close()
//                conn.disconnect()
//                LogUtils.e("state", "sb==$sb")
//                DataUtils.IP_INFORMATION_DYNAMIC = sb.toString()
//            }
//        } catch (var1: Exception) {
//            LogUtils.e("state", "Exception==${var1.message}")
//            getIpInformation2()
//        }
//    }


    fun getIpInformation() {
        val url = "https://ip.seeip.org/geoip/"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    body?.let {
                        DataUtils.IP_INFORMATION_DYNAMIC = it
                        // 在这里处理你的响应数据
                    }
                } else {
                    getIpInformation2()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 处理网络请求失败的情况
                getIpInformation2()
            }
        })
    }

    fun getIpInformation2() {
        val url = "https://api.myip.com/"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    body?.let {
                        DataUtils.IP_INFORMATION_DYNAMIC2 = it
                        // 在这里处理你的响应数据
                    }
                } else {
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 处理网络请求失败的情况
            }
        })
    }


//    fun getIpInformation2() {
//        val sb = StringBuffer()
//        try {
//            val url = URL("https://api.myip.com/")
//            val conn = url.openConnection() as HttpURLConnection
//            conn.requestMethod = "GET"
//            conn.connectTimeout = 10000
//            val code = conn.responseCode
//            if (code == 200) {
//                val `is` = conn.inputStream
//                val b = ByteArray(1024)
//                var len: Int
//                while (`is`.read(b).also { len = it } != -1) {
//                    sb.append(String(b, 0, len, Charset.forName("UTF-8")))
//                }
//                `is`.close()
//                conn.disconnect()
//                LogUtils.e("state", "sb2==$sb")
//                DataUtils.IP_INFORMATION_DYNAMIC2 = sb.toString()
//            } else {
//                LogUtils.e("state", "code2==$code")
//            }
//        } catch (var1: Exception) {
//            LogUtils.e("state", "Exception2==${var1.message}")
//        }
//    }

    /**
     * 通过国家获取国旗
     */
    fun getFlagThroughCountryDynamic(img: String): Int {
        when (img) {
            "Fast" -> {
                return R.drawable.ic_fast
            }
            "Japan" -> {
                return R.drawable.japan
            }
            "United Kingdom" -> {
                return R.drawable.ic_fast
            }
            "United States" -> {
                return R.drawable.ic_fast
            }
            "Australia" -> {
                return R.drawable.australia
            }
            "Belgium" -> {
                return R.drawable.belgium
            }
            "Brazil" -> {
                return R.drawable.ic_brazil
            }
            "Canada" -> {
                return R.drawable.ic_canada
            }
            "France" -> {
                return R.drawable.ic_france
            }
            "Germany" -> {
                return R.drawable.ic_germany
            }
            "India" -> {
                return R.drawable.ic_india
            }
            "Ireland" -> {
                return R.drawable.ic_ireland
            }
            "Italy" -> {
                return R.drawable.ic_italy
            }
            "South Korea" -> {
                return R.drawable.koreasouth
            }
            "Netherlands" -> {
                return R.drawable.netherlands
            }
            "Newzealand" -> {
                return R.drawable.newzealand
            }
            "Norway" -> {
                return R.drawable.norway
            }
            "Russianfederation" -> {
                return R.drawable.russianfederation
            }
            "Singapore" -> {
                return R.drawable.singapore
            }
            "Sweden" -> {
                return R.drawable.sweden
            }
            "Switzerland" -> {
                return R.drawable.switzerland
            }
        }
        return R.drawable.ic_fast
    }
}