package com.hcn.dynamicvpn.ui.web

import android.webkit.WebView
import android.webkit.WebViewClient
import com.hcn.dynamicvpn.base.BaseViewModel
import com.hcn.dynamicvpn.data.DataUtils
import com.hcn.dynamicvpn.databinding.ActivityWebDynamicBinding

class WebViewModel:BaseViewModel() {
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