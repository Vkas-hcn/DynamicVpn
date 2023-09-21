package dy.na.mic.ad

import android.view.View
import androidx.appcompat.app.AppCompatActivity


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.ActivityUtils
import com.elvishew.xlog.XLog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dy.na.mic.bean.DynamicAdBean
import dy.na.mic.bean.DynamicAdDetailBean
import dy.na.mic.data.DataUtils
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils
import sdfhf.CKNO
import dy.na.mic.R

object AdDynamicUtils {
    fun loadOf(where: String) {
        Load.of(where)?.load()
    }

    fun resultOf(where: String): Any? {
        return Load.of(where)?.res
    }

    fun showFullScreenOf(
        where: String,
        context: AppCompatActivity,
        res: Any,
        preload: Boolean = false,
        onShowCompleted: () -> Unit
    ) {
        Show.of(where)
            .showFullScreen(
                context = context,
                res = res,
                callback = {
                    Load.of(where)?.let { load ->
                        load.clearCache()
                        if (preload) {
                            load.load()
                        }
                    }
                    onShowCompleted()
                }
            )
    }

    fun showNativeOf(
        where: String,
        nativeRoot: ViewGroup,
        res: Any,
        preload: Boolean = false,
        onShowCompleted: (() -> Unit)? = null
    ) {
        Show.of(where)
            .showNativeOf(
                nativeRoot = nativeRoot,
                res = res,
                callback = {
                    Load.of(where)?.let { load ->
                        load.clearCache()
                        if (preload) {
                            load.load()
                        }
                    }
                    onShowCompleted?.invoke()
                }
            )
    }

    fun clearAllAds() {
        XLog.e("清除广告")
        Load.of(DataUtils.ad_home)?.res = null
        Load.of(DataUtils.ad_home)?.isLoading = false
        Load.of(DataUtils.ad_connect)?.res = null
        Load.of(DataUtils.ad_connect)?.isLoading = false
        Load.of(DataUtils.ad_result)?.res = null
        Load.of(DataUtils.ad_result)?.isLoading = false
        Load.of(DataUtils.ad_back)?.res = null
        Load.of(DataUtils.ad_back)?.isLoading = false
    }

    private class Load private constructor(private val where: String) {
        companion object {
            private val open by lazy { Load(DataUtils.ad_open) }
            private val home by lazy { Load(DataUtils.ad_home) }
            private val connect by lazy { Load(DataUtils.ad_connect) }
            private val back by lazy { Load(DataUtils.ad_back) }
            private val result by lazy { Load(DataUtils.ad_result) }

            fun of(where: String): Load? {
                return when (where) {
                    DataUtils.ad_open -> open
                    DataUtils.ad_home -> home
                    DataUtils.ad_connect -> connect
                    DataUtils.ad_back -> back
                    DataUtils.ad_result -> result
                    else -> null
                }
            }
        }

        private var createdTime = 0L
        var res: Any? = null
            set
        var isLoading = false
            set


        fun load(
            context: Context = CKNO.context,
            requestCount: Int = 1,
            inst: DynamicAdBean = DynamicUtils.getLocalAdData(),
        ) {
            DynamicTimeUtils.isAppOpenSameDayDynamic()

            if (isLoading) {
                XLog.d("${where},正在请求中")
                return
            }

            val cache = res
            val cacheTime = createdTime
            if (cache != null && cache != "") {
                if (cacheTime > 0L
                    && ((System.currentTimeMillis() - cacheTime) > (1000L * 60L * 60L))
                ) {
                    XLog.d("${where},缓存已过期")
                    clearCache()
                } else {
                    XLog.d("${where},现有缓存")
                    return
                }
            }
            if ((cache == null || cache == "") && DynamicTimeUtils.isThresholdReached()) {
                XLog.d("广告达到上线")
                res = ""
                return
            }
            if ((where == DataUtils.ad_back || where == DataUtils.ad_connect || where == DataUtils.ad_home) && DynamicUtils.isBuyQuantityBanDynamic()) {
                XLog.e("买量屏蔽用户不加载${where}广告")
                return
            }

            isLoading = true
            XLog.d("${where},加载启动")
            AdBaseUtils.doRequestFun(where, context, inst, isSuccessfulFun = {
                val isSuccessful = it != null
                XLog.d("${where},加载完成, 结果=$isSuccessful")
                if (isSuccessful) {
                    res = it
                    createdTime = System.currentTimeMillis()
                }
                isLoading = false
                if (!isSuccessful && where == DataUtils.ad_open && requestCount < 2) {
                    load(context, requestCount + 1, inst)
                }
            })
        }

        fun clearCache() {
            res = null
            createdTime = 0L
        }
    }

    private class Show private constructor(private val where: String) {
        companion object {
            private var isShowingFullScreen = false

            fun of(where: String): Show {
                return Show(where)
            }

            fun finishAdActivity() {
                GoogleAds.finishAdActivity()
            }
        }

        fun showFullScreen(
            context: AppCompatActivity,
            res: Any,
            callback: () -> Unit
        ) {
            if (isShowingFullScreen || context.lifecycle.currentState != Lifecycle.State.RESUMED) {
//                callback()
                return
            }
            isShowingFullScreen = true
            GoogleAds(where)
                .showFullScreen(
                    context = context,
                    res = res,
                    callback = {
                        isShowingFullScreen = false
                        callback()
                    }
                )
        }

