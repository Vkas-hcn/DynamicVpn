package sdfhi

import android.view.KeyEvent
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import dy.na.mic.R
import dy.na.mic.base.BaseActivity
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityResultDynamicBinding
import dy.na.mic.utils.DynamicUtils
import kotlinx.coroutines.Job

class CKNR : BaseActivity<ActivityResultDynamicBinding, CKOD>() ,CKOE{
    private var isConnectionDynamic: Boolean = false

    //当前服务器
    private lateinit var currentServerBeanDynamic: DynamicVpnBean
    override val implLayoutResId: Int
        get() = R.layout.activity_result_dynamic
    override val model: CKOD
        get() = CKOD()

    override fun initialize() {
        model.setResultCallBack(this)
        initializeCall()
    }

    override fun initData() {
        initDataCall()
    }

    private fun returnToHomePage() {
        finish()
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
                DynamicUtils.getFlagThroughCountryDynamic(
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
//        BE.nativeAdRefreshDynamic =true
    }
}