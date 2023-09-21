package dy.na.mic.ad

import android.content.Context
import com.elvishew.xlog.XLog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import dy.na.mic.bean.DynamicAdBean
import dy.na.mic.bean.DynamicAdDetailBean
import dy.na.mic.data.DataUtils
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils

object AdBaseUtils {
    fun loadOpenAd(
        context: Context,
        unit: DynamicAdDetailBean, where: String,
        callback: ((result: Any?) -> Unit)
    ) {
        AppOpenAd.load(
            context.applicationContext,
            unit.Lig_thful,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object :
                AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    XLog.d("${where},request fail: ${loadAdError.message}")
                    callback(null)
                }

                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    appOpenAd.setOnPaidEventListener {
                        DynamicUtils.putPointAdValueDynamic(it.valueMicros, context)
                    }
                    callback(appOpenAd)
                }
            })
    }

    fun loadInterstitialAd(
        context: Context,
        unit: DynamicAdDetailBean, where: String,
        callback: ((result: Any?) -> Unit)
    ) {
        InterstitialAd.load(
            context.applicationContext,
            unit.Lig_thful,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    XLog.d("${where},request fail: ${loadAdError.message}")
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.setOnPaidEventListener {
                        DynamicUtils.putPointAdValueDynamic(it.valueMicros, context)
                    }
                    callback(interstitialAd)
                }
            }
        )
    }

    fun loadNavAd(
        context: Context,
        unit: DynamicAdDetailBean, where: String,
        callback: ((result: Any?) -> Unit)
    ) {
        AdLoader.Builder(context.applicationContext, unit.Lig_thful)
            .forNativeAd {
                it.setOnPaidEventListener { adValue ->
                    AdDynamicUtils.loadOf(where)
                    DynamicUtils.putPointAdValueDynamic(adValue.valueMicros, context)
                }
                callback(it)
            }
            .withAdListener(object : AdListener() {
                override fun onAdOpened() {
                    super.onAdOpened()
                    XLog.e("${where}-原生广告点击")

                    DynamicTimeUtils.recordNumOfAdClickDyna()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    XLog.d("${where},request fail: ${loadAdError.message}")
                    callback(null)
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()

                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        when (where) {
                            DataUtils.ad_home -> NativeAdOptions.ADCHOICES_TOP_RIGHT
                            DataUtils.ad_result -> NativeAdOptions.ADCHOICES_TOP_LEFT
                            else -> NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                        }
                    )
                    .build()
            )
            .build()
            .loadAd(AdRequest.Builder().build())
    }

    fun doRequestFun(
        where: String,
        context: Context,
        inst: DynamicAdBean = DynamicUtils.getLocalAdData(),
        isSuccessfulFun: (it:Any?) -> Unit
    ) {
        doRequest(where,
            context, when (where) {
                DataUtils.ad_open -> inst.Lig_anth
                DataUtils.ad_connect -> inst.Lig_plum
                DataUtils.ad_home -> inst.Lig_fari
                DataUtils.ad_result -> inst.Lig_fusia
                DataUtils.ad_back -> inst.Lig_pulch
                else -> emptyList()
            }.sortedByDescending { it.Lig_ofag }
        ) {
            isSuccessfulFun(it)
        }
    }

    private fun doRequest(
        where: String,
        context: Context,
        units: List<DynamicAdDetailBean>,
        startIndex: Int = 0,
        callback: ((result: Any?) -> Unit)
    ) {
        val unit = units.getOrNull(startIndex)
        if (unit == null) {
            callback(null)
            return
        }
        XLog.d("${where},on request: $unit")
        AdDynamicUtils.GoogleAds(where).load(context, unit) {
            if (it == null)
                doRequest(where, context, units, startIndex + 1, callback)
            else
                callback(it)
        }
    }

    fun loadAllAd() {
        runCatching {
            XLog.e("重新加载广告")
            AdDynamicUtils.loadOf(DataUtils.ad_connect)

            AdDynamicUtils.loadOf(DataUtils.ad_result)

            AdDynamicUtils.loadOf(DataUtils.ad_home)

            AdDynamicUtils.loadOf(DataUtils.ad_back)
        }
    }

    fun disConnectLoadAllAd() {
        runCatching {
            XLog.e("断开检查广告缓存")
            AdDynamicUtils.loadOf(DataUtils.ad_home)

            AdDynamicUtils.loadOf(DataUtils.ad_connect)

            AdDynamicUtils.loadOf(DataUtils.ad_result)

            AdDynamicUtils.loadOf(DataUtils.ad_back)
        }
    }
}