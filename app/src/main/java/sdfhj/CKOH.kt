package sdfhj

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import dy.na.mic.base.BaseViewModel
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import sdfhh.CKOC
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils
import dy.na.mic.utils.SharedFlowBus
import kotlinx.coroutines.launch

class CKOH:BaseViewModel() {
    var listCall: CKOF? = null
    fun setListCallback(listCall: CKOF) {
        this.listCall = listCall
    }
    val showBackAd = MutableLiveData<Any>()
    val liveServerListData: MutableLiveData<MutableList<DynamicVpnBean>> by lazy {
        MutableLiveData<MutableList<DynamicVpnBean>>()
    }



    fun isSelect(
        ecServiceBeanList: MutableList<DynamicVpnBean>,
        checkSkServiceBeanClick: DynamicVpnBean,
        position: Int
    ): Boolean {
        return ecServiceBeanList[position].dynamic_ip == checkSkServiceBeanClick.dynamic_ip && ecServiceBeanList[position].dynamic_best == checkSkServiceBeanClick.dynamic_best
    }

    fun forEachIndexedFun(
        ecServiceBeanList: MutableList<DynamicVpnBean>,
        funData: (index: Int) -> Unit
    ) {
        ecServiceBeanList.forEachIndexed { index, _ ->
            funData(index)
        }
    }

    fun forEachIndexedFun2(
        ecServiceBeanList: MutableList<DynamicVpnBean>,
        checkSkServiceBeanClick: DynamicVpnBean,
        funData: (index: Int) -> Unit
    ) {
        ecServiceBeanList.forEachIndexed { index, _ ->
            funData(index)
        }
    }
}