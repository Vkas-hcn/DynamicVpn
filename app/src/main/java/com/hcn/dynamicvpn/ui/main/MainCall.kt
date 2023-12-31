package com.hcn.dynamicvpn.ui.main

import com.hcn.dynamicvpn.bean.DynamicVpnBean

interface MainCall {
    fun jumpResultsPageDataCall()
    fun setServiceDataCall()
    fun flowBusFunCall()
    fun setTitleDataCall()
    fun toPPCall()
    fun toUpgradeCall()
    fun toShareCall()
    fun jumpToServerListCall()
    fun setFastInformationCall(elVpnBean: DynamicVpnBean)
    fun startVpnCall()
    fun connectOrDisconnectDynamicCall()
    fun changeOfVpnStatusCall(state: Int, isPlan: Boolean = false)
    fun showVpnGuideDynamicCall()
    fun onActivityResultFunCall()
}