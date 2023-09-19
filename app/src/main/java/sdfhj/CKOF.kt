package sdfhj

import dy.na.mic.bean.DynamicVpnBean

interface CKOF {
    fun initializeCall()
    fun getServerListDataCall()
    fun initSelectRecyclerViewCall()
    fun selectServerCall(position: Int)
    fun echoServerCall(it: MutableList<DynamicVpnBean>)
    fun returnToHomePageCall()
    fun showDisconnectDialogCall()
}