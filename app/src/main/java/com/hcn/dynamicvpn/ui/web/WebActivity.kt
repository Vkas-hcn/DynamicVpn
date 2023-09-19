package com.hcn.dynamicvpn.ui.web

import com.hcn.dynamicvpn.R
import com.hcn.dynamicvpn.base.BaseActivity
import com.hcn.dynamicvpn.data.DataUtils
import com.hcn.dynamicvpn.databinding.ActivityWebDynamicBinding

class WebActivity:BaseActivity<ActivityWebDynamicBinding,WebViewModel>() {
    override val implLayoutResId: Int
        get() = R.layout.activity_web_dynamic
    override val model: WebViewModel
        get() = WebViewModel()

    override fun initialize() {
        binding.inWebTitle.tvTitle.text = "Privacy Policy"
        binding.inWebTitle.imgBack.setOnClickListener {
            finish()
        }
        binding.webDynamic.loadUrl(DataUtils.privacyDynamicAgreement)
    }

    override fun initData() {
        model.setWeb(binding)

    }
}