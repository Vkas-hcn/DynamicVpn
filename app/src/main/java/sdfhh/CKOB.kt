package sdfhh

import android.annotation.SuppressLint
import dy.na.mic.base.BaseViewModel
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager
import com.github.shadowsocks.preference.DataStore
import com.google.gson.reflect.TypeToken
import dy.na.mic.bean.DynamicIp2Bean
import dy.na.mic.bean.DynamicIpBean
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import dy.na.mic.utils.SharedFlowBus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import dy.na.mic.R
import dy.na.mic.utils.DynamicUtils
import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.*
import sdfhl.CKOJ
import com.github.shadowsocks.aidl.ShadowsocksConnection
import dy.na.mic.ad.AdBaseUtils
import dy.na.mic.ad.AdDynamicUtils
import dy.na.mic.bean.DynamicRefBean
import sdfhm.CKON
import sdfhf.CKNO
import dy.na.mic.databinding.ActivityMainBinding
import sdfhj.CKNS
import dy.na.mic.utils.DynamicTimeUtils
import kotlinx.coroutines.*
import kotlin.system.exitProcess

class CKOB : BaseViewModel() {
    var mainCall: CKOC? = null
    fun setMainCallback(mainCall: CKOC) {
        this.mainCall = mainCall
    }

    val referData: DynamicRefBean = DynamicUtils.getLocalVpnReferData()
    var isPlanA = true
    var isClickState: Int = 1
    private var jobStartDynamic: Job? = null

    //初始化服务器数据
    val liveInitializeServerData: MutableLiveData<DynamicVpnBean> by lazy {
        MutableLiveData<DynamicVpnBean>()
    }

    //更新服务器数据(未连接)
    val liveNoUpdateServerData: MutableLiveData<DynamicVpnBean> by lazy {
        MutableLiveData<DynamicVpnBean>()
    }

    //更新服务器数据(已连接)
    val liveUpdateServerData: MutableLiveData<DynamicVpnBean> by lazy {
        MutableLiveData<DynamicVpnBean>()
    }

    //当前服务器
    var currentServerData: DynamicVpnBean = DynamicVpnBean()

    //断开后选中服务器
    var afterDisconnectionServerData: DynamicVpnBean = DynamicVpnBean()

    //跳转结果页
    val liveJumpResultsPage: MutableLiveData<Bundle> by lazy {
        MutableLiveData<Bundle>()
    }

    fun initializeServerData() {
        val bestData = DynamicUtils.getFastIpDynamic()
        ProfileManager.getProfile(DataStore.profileId).let {
            if (it != null) {
                ProfileManager.updateProfile(setSkServerData(it, bestData))
            } else {
                val profile = Profile()
                ProfileManager.createProfile(setSkServerData(profile, bestData))
            }
        }
        DataStore.profileId = 1L
        currentServerData = bestData
        val serviceData = GsonUtils.toJson(currentServerData)
        DataUtils.current_server_data_dynamic = serviceData
        liveInitializeServerData.postValue(bestData)
    }

    fun updateSkServer(skServiceBean: DynamicVpnBean, isConnect: Boolean) {
        ProfileManager.getProfile(DataStore.profileId).let {
            if (it != null) {
                setSkServerData(it, skServiceBean)
                ProfileManager.updateProfile(it)
            } else {
                ProfileManager.createProfile(Profile())
            }
        }
        DataStore.profileId = 1L
        if (isConnect) {
            afterDisconnectionServerData = skServiceBean
            liveUpdateServerData.postValue(skServiceBean)
        } else {
            currentServerData = skServiceBean
            val serviceData = GsonUtils.toJson(currentServerData)
            DataUtils.current_server_data_dynamic = serviceData
            liveNoUpdateServerData.postValue(skServiceBean)
        }
    }

    fun isShowHomeAd(binding: ActivityMainBinding) {
        val data = DynamicUtils.isBuyQuantityBanDynamic()
        if (data) {
            binding.flAd.visibility = View.GONE
        } else {
            binding.flAd.visibility = View.VISIBLE
        }
    }

    private fun setSkServerData(profile: Profile, bestData: DynamicVpnBean): Profile {
        profile.name = bestData.dynamic_country + "-" + bestData.dynamic_city
        profile.host = bestData.dynamic_ip.toString()
        profile.password = bestData.dynamic_password ?: ""
        profile.method = bestData.dynamic_protocol ?: ""
        profile.remotePort = bestData.dynamic_port ?: 0
        return profile
    }

    /**
     * 跳转连接结果页
     */
    fun jumpConnectionResultsPage(isConnection: Boolean) {
        val bundle = Bundle()
        val serviceData = DataUtils.current_server_data_dynamic
        bundle.putBoolean(DataUtils.connectionDynamicStatus, isConnection)
        bundle.putString(DataUtils.serverDynamicInformation, serviceData)
        liveJumpResultsPage.postValue(bundle)
    }

    /**
     * 判断是否是非法IP；中国大陆IP、伊朗IP
     */
    fun isIllegalIp(): Boolean {
        val ipData = DataUtils.IP_INFORMATION_DYNAMIC
        val locale = Locale.getDefault()
        val language = locale.language
        if (ipData.isNullOrEmpty()) {
            return isIllegalIp2()
        }
        var greenIpBean = DynamicIpBean("", "", "", "")
        runCatching {
            greenIpBean = GsonUtils.fromJson(
                ipData,
                object : TypeToken<DynamicIpBean?>() {}.type
            )
        }
        return greenIpBean.country_code == "IR" || greenIpBean.country_code == "CN" ||
                greenIpBean.country_code == "HK" || greenIpBean.country_code == "MO"
    }

