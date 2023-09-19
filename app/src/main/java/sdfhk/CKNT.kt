package sdfhk

import dy.na.mic.R
import dy.na.mic.base.BaseActivity
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityWebDynamicBinding

class CKNT:BaseActivity<ActivityWebDynamicBinding,CKOI>() {
    override val implLayoutResId: Int
        get() = R.layout.activity_web_dynamic
    override val model: CKOI
        get() = CKOI()

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