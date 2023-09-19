package com.hcn.dynamicvpn.ui.list

import com.hcn.dynamicvpn.bean.DynamicVpnBean

interface ListCall {
    fun initializeCall()
    fun getServerListDataCall()
    fun initSelectRecyclerViewCall()
    fun selectServerCall(position: Int)
    fun echoServerCall(it: MutableList<DynamicVpnBean>)
    fun returnToHomePageCall()
    fun showDisconnectDialogCall()
}