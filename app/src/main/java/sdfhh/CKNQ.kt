package sdfhh

import androidx.activity.viewModels
import dy.na.mic.R
import dy.na.mic.base.BaseActivity
import dy.na.mic.databinding.ActivityMainBinding
import android.content.Intent
import android.os.RemoteException
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceDataStore
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.preference.OnPreferenceDataStoreChangeListener
import com.github.shadowsocks.utils.Key
import com.github.shadowsocks.utils.StartService
import sdfhf.*
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.elvishew.xlog.XLog
import sdfhl.CKOJ
import sdfhm.CKON
import com.google.gson.reflect.TypeToken
import dy.na.mic.ad.AdBaseUtils
import dy.na.mic.ad.AdDynamicUtils
import sdfhf.CKNO
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import sdfhj.CKNS
import sdfhi.CKNR
import sdfhk.CKNT
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils
import dy.na.mic.utils.SharedFlowBus
import kotlinx.coroutines.*

class CKNQ : BaseActivity<ActivityMainBinding, CKOB>(),
    ShadowsocksConnection.Callback,
    OnPreferenceDataStoreChangeListener, LifecycleObserver, CKOC {

    override val model by viewModels<CKOB>()
    override val implLayoutResId: Int
        get() = R.layout.activity_main
    var state = CKON.State.Idle
    var stateListener: ((CKON.State) -> Unit)? = null

    private var jobNativeAdsDynamic: Job? = null

    // 跳转结果页
    private val connection = ShadowsocksConnection(true)

    // 是否返回刷新服务器
    var whetherRefreshServer = false

    override fun initialize() {
        model.setMainCallback(this)
        model.mainCall?.setTitleDataCall()
//        if (model.isIllegalIp()) {
//            model.whetherTheBulletBoxCannotBeUsed(this)
//            return
//        }

        // 连接服务
        connection.connect(this, this)

        // 注册数据改变监听
        DataStore.publicStore.registerChangeListener(this)

        // 初始化服务数据
        if (DynamicTimeUtils.isStopThread) {
            model.initializeServerData()
        } else {
            val serviceData = DataUtils.current_server_data_dynamic
            val currentServerData: DynamicVpnBean =
                GsonUtils.fromJson(serviceData, object : TypeToken<DynamicVpnBean?>() {}.type)
            setFastInformation(currentServerData)
        }
        model.isShowHomeAd(binding)
        if (model.referData.Lig_eency == 1) {
            judgeVpnScheme()
        }
        model.mainCall?.showVpnGuideDynamicCall()
    }

    override fun initData() {
        model.mainCall?.jumpResultsPageDataCall()
        model.mainCall?.setServiceDataCall()
        model.mainCall?.flowBusFunCall()
        DataUtils.nativeHomeAdRefreshDynamic = true
        DataUtils.isHotHome = true
    }


    private fun flowBusFun() {
        SharedFlowBus.with<String>(DataUtils.timerDynamicData).register(this) {
            binding.tvTiming.text = it
        }

        SharedFlowBus.with<DynamicVpnBean>(DataUtils.notConnectedDynamicReturn).register(this) {
            model.updateSkServer(it, false)
        }

        SharedFlowBus.with<DynamicVpnBean>(DataUtils.connectedDynamicReturn).register(this) {
            model.updateSkServer(it, true)
        }
    }

    fun showHomeAd(it: Any) {
        AdDynamicUtils.showNativeOf(
            where = DataUtils.ad_home,
            nativeRoot = binding.flAd,
            res = it,
            preload = true,
            onShowCompleted = {
                DataUtils.nativeHomeAdRefreshDynamic = false
            }
        )
    }

    fun showConnectAd(it: Any) {
        AdDynamicUtils.showFullScreenOf(
            where = DataUtils.ad_connect,
            context = this,
            res = it,
            preload = true,
            onShowCompleted = {
                connectOrDisconnectDynamic()
            }
        )
    }

    private fun setTitleData() {
        with(binding.inMainTitle) {
            this.tvTitle.text = this@CKNQ.getString(R.string.app_name)
            this.imgBack.setImageResource(R.drawable.ic_title_menu)
            this.imgBack.setOnClickListener {
                model.imgBackClick(binding) {
                    model.mainCall?.changeOfVpnStatusCall(it)
                }
            }
        }
    }

    private fun jumpResultsPageData() {
        model.liveJumpResultsPage.observe(this, {
            lifecycleScope.launch(Dispatchers.Main.immediate) {
                delay(300L)
                if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                    launchActivityForResult(CKNR::class.java, 0x78, it)
                }
            }
        })
    }

    private fun setServiceData() {
        model.liveInitializeServerData.observe(this, {
            setFastInformation(it)
        })
        model.liveUpdateServerData.observe(this, {
            whetherRefreshServer = true
            connect.launch(null)
        })
        model.liveNoUpdateServerData.observe(this, {
            whetherRefreshServer = false
            setFastInformation(it)
            connect.launch(null)
        })

    }

    fun linkVpn(v: View) {
        if (binding.vpnStateDynamic != 1 && binding.homeGuideDynamic == false) {
            DynamicUtils.putPointDynamic("Lig_ordid")
            connect.launch(null)
        }
    }

    fun linkServiceGuide(v: View) {
        if (binding.vpnStateDynamic != 1 && binding.homeGuideDynamic == true) {
            DynamicUtils.putPointDynamic("Lig_oarium")
            connect.launch(null)
        }

    }

    fun toServiceListPage(v: View) {
        model.imgSetting(this, binding, changeFu = {
            model.mainCall?.changeOfVpnStatusCall(it)
        }, jumpFu = {
            jumpToServerList(it)
        })
    }

    fun clickMask(v: View) {

    }

    fun clickMain(v: View) {
        binding.dhType = false
    }

    fun clickNav(v: View) {

    }

    fun toNavBack(v: View) {
        binding.dhType = false
    }

    fun toPP(v: View) {
        model.mainCall?.toPPCall()

    }

    fun toUpgrade(v: View) {
        model.mainCall?.toUpgradeCall()

    }

    fun toShare(v: View) {
        model.mainCall?.toShareCall()
    }


    fun jumpToServerList(isHaveData: Boolean) {
        lifecycleScope.launch {
            if (lifecycle.currentState != Lifecycle.State.RESUMED) {
                return@launch
            }
            val bundle = Bundle()
            if (state.name == "Connected") {
                bundle.putBoolean(DataUtils.whetherDynamicConnected, true)
            } else {
                bundle.putBoolean(DataUtils.whetherDynamicConnected, false)
            }
            bundle.putString(DataUtils.currentDynamicService, DataUtils.current_server_data_dynamic)
            launchActivityForResult(CKNS::class.java, 0x77, bundle)
        }
    }

    private fun setFastInformation(elVpnBean: DynamicVpnBean) {
        if (elVpnBean.dynamic_best == true) {
            binding.tvMainName.text = "Faster server"
            binding.imgMainFlag.setImageResource(DynamicUtils.getFlagCountryDynamic("Faster server"))
        } else {
            binding.tvMainName.text =
                elVpnBean.dynamic_country.toString() + "-" + elVpnBean.dynamic_city.toString()
            binding.imgMainFlag.setImageResource(DynamicUtils.getFlagCountryDynamic(elVpnBean.dynamic_country.toString()))
        }

    }

    private val connect = registerForActivityResult(StartService()) {
        binding.homeGuideDynamic = false
        lifecycleScope.launch(Dispatchers.IO) {
            DynamicUtils.getIpInformation()
        }
        if (it) {
            ToastUtils.showLong("no permissions")
        } else {
            if (!DataUtils.isPermissionDynamicData) {
                DynamicUtils.putPointDynamic("Lig_yatory")
                DataUtils.isPermissionDynamicData = true
            }
            if (NetworkUtils.isConnected()) {
                model.checkFastData(this, binding, connectVpnFun = {
                    model.mainCall?.startVpnCall()
                })
            } else {
                ToastUtils.showLong(
                    "No network connection detected. Please check your network.",
                    3002
                )
            }
        }
    }

    private fun startVpn() {
//        if (model.isIllegalIp()) {
//            model.whetherTheBulletBoxCannotBeUsed(this)
//            return
//        }
        model.mainVpnClick(changeFu = {
            model.mainCall?.changeOfVpnStatusCall(1)
        }, connectFun = {
            connectOrDisconnectDynamic()
        }, showConnectAd = {
            showConnectAd(it)
        }, this)
    }

    private fun connectOrDisconnectDynamic() {
        if (model.isClickState == 2) {
            model.startDisConnectVpn()
            if (!CKNO.isBackDataDynamic) {
                model.jumpConnectionResultsPage(false)
            }
            model.mainCall?.changeOfVpnStatusCall(0, true)
        }
        if (model.isClickState == 0) {
            if (!CKNO.isBackDataDynamic) {
                model.jumpConnectionResultsPage(true)
            }
            model.mainCall?.changeOfVpnStatusCall(2)
        }
    }

    private fun changeState(
        state: CKON.State,
    ) {
        this.state = state
//        if (state.canStop && !model.isPlanA) {
//            DynamicUtils.reloadVpnAd(this)
//            model.isPlanA = true
//        }
        connectionStatusJudgment(state.canStop)
        stateListener?.invoke(state)
    }

    private fun connectionStatusJudgment(state: Boolean) {
        if (model.isClickState == 0 && !state) {
            //vpn连接失败
            XLog.d("vpn连接失败")
            DynamicUtils.putPointDynamic("Lig_sixtic")
            ToastUtils.showLong("connected failed", 3000)
        }
    }

    private fun changeOfVpnStatus(state: Int, isPlan: Boolean = false) {
        lifecycleScope.launch {
            binding.vpnStateDynamic = state
            when (binding.vpnStateDynamic) {
                0 -> {
                    binding.lavVpn.pauseAnimation()
                    binding.tvState.text = "Connect"
                    binding.tvVpnIcState.text = "START"
                    DynamicTimeUtils.endTiming(isPlan)
                }
                1 -> {
                    binding.lavVpn.playAnimation()
                    if (CKNO.isConnectType) {
                        binding.tvState.text = "DisConnecting…"
                    } else {
                        binding.tvState.text = "Connecting…"
                    }
                }
                2 -> {
                    binding.lavVpn.pauseAnimation()
                    binding.tvState.text = "Conneted"
                    binding.tvVpnIcState.text = "STOP"
                    DynamicTimeUtils.startTiming()
                }
            }
        }
    }


    override fun stateChanged(state: CKON.State, profileName: String?, msg: String?) {
        CKNO.isConnectType = state.canStop
        if (CKNO.isConnectType) {
            DynamicUtils.putPointDynamic("Lig_vot")
            XLog.e("连接成功")
        }
        if (CKNO.isConnectType && !model.isPlanA) {
            AdBaseUtils.loadAllAd()
            model.isPlanA = true
        }
        changeState(state)
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        CKNO.isConnectType = CKON.State.values()[service.state].canStop
        if (CKNO.isConnectType) {
            model.mainCall?.changeOfVpnStatusCall(2)
            model.mainCall?.changeOfVpnStatusCall(2)
        } else {
            model.mainCall?.changeOfVpnStatusCall(0)
        }
    }


    override fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String) {
        when (key) {
            Key.serviceMode -> {
                connection.disconnect(this)
                connection.connect(this, this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connection.bandwidthTimeout = 500
    }


    private fun showHomeAdFun() {
        jobNativeAdsDynamic?.cancel()
        jobNativeAdsDynamic = null
        jobNativeAdsDynamic = lifecycleScope.launch {
            while (true) {
                if (!isActive) {
                    break
                }
                AdDynamicUtils.resultOf(DataUtils.ad_home)?.let {
                    cancel()
                    jobNativeAdsDynamic = null
                    showHomeAd(it)
                }
                delay(500L)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        connection.bandwidthTimeout = 0
        model.cancelJob()
        model.viewModelStop(binding, changeFu = {
            model.mainCall?.changeOfVpnStatusCall(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        DataStore.publicStore.unregisterChangeListener(this)
        connection.disconnect(this)
        model.cancelJob()

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(300)
            if (lifecycle.currentState != Lifecycle.State.RESUMED) {
                return@launch
            }
            AdDynamicUtils.loadOf(
                where = DataUtils.ad_home
            )
            DynamicUtils.putPointDynamic("Lig_plint")
            if (DataUtils.nativeHomeAdRefreshDynamic) {
                if (DataUtils.isHotHome && model.referData.Lig_eency == 2) {
                    judgeVpnScheme()
                }
                showHomeAdFun()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x78) {
            DataUtils.isHotHome = false
        }
        if (requestCode == 0x78 && whetherRefreshServer) {
            DataUtils.isHotHome = false
            model.mainCall?.onActivityResultFunCall()
        }
    }

    fun onActivityResultFun() {
        setFastInformation(model.afterDisconnectionServerData)
        val serviceData = GsonUtils.toJson(model.afterDisconnectionServerData)
        DataUtils.current_server_data_dynamic = serviceData
        model.currentServerData = model.afterDisconnectionServerData
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.viewGuideMask.isVisible) {
                binding.homeGuideDynamic = false
                binding.lavViewGu.pauseAnimation()
            } else {
                if (binding.dhType == true) {
                    binding.dhType = false
                    return false
                }
                if (model.isClickState == 2 && binding.vpnStateDynamic == 1) {
                    model.cancelJob()
                    model.viewModelStop(binding, changeFu = {
                        model.mainCall?.changeOfVpnStatusCall(it)
                    })
                    return false
                }
                if (!(!CKNO.isConnectType && binding.vpnStateDynamic == 1)) {
                    finish()
                }
            }
        }
        return true
    }

    fun showVpnGuideDynamic() {
        lifecycleScope.launch {
            delay(400)
            if (!CKNO.isConnectType && model.isPlanA) {
                binding.homeGuideDynamic = true
                binding.lavViewGu.playAnimation()
                DynamicUtils.putPointDynamic("Lig_archa")
            } else {
                binding.homeGuideDynamic = false
            }
        }
    }

    override fun jumpResultsPageDataCall() {
        jumpResultsPageData()
    }

    override fun setServiceDataCall() {
        setServiceData()
    }

    override fun flowBusFunCall() {
        flowBusFun()
    }

    override fun setTitleDataCall() {
        setTitleData()
    }

    override fun toPPCall() {
        val intent = Intent(this, CKNT::class.java)
        startActivity(intent)
    }

    override fun toUpgradeCall() {
        model.openInBrowser(
            this,
            DataUtils.shareDynamicAddress + this.packageName
        )
    }

    override fun toShareCall() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            DataUtils.shareDynamicAddress + this.packageName
        )
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun jumpToServerListCall() {
        jumpToServerList(false)
    }

    override fun setFastInformationCall(dynamicVpnBean: DynamicVpnBean) {
        setFastInformation(dynamicVpnBean)
    }

    override fun startVpnCall() {
        startVpn()
    }

    override fun connectOrDisconnectDynamicCall() {
        connectOrDisconnectDynamic()
    }

    override fun changeOfVpnStatusCall(state: Int, isPlan: Boolean) {
        changeOfVpnStatus(state, isPlan)
    }

    override fun showVpnGuideDynamicCall() {
        showVpnGuideDynamic()
    }

    override fun onActivityResultFunCall() {
        onActivityResultFun()
    }

    /**
     * 判断Vpn方案
     */
    private fun judgeVpnScheme() {
        if (CKNO.isConnectType) {
            return
        }
        if (!DynamicUtils.isValuableUser(DynamicUtils.getLocalLifKeepiData())) {
            //非买量用户直接走BE方案
            model.isPlanA = true
            return
        }
        val data = model.referData.Lig_suav
        if (data == null) {
            XLog.d("判断Vpn方案---默认")
            vpnCScheme(50)
        } else {
            //CKNO
            vpnCScheme(data)
        }
    }

    /**
     * vpn B 方案
     */
    private fun vpnBScheme() {
        DynamicUtils.putPointDynamic("Lig_tonssu")
        lifecycleScope.launch {
            delay(300)
            XLog.d("CKNO.isConnectType=${CKNO.isConnectType}")
            if (!CKNO.isConnectType) {
                connect.launch(null)
            }
        }
    }

    /**
     * vpn CKNO 方案
     * 概率
     */
    private fun vpnCScheme(mProbability: Int) {
        val random = (1..100).shuffled().last()
        when {
            random <= mProbability -> {
                //B
                XLog.d("随机落在B方案")
                model.isPlanA = false
                vpnBScheme() //20，代表20%为B用户；80%为BE用户
            }
            else -> {
                //CKNO
                XLog.d("随机落在BE方案")
                model.isPlanA = true
            }
        }
    }

}