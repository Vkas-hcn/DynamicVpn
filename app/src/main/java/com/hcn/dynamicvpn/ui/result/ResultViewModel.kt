package com.hcn.dynamicvpn.ui.result

import com.hcn.dynamicvpn.base.BaseViewModel

class ResultViewModel:BaseViewModel() {
    var resultCall: ResultCall? = null
    fun setResultCallBack(resultCall: ResultCall) {
        this.resultCall = resultCall
    }
}