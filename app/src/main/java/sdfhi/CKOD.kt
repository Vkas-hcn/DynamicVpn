package sdfhi

import dy.na.mic.base.BaseViewModel

class CKOD:BaseViewModel() {
    var resultCall: CKOE? = null
    fun setResultCallBack(resultCall: CKOE) {
        this.resultCall = resultCall
    }
}