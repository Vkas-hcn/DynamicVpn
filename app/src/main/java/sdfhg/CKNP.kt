package sdfhg

import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import dy.na.mic.R
import dy.na.mic.base.BaseActivity
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityGuideDynamicBinding
import dy.na.mic.utils.DynamicUtils
import kotlinx.coroutines.*

class CKNP : BaseActivity<ActivityGuideDynamicBinding, CKOA>() {
    override val model by viewModels<CKOA>()
    override val implLayoutResId: Int
        get() = R.layout.activity_guide_dynamic
    var proGuideProgress = 0
    private var jobOpenAdsMeteor: Job? = null
    private var startCateMeteor:Job?=null
    override fun initialize() {
        model.isCurrentPage = intent.getBooleanExtra(DataUtils.returnDynamicCurrentPage, false)
        getFirebaseDataMeteor()
        lifecycleScope.launch(Dispatchers.IO) {
            DynamicUtils.getIpInformation()
        }
//        lifecycleScope.launch(Dispatchers.IO) {
//            TbaDataUpload.getInstance().getDeliverDataMeteor()
//            TbaDataUpload.getInstance().getBlacklistData(this@C)
//            TbaDataUpload.getInstance().getCurrentIp()
//            TbaDataUpload.getInstance().postSessionEvent()
//        }
    }

    override fun initData() {
        lifecycleScope.launch {
            try {
                withTimeout(4000L) {
                    while (isActive) {
                        proGuideProgress += 1
                        binding.pbGuide.progress = proGuideProgress
                        delay(40)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                binding.pbGuide.progress = 100
            }
        }
    }

    private fun rotationDisplayOpeningAdMeteor() {
//        jobOpenAdsMeteor?.cancel()
//        jobOpenAdsMeteor = null
//        jobOpenAdsMeteor = lifecycleScope.launch {
//            try {
//                withTimeout(10000L) {
//                    ButtonClickCounter.isAppOpenSameDayMeteor()
//                    if (isThresholdReached()) {
//                        LogUtils.d(logTagMeteor, "广告达到上线")
//                        binding.mProgress.hide()
//                        if (this@C.lifecycle.currentState == Lifecycle.State.RESUMED) {
//                            model.jumpPageModel(this@C)
//                        }
//                        jobOpenAdsMeteor?.cancel()
//                        jobOpenAdsMeteor = null
//                        return@withTimeout
//                    }
//                    while (isActive) {
//                        val showState = MeteorLoadOpenAd
//                            .judgeConditionsOpenAd(this@C)
//                        if (showState) {
//                            jobOpenAdsMeteor?.cancel()
//                            jobOpenAdsMeteor = null
//                            binding.mProgress.hide()
//                            ButtonClickCounter.isAppOpenSameDayMeteor()
//                            if (!isThresholdReached()) {
//                                MeteorLoadOpenAd
//                                    .displayOpenAdvertisementMeteor(this@C)
//                            }
//                        }
//                        delay(1000L)
//                    }
//                }
//            } catch (e: TimeoutCancellationException) {
//                jobOpenAdsMeteor?.cancel()
//                jobOpenAdsMeteor = null
//                binding.mProgress.hide()
//                model.jumpPageModel(this@CKNP)
//            }
//        }
        model.jumpPageModel(this@CKNP)

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

    override fun onStop() {
        super.onStop()
        jobOpenAdsMeteor?.cancel()
        jobOpenAdsMeteor = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(300)
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
//                if (CKNP.isGuideMeteor) {
//                    P.toBuriedPointMeteor("sky_meteorem")
//                    CKNP.isGuideMeteor = false
//                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }

    private fun getFirebaseDataMeteor() {
        startCateMeteor = lifecycleScope.launch {
            var isCa = false
//            if (!BuildConfig.DEBUG) {
//                val auth = Firebase.remoteConfig
//                auth.fetchAndActivate().addOnSuccessListener {
//                    S.profileMeteorData = auth.getString("Octopus_servlist")
//                    S.profileMeteorDataFast = auth.getString("Octopus_servlist")
//                    S.advertisingMeteorData = auth.getString("Octopus_ad")
//                    S.meteorConfig = auth.getString(Q.meteorConfig)
//                    S.meteorem_glabs = auth.getString(Q.meteorem_glabs)
//
//                    isCa = true
//                }
//            }
            try {
                withTimeout(4000L) {
                    while (true) {
                        if(!isActive){break}
                        if (isCa) {
                            preloadedAdvertisement()
                            cancel()
                            startCateMeteor = null
                        }
                        delay(500)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                cancel()
                startCateMeteor = null
                preloadedAdvertisement()
            }
        }
    }


    private fun preloadedAdvertisement() {
        rotationDisplayOpeningAdMeteor()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}