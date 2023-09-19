package com.hcn.dynamicvpn.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.hcn.dynamicvpn.base.BaseViewModel
import com.hcn.dynamicvpn.bean.DynamicVpnBean
import com.hcn.dynamicvpn.data.DataUtils
import com.hcn.dynamicvpn.ui.main.MainCall
import com.hcn.dynamicvpn.utils.DynamicTimeUtils
import com.hcn.dynamicvpn.utils.DynamicUtils
import com.hcn.dynamicvpn.utils.SharedFlowBus
import kotlinx.coroutines.launch

class ListViewModel:BaseViewModel() {
    var listCall: ListCall? = null
    fun setListCallback(listCall: ListCall) {
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