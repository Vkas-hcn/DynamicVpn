package sdfhi

import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.GsonUtils
import com.elvishew.xlog.XLog
import com.google.gson.reflect.TypeToken
import dy.na.mic.R
import dy.na.mic.ad.AdDynamicUtils
import dy.na.mic.base.BaseActivity
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityResultDynamicBinding
import dy.na.mic.utils.DynamicUtils
import kotlinx.coroutines.*
import sdfhf.CKNO

class CKNR : BaseActivity<ActivityResultDynamicBinding, CKOD>(), CKOE {
    private var isConnectionDynamic: Boolean = false
    private var resultAdJobDynamic: Job? = null
    var resultCall: CKOE? = null
    fun setResultCallBack(resultCall: CKOE) {
        this.resultCall = resultCall
    }
    //当前服务器
    private lateinit var currentServerBeanDynamic: DynamicVpnBean
    override val implLayoutResId: Int
        get() = R.layout.activity_result_dynamic
    override val model: CKOD
        get() = CKOD()

    override fun initialize() {
        setResultCallBack(this)
        initializeCall()
    }

    override fun initData() {
        resultCall?.initDataCall()
        DataUtils.nativeResultAdRefreshDynamic = true
    }

    private fun returnToHomePage() {
        DynamicUtils.putPointDynamic("Lig_viron")
        AdDynamicUtils.resultOf(DataUtils.ad_back).run {
            if (this == null) {
                finish()
            } else {
                model.showBackAdFun(this,this@CKNR)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AdDynamicUtils.loadOf(
            where = DataUtils.ad_result
        )
        resultAdJobDynamic = lifecycleScope.launch(Dispatchers.Main) {
            delay(200)
            if (lifecycle.currentState != Lifecycle.State.RESUMED) {
                return@launch
            }
            DynamicUtils.putPointDynamic("Lig_hette")
            if (DataUtils.nativeResultAdRefreshDynamic) {
                model.initResultAd(this@CKNR,binding)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnToHomePage()
        }
        return true
    }

    override fun initializeCall() {
        val bundle = intent.extras
        isConnectionDynamic = bundle?.getBoolean(DataUtils.connectionDynamicStatus) == true
        currentServerBeanDynamic = runCatching {
            GsonUtils.fromJson<DynamicVpnBean>(
                DataUtils.current_server_data_dynamic,
                object : TypeToken<DynamicVpnBean?>() {}.type
            )
        }.getOrDefault(DynamicVpnBean())
        binding.inResultTitle.imgBack.setOnClickListener {
            returnToHomePage()
        }
        binding.inResultTitle.tvTitle.text = "Server"
        with(currentServerBeanDynamic) {
            binding.imgResultFlag.setImageResource(
                DynamicUtils.getFlagCountryDynamic(
                    this.dynamic_country ?: ""
                )
            )
            binding.tvResultName.text = this.dynamic_country
        }
    }

    override fun initDataCall() {
        if (isConnectionDynamic) {
            binding.tvState.text = "Connect success"
            binding.imgResultType.setImageResource(R.drawable.ic_result_connect)
        } else {
            binding.tvState.text = "Disconnected"
            binding.imgResultType.setImageResource(R.drawable.ic_result_disconnect)
        }
    }

}