        fun showNativeOf(
            nativeRoot: ViewGroup,
            res: Any,
            callback: () -> Unit
        ) {
            GoogleAds(where)
                .showNativeOf(
                    nativeRoot = nativeRoot,
                    res = res,
                    callback = callback
                )
        }
    }

    class GoogleAds(private val where: String) {
        private class GoogleFullScreenCallback(
            private val where: String,
            private val callback: () -> Unit
        ) : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                XLog.d("${where}--dismissed")
                onAdComplete()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                XLog.d("${where}--fail to show, message=${p0.message}]")
                onAdComplete()
            }

            private fun onAdComplete() {
                callback()
            }

            override fun onAdShowedFullScreenContent() {
                DynamicTimeUtils.recordNumOfAdShowDyna()
                XLog.d("${where}--showed")

            }

            override fun onAdClicked() {
                super.onAdClicked()
                XLog.d("${where}插屏广告点击")
                DynamicTimeUtils.recordNumOfAdClickDyna()
            }
        }

        companion object {
            fun init(context: Context, onInitialized: () -> Unit) {
                MobileAds.initialize(context) {
                    onInitialized()
                }
            }

            fun finishAdActivity() {
                ActivityUtils.finishActivity(AdActivity::class.java)
            }
        }

        fun load(
            context: Context,
            unit: DynamicAdDetailBean,
            callback: ((result: Any?) -> Unit)
        ) {
            when (unit.Lig_hster) {
                DataUtils.open -> {
                    AdBaseUtils.loadOpenAd(context, unit, where, callback)
                }
                DataUtils.ins -> {
                    if (where == DataUtils.ad_connect && CKNO.isConnectType) {
                        DynamicUtils.putPointDynamic("Lig_esque")
                    }
                    AdBaseUtils.loadInterstitialAd(context, unit, where, callback)
                }
                DataUtils.nav -> {
                    if (where == DataUtils.ad_result && CKNO.isConnectType) {
                        DynamicUtils.putPointDynamic("Lig_riskst")
                    }
                    AdBaseUtils.loadNavAd(context, unit, where, callback)
                }
                else -> {
                    callback(null)
                }
            }
        }

        fun showFullScreen(
            context: AppCompatActivity,
            res: Any,
            callback: () -> Unit
        ) {
            when (res) {
                is AppOpenAd -> {
                    res.fullScreenContentCallback = GoogleFullScreenCallback(where, callback)
                    res.show(context)
                }
                is InterstitialAd -> {
                    if (where == DataUtils.ad_back || where == DataUtils.ad_connect) {
                        if (DynamicUtils.isBuyQuantityBanDynamic()) {
                            XLog.e("根据买量屏蔽插屏广告。。。")
                            callback.invoke()
                            return
                        }
                    }
                    res.fullScreenContentCallback = GoogleFullScreenCallback(where, callback)
                    res.show(context)

                }
                else -> callback()
            }
        }

        fun showNativeOf(
            nativeRoot: ViewGroup,
            res: Any,
            callback: () -> Unit
        ) {
            val flAd = nativeRoot.findViewById<View>(R.id.fl_ad)
            if (where == DataUtils.ad_home && DynamicUtils.isBuyQuantityBanDynamic()) {
                XLog.d("根据买量屏蔽home广告。。。")
                flAd.visibility = View.GONE
                return
            }
            val imgOc = nativeRoot.findViewById<View>(R.id.img_dynamic_ad_frame)
            val nativeAd = res as? NativeAd ?: return
            imgOc?.visibility = View.GONE
            val ogAdFrame = nativeRoot.findViewById<ViewGroup>(R.id.dynamic_ad_frame)
            ogAdFrame?.visibility = View.VISIBLE
            val adView = if (where == DataUtils.ad_home) {
                LayoutInflater.from(nativeRoot.context)
                    .inflate(
                        R.layout.layout_home_ad_dynamic,
                        nativeRoot.findViewById(R.id.dynamic_ad_frame),
                        false
                    ) as NativeAdView
            } else {
                LayoutInflater.from(nativeRoot.context)
                    .inflate(
                        R.layout.layout_result_ad_dynamic,
                        nativeRoot.findViewById(R.id.dynamic_ad_frame),
                        false
                    ) as NativeAdView
            }
            ogAdFrame.apply {
                removeAllViews()
                addView(adView)
            }
            adView.findViewById<MediaView>(R.id.ad_media)?.let { mediaView ->
                adView.mediaView = mediaView
                nativeAd.mediaContent?.let { mediaContent ->
                    mediaView.setMediaContent(mediaContent)
                    mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                }
            }

            adView.findViewById<TextView>(R.id.ad_headline)?.let { titleView ->
                adView.headlineView = titleView
                titleView.text = nativeAd.headline
            }

            adView.findViewById<TextView>(R.id.ad_body)?.let { bodyView ->
                adView.bodyView = bodyView
                bodyView.text = nativeAd.body
            }

            adView.findViewById<TextView>(R.id.ad_call_to_action)?.let { actionView ->
                adView.callToActionView = actionView
                actionView.text = nativeAd.callToAction
            }

            adView.findViewById<ImageView>(R.id.ad_app_icon)?.let { iconView ->
                adView.iconView = iconView
                iconView.setImageDrawable(nativeAd.icon?.drawable)
            }

            adView.setNativeAd(nativeAd)
            DynamicTimeUtils.recordNumOfAdShowDyna()
            XLog.d("${where}--showed")
            callback()
        }
    }
}