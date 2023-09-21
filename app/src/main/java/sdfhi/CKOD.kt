package sdfhi

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dy.na.mic.ad.AdDynamicUtils
import dy.na.mic.base.BaseViewModel
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityResultDynamicBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CKOD:BaseViewModel() {
    private var jobResultDynamic: Job? = null

     fun initResultAd(activity: AppCompatActivity,binding: ActivityResultDynamicBinding) {
        jobResultDynamic = activity.lifecycleScope.launch {
            while (true) {
                AdDynamicUtils.resultOf(DataUtils.ad_result)?.let {
                    cancel()
                    jobResultDynamic = null
                    showResultAdFun(it,binding)
                }
                delay(500L)
            }
        }
    }

     private fun showResultAdFun(it: Any, binding:ActivityResultDynamicBinding) {
        AdDynamicUtils.showNativeOf(
            where = DataUtils.ad_result,
            nativeRoot = binding.flAd,
            res = it,
            preload = true,
            onShowCompleted = {
                DataUtils.nativeResultAdRefreshDynamic = false
            }
        )
    }

     fun showBackAdFun(it: Any,activity: AppCompatActivity) {
        AdDynamicUtils.showFullScreenOf(
            where = DataUtils.ad_back,
            context = activity,
            res = it,
            onShowCompleted = {
                activity.finish()
            }
        )
    }
}