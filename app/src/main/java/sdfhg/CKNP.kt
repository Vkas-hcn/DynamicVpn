package sdfhg

import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dy.na.mic.BuildConfig
import dy.na.mic.R
import dy.na.mic.ad.AdDynamicUtils
import dy.na.mic.base.BaseActivity
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityGuideDynamicBinding
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils
import kotlinx.coroutines.*
import sdfhf.CKNO

class CKNP : BaseActivity<ActivityGuideDynamicBinding, CKOA>() {
    override val model by viewModels<CKOA>()
    override val implLayoutResId: Int
        get() = R.layout.activity_guide_dynamic
    var proGuideProgress = 0
    private var jobOpenAdsDynamic: Job? = null
    private var startCateDynamic: Job? = null
    override fun initialize() {
        model.isCurrentPage = intent.getBooleanExtra(DataUtils.returnDynamicCurrentPage, false)
        getFirebaseDataDynamic()
        DataUtils.isStartDynamic = true
        lifecycleScope.launch(Dispatchers.IO) {
            DynamicUtils.getIpInformation()
        }
    }

    override fun initData() {
        jumpHomePageData()
    }


    fun showOpenAd(data: Any) {
        AdDynamicUtils.showFullScreenOf(
            where = DataUtils.ad_open,
            context = this,
            res = data,
            preload = false,
            onShowCompleted = {
                model.liveStartToMain.postValue(true)
            }
        )
    }

    private fun jumpHomePageData() {
        model.liveStartToMain.observe(this, {
            lifecycleScope.launch(Dispatchers.Main.immediate) {
                delay(300)
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    model.jumpPageModel(this@CKNP)
                }
            }
        })
    }

    private fun getFirebaseDataDynamic() {
        startCateDynamic = lifecycleScope.launch {
            var isCa = false
            if (!BuildConfig.DEBUG) {
                val auth = Firebase.remoteConfig
                auth.fetchAndActivate().addOnSuccessListener {
                    DataUtils.dynamic_ref = auth.getString(DataUtils.online_ref_tag)
                    DataUtils.ad_dynamic_data = auth.getString(DataUtils.online_ad_tag)
                    DataUtils.con_dynamic_data = auth.getString(DataUtils.online_con_tag)
                    isCa = true
                }
            }
            try {
                withTimeout(4000L) {
                    while (true) {
                        if (!isActive) {
                            break
                        }
                        proGuideProgress += 1
                        binding.pbGuide.progress = proGuideProgress
                        if (isCa) {
                            preloadedAdvertisement()
                            cancel()
                            startCateDynamic = null
                        }
                        delay(140)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                cancel()
                startCateDynamic = null
                preloadedAdvertisement()
            }
        }
    }


    private fun preloadedAdvertisement() {
        DynamicTimeUtils.isAppOpenSameDayDynamic()
        if (DynamicTimeUtils.isThresholdReached()) {
            XLog.d("广告达到上线")
            lifecycleScope.launch {
                model.liveStartToMain.postValue(true)
            }
            model.isAdShowType = 3
        } else {
            loadAdvertisement()
        }
    }

    private fun loadAdvertisement() {
        runCatching {
            AdDynamicUtils.loadOf(DataUtils.ad_open)
            rotationOpenAdDyna()
            AdDynamicUtils.loadOf(DataUtils.ad_home)
            AdDynamicUtils.loadOf(DataUtils.ad_connect)
            AdDynamicUtils.loadOf(DataUtils.ad_result)
            AdDynamicUtils.loadOf(DataUtils.ad_back)
        }
    }

    private fun rotationOpenAdDyna() {
        model.isAdShowType = 0
        jobOpenAdsDynamic = lifecycleScope.launch {
            try {
                withTimeout(10000L) {
                    while (true) {
                        proGuideProgress += 1
                        binding.pbGuide.progress = proGuideProgress
                        AdDynamicUtils.resultOf(DataUtils.ad_open)?.let {
                            cancel()
                            jobOpenAdsDynamic = null
                            showOpenAd(it)
                            model.isAdShowType = 1
                            binding.pbGuide.progress = 100
                        }
                        delay(140)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                cancel()
                jobOpenAdsDynamic = null
                model.liveStartToMain.postValue(true)
                model.isAdShowType = 2
                binding.pbGuide.progress = 100
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(300)

            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (model.isAdShowType == 1) {
                    AdDynamicUtils.resultOf(DataUtils.ad_open)?.let {
                        showOpenAd(it)
                    }
                }
                if (model.isAdShowType == 2 || model.isAdShowType == 3) {
                    model.liveStartToMain.postValue(true)
                }
                if (DataUtils.isStartDynamic) {
                    DynamicUtils.putPointDynamic("Lig_bigati")
                    DataUtils.isStartDynamic = false
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}