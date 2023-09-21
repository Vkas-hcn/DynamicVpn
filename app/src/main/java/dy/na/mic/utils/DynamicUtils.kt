package dy.na.mic.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ResourceUtils
import com.elvishew.xlog.XLog
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.reflect.TypeToken
import dy.na.mic.BuildConfig
import dy.na.mic.R
import dy.na.mic.bean.DynamicAdBean
import dy.na.mic.bean.DynamicRefBean
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.bean.LigEatateBean
import dy.na.mic.data.DataUtils
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*

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
        return GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_fast.json"),
            object : TypeToken<MutableList<String>>() {}.type
        )
    }


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

    fun getLocalAdData(): DynamicAdBean {
        val listType = object : TypeToken<DynamicAdBean>() {}.type
        return runCatching {
            GsonUtils.fromJson<DynamicAdBean>(
                DataUtils.ad_dynamic_data,
                listType
            )
        }.getOrNull() ?: GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_ad.json"),
            object : TypeToken<DynamicAdBean?>() {}.type
        )
    }

    fun getLocalVpnReferData(): DynamicRefBean {
        val listType = object : TypeToken<DynamicRefBean>() {}.type
        return runCatching {
            GsonUtils.fromJson<DynamicRefBean>(
                DataUtils.dynamic_ref,
                listType
            )
        }.getOrNull() ?: GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_ref.json"),
            object : TypeToken<DynamicRefBean?>() {}.type
        )
    }

    fun getLocalLifKeepiData(): LigEatateBean {
        val listType = object : TypeToken<LigEatateBean>() {}.type
        return runCatching {
            GsonUtils.fromJson<LigEatateBean>(
                DataUtils.con_dynamic_data,
                listType
            )
        }.getOrNull() ?: GsonUtils.fromJson(
            ResourceUtils.readAssets2String("dynamic_con.json"),
            listType
        )
    }
    var referJobDynamic: Job? = null


     fun getReferInformationDynamic(context: Context) {
        referJobDynamic?.cancel()
        referJobDynamic = GlobalScope.launch {
            while (isActive) {
                if (!isActive) {
                    break
                }
                if (DataUtils.install_referrer_dynamic == "") {
                    referrerDynamic(context)
                } else {
                    cancel()
                    referJobDynamic = null
                }
                delay(10000)
            }
        }
    }
    fun referrerDynamic(
        context: Context,
    ) {
        var installReferrerDynaMic = ""
        if (DataUtils.install_referrer_dynamic != "") {
            return
        }
        if (BuildConfig.DEBUG) {
//            installReferrerDynaMic = "gclid"
            installReferrerDynaMic = "fb4a"
            DataUtils.install_referrer_dynamic = installReferrerDynaMic
        }
        try {
            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(p0: Int) {
                    when (p0) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            installReferrerDynaMic =
                                referrerClient.installReferrer.installReferrer ?: ""
//                            DataUtils.install_referrer_dynamic = installReferrerDynaMic
                            XLog.e("TAG", "installReferrer====${installReferrerDynaMic}")
                            putPointDynamic("Lig_etoio")
                            referrerClient.endConnection()
                            return
                        }
                        else -> {
                            referrerClient.endConnection()
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        } catch (e: Exception) {
        }
    }

    fun isFacebookUser(ligEatateBean: LigEatateBean): Boolean {
        val referrer = DataUtils.install_referrer_dynamic
        return (ligEatateBean.Lig_attorney == "1" && referrer.contains("fb4a", true))
                || (ligEatateBean.Lig_attorney == "1" && referrer.contains("facebook", true))
    }

    fun isValuableUser(ligEatateBean: LigEatateBean): Boolean {
        val referrer = DataUtils.install_referrer_dynamic
        return isFacebookUser(ligEatateBean)
                || (ligEatateBean.Lig_senee == "1" && referrer.contains("gclid", true))
                || (ligEatateBean.Lig_meteron == "1" && referrer.contains("not%20set", true))
                || (ligEatateBean.Lig_kindcy == "1" && referrer.contains("youtubeads", true))
                || (ligEatateBean.Lig_armform == "1" && referrer.contains("%7B%22", true))
                || (ligEatateBean.Lig_jacfic == "1" && referrer.contains("adjust", true))
                || (ligEatateBean.Lig_haurion == "1" && referrer.contains("bytedance", true))
    }

    fun isBuyQuantityBanDynamic(): Boolean {
        val localVpnBootData = getLocalVpnReferData()
        val ligEatateBean = getLocalLifKeepiData()
        if (!isToBlockScreenAdsDynamic(localVpnBootData.Lig_anyone ?: 3, ligEatateBean)) {
            return true
        }
        return false
    }

    fun isToBlockScreenAdsDynamic(onlineRef: Int, ligEatateBean: LigEatateBean): Boolean {
        when (onlineRef) {
            1 -> {
                return true
            }
            2 -> {
                return isValuableUser(ligEatateBean)
            }
            3 -> {
                return isFacebookUser(ligEatateBean)
            }
            4 -> {
                return false
            }
            else -> {
                return true
            }
        }
    }

    fun putPointDynamic(name: String) {
        if (BuildConfig.DEBUG) {
            XLog.d("触发埋点----name=${name}")
        } else {
            Firebase.analytics.logEvent(name, null)
        }
    }


    fun putPointTimeDynamic(name: String, time: Int) {
        if (BuildConfig.DEBUG) {
            XLog.d("触发埋点----name=${name}---time=${time}")
        } else {
            Firebase.analytics.logEvent(name, bundleOf("time" to time))
        }
    }

    fun putPointAdValueDynamic(adValue: Long, context: Context) {
        AppEventsLogger.newLogger(context).logPurchase(
            (adValue / 1000000.0).toBigDecimal(), Currency.getInstance("USD")
        )
    }

    fun getFlagCountryDynamic(img: String): Int {
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
                return R.drawable.ic_unitedstates
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