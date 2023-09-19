package sdfhf

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ProcessUtils
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import sdfhl.CKOJ
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dy.na.mic.BuildConfig
import dy.na.mic.utils.DynamicTimeUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
//import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
//
//import com.elvishew.xlog.LogConfiguration
//import com.elvishew.xlog.LogLevel
import sdfhh.CKNQ
import dy.na.mic.data.DataUtils
import sdfhg.CKNP


class CKNO : Application(), LifecycleObserver {
    private var flag = 0
    private var job_og: Job? = null
    private var top_activity_og: Activity? = null

    companion object {
        lateinit var context: Context
        var warmBoot = false

        var isBackDataDynamic = false

        var whetherBackgroundDynamic = false

        var nativeAdRefreshDynamic = false

        var isConnectType = false
        var isGuideDynamic: Boolean = true
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        setActivityLifecycleDynamic(this)
        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        if (ProcessUtils.isMainProcess()) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            Firebase.initialize(this)
            FirebaseApp.initializeApp(this)
//            P.getReferInformation(this)
//            val data = S.uuidValueDynamic
//            if (data.isEmpty()) {
//                S.uuidValueDynamic = UUID.randomUUID().toString()
//            }
            logSetting()
        }
        CKOJ.init(this, CKNQ::class)
        DynamicTimeUtils.sendTimerInformation()
        DynamicTimeUtils.isAppOpenSameDayDynamic()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        nativeAdRefreshDynamic = true
        job_og?.cancel()
        job_og = null
        if (whetherBackgroundDynamic && !isBackDataDynamic) {
            jumpGuidePageDynamic()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopState() {
        job_og = GlobalScope.launch {
            whetherBackgroundDynamic = false
            delay(3000L)
            whetherBackgroundDynamic = true
            ActivityUtils.finishActivity(AdActivity::class.java)
            ActivityUtils.finishActivity(CKNP::class.java)
        }
    }


    private fun jumpGuidePageDynamic() {
        whetherBackgroundDynamic = false
        warmBoot = true
        val intent = Intent(top_activity_og, CKNP::class.java)
        intent.putExtra(DataUtils.returnDynamicCurrentPage, true)
        top_activity_og?.startActivity(intent)
    }

    fun setActivityLifecycleDynamic(application: Application) {
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity !is AdActivity) {
                    top_activity_og = activity
                }
            }

            override fun onActivityStarted(activity: Activity) {

                if (activity !is AdActivity) {
                    top_activity_og = activity
                }
                flag++
                isBackDataDynamic = false
            }

            override fun onActivityResumed(activity: Activity) {
                if (activity !is AdActivity) {
                    top_activity_og = activity
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (activity !is AdActivity) {
                    top_activity_og = activity
                }
            }

            override fun onActivityStopped(activity: Activity) {
                flag--
                if (flag == 0) {
                    isBackDataDynamic = true
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                top_activity_og = null
            }
        })
    }

    private fun logSetting() {
        val config = LogConfiguration.Builder()
            .logLevel(
                if (BuildConfig.DEBUG) LogLevel.ALL // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                else LogLevel.NONE
            )
            .tag("logTagDynamic") // 指定 TAG，默认为 "X-LOG"
            .enableThreadInfo() // 允许打印线程信息，默认禁止
            .enableStackTrace(2) // 允许打印深度为 2 的调用栈信息，默认禁止
            .enableBorder() // 允许打印日志边框，默认禁止
            .build()
        XLog.init(config)
    }
}