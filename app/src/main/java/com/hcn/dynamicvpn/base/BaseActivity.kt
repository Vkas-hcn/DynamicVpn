package com.hcn.dynamicvpn.base
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.hcn.dynamicvpn.BR
import com.hcn.dynamicvpn.R


abstract class BaseActivity <B : ViewDataBinding, M : BaseViewModel> : AppCompatActivity() {
    protected lateinit var binding: B
    protected abstract val model: M
    protected abstract val implLayoutResId: Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adaptScreen()
        initSystemUi()
        initDataBinding()
        initialize()
        initData()
    }
    abstract fun initialize()

    abstract fun initData()

    private fun initSystemUi() {
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, true)
    }

    private fun initDataBinding() {
        val that = this
        binding = DataBindingUtil.setContentView<B>(this, implLayoutResId)
            .apply {
                lifecycleOwner = that
                setVariable(BR._all, model)
            }

    }

    private fun adaptScreen() {
        immersionBar {
            statusBarColor(R.color.colorPrimary)
            navigationBarColor(R.color.colorPrimary)
        }
        with(resources.displayMetrics) {
            density = heightPixels / 780.0F
            densityDpi = (160 * density).toInt()
        }
    }
    // 跳转到另一个Activity
    fun <T> launchActivity(activityClass: Class<T>, extras: Bundle? = null) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        startActivity(intent)
    }

    // 跳转到另一个Activity，并等待返回结果
    fun <T> launchActivityForResult(activityClass: Class<T>, requestCode: Int, extras: Bundle? = null) {
        val intent = Intent(this, activityClass)
        extras?.let { intent.putExtras(it) }
        startActivityForResult(intent, requestCode)
    }
    override fun onDestroy() {
        super.onDestroy()

    }
}