    private fun isIllegalIp2(): Boolean {
        val ipData = DataUtils.IP_INFORMATION_DYNAMIC2
        val locale = Locale.getDefault()
        val language = locale.language
        if (ipData.isNullOrEmpty()) {
            return language == "zh" || language == "fa"
        }
        var greenIp2Bean = DynamicIp2Bean("", "", "")
        runCatching {
            greenIp2Bean = GsonUtils.fromJson(ipData, object : TypeToken<DynamicIp2Bean?>() {}.type)
        }
        return greenIp2Bean.cc == "IR" || greenIp2Bean.cc == "CN" ||
                greenIp2Bean.cc == "HK" || greenIp2Bean.cc == "MO"
    }


    /**
     * 是否显示不能使用弹框
     */
    fun whetherTheBulletBoxCannotBeUsed(context: AppCompatActivity) {
        val dialogVpn: AlertDialog = AlertDialog.Builder(context)
            .setTitle("Reminder")
            .setMessage("Due to policy restrictions, this service is not available in your current region.")
            .setCancelable(false)
            .setPositiveButton("confirm") { dialog, _ ->
                dialog.dismiss()
                ActivityUtils.finishAllActivities()
                exitProcess(0)
            }.create()
        dialogVpn.setCancelable(false)
        dialogVpn.show()
        dialogVpn.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
        dialogVpn.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
    }

    fun openInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            ToastUtils.showShort("Unable to open link")
        }
    }

    fun startConnectVpn() {
        if (!CKNO.isConnectType) {
            CKOJ.startService()
            XLog.e("开始连接")
            DynamicUtils.putPointDynamic("Lig_pattern")
        }
    }

    fun startDisConnectVpn() {
        if (CKNO.isConnectType) {
            CKOJ.stopService()
            XLog.e("开始断开")
            DynamicUtils.putPointDynamic("Lig_teache")
        }
    }

    fun viewModelStop(binding: ActivityMainBinding, changeFu: (state: Int) -> Unit) {
        if (isClickState == 0 && binding.vpnStateDynamic == 1) {
            DynamicUtils.putPointDynamic("Lig_itive")
        }
        if (isClickState == 2 && binding.vpnStateDynamic == 1) {
            DynamicUtils.putPointDynamic("Lig_muniu")
        }
        if (CKNO.isConnectType) {
            changeFu(2)
        } else {
            changeFu(0)
        }
    }

    fun mainVpnClick(
        changeFu: (state: Int) -> Unit,
        connectFun: () -> Unit,
        showConnectAd: (it: Any) -> Unit,
        activity: AppCompatActivity
    ) {
        changeFu(1)
        isClickState = if (CKNO.isConnectType) {
            2
        } else {
            0
        }
        jobStartDynamic?.cancel()
        jobStartDynamic = null
        jobStartDynamic = activity.lifecycleScope.launch {
            if (CKNO.isConnectType) {
                AdBaseUtils.disConnectLoadAllAd()
            }
            if (isPlanA && !CKNO.isConnectType) {
                AdDynamicUtils.loadOf(DataUtils.ad_connect)
                AdDynamicUtils.loadOf(DataUtils.ad_result)
                AdDynamicUtils.loadOf(DataUtils.ad_back)
            }
            delay(2000L)
            if (!isPlanA && !CKNO.isConnectType) {
                AdDynamicUtils.clearAllAds()
            }
            startConnectVpn()
            try {
                withTimeout(10000L) {
                    delay(300L)
                    while (true) {
                        if (!isActive) {
                            break
                        }
                        if (DynamicUtils.isBuyQuantityBanDynamic()) {
                            cancel()
                            connectFun()
                        }
                        AdDynamicUtils.resultOf(DataUtils.ad_connect)?.let {
                            cancel()
                            jobStartDynamic = null
                            showConnectAd(it)
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                XLog.d("connect---插屏超时")
                connectFun()
            }

        }
    }

    fun cancelJob() {
        jobStartDynamic?.cancel()
        jobStartDynamic = null
    }

    fun imgBackClick(binding: ActivityMainBinding, changeFu: (state: Int) -> Unit) {
        if (isClickState == 2 && binding.vpnStateDynamic == 1) {
            cancelJob()
            changeFu(2)
        }
        if (binding.vpnStateDynamic != 1 && !binding.viewGuideMask.isVisible) {
            binding.dhType = true
        }
    }

    fun imgSetting(
        activity: AppCompatActivity,
        binding: ActivityMainBinding,
        changeFu: (state: Int) -> Unit,
        jumpFu: (isHaveData: Boolean) -> Unit
    ) {
        if (isClickState == 2 && binding.vpnStateDynamic == 1) {
            cancelJob()
            changeFu(2)
        }
        if (binding.vpnStateDynamic != 1 && !binding.viewGuideMask.isVisible) {
            jumpFu(false)
        }
    }

    fun checkServerData(
        activity: AppCompatActivity,
        binding: ActivityMainBinding,
        startTheJudgmentFun: (isHaveData: Boolean) -> Unit
    ) {
        activity.lifecycleScope.launch {
            startTheJudgmentFun(true)
        }
    }

    fun checkFastData(
        activity: AppCompatActivity,
        binding: ActivityMainBinding,
        connectVpnFun: () -> Unit
    ) {
        activity.lifecycleScope.launch {
            connectVpnFun()
        }
    }


}