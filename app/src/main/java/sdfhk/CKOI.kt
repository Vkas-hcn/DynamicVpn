package sdfhk

import android.webkit.WebView
import android.webkit.WebViewClient
import dy.na.mic.base.BaseViewModel
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityWebDynamicBinding

class CKOI:BaseViewModel() {
    fun setWeb(binding:ActivityWebDynamicBinding){

        binding.webDynamic.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (DataUtils.privacyDynamicAgreement == url) {
                    view.loadUrl(url)
                } else {
                    // 系统处理
                    return super.shouldOverrideUrlLoading(view, url)
                }
                view.loadUrl(url)
                return true
            }
        }
    }
}