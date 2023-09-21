package sdfhj

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.elvishew.xlog.XLog
import com.google.gson.reflect.TypeToken
import dy.na.mic.R
import dy.na.mic.ad.AdDynamicUtils
import dy.na.mic.base.BaseActivity
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.data.DataUtils
import dy.na.mic.databinding.ActivityListDynamicBinding
import dy.na.mic.utils.DynamicTimeUtils
import dy.na.mic.utils.DynamicUtils
import dy.na.mic.utils.SharedFlowBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CKNS : BaseActivity<ActivityListDynamicBinding, CKOH>(), CKOF {
    private lateinit var selectAdapter: CKOG
    private var ecServiceBeanList: MutableList<DynamicVpnBean> = ArrayList()
    private lateinit var adBean: DynamicVpnBean
//    private var jobBackPower: Job? = null

    //选中服务器
    private lateinit var checkSkServiceBean: DynamicVpnBean
    private lateinit var checkSkServiceBeanClick: DynamicVpnBean

    // 是否连接
    private var whetherToConnect = false
    private var whetherHaveData = false
    private lateinit var dynamicServiceBean: DynamicVpnBean
    private lateinit var dynamicServiceBeanList: MutableList<DynamicVpnBean>
    override val implLayoutResId: Int
        get() = R.layout.activity_list_dynamic
    override val model: CKOH
        get() = CKOH()

    override fun initialize() {
        model.setListCallback(this)
        val bundle = intent.extras
        checkSkServiceBean = DynamicVpnBean()
        whetherToConnect = bundle?.getBoolean(DataUtils.whetherDynamicConnected) == true
        checkSkServiceBean = GsonUtils.fromJson(
            bundle?.getString(DataUtils.currentDynamicService),
            object : TypeToken<DynamicVpnBean?>() {}.type
        )
        checkSkServiceBeanClick = checkSkServiceBean
        binding.inListTitle.tvTitle.text = "Serve Location"
        binding.inListTitle.imgBack.setOnClickListener {
            returnToHomePageCall()
        }
    }

    private fun getServerListData() {
        dynamicServiceBeanList = ArrayList()
        dynamicServiceBean = DynamicVpnBean()
        dynamicServiceBeanList = DynamicUtils.getLocalServerData()
        dynamicServiceBeanList.add(0, DynamicUtils.getFastIpDynamic())
        echoServerCall(dynamicServiceBeanList)
    }

    override fun initData() {
        AdDynamicUtils.loadOf(DataUtils.ad_back)
        initSelectRecyclerViewCall()
        getServerListDataCall()
    }

    private fun showBackAd(it: Any) {
        AdDynamicUtils.showFullScreenOf(
            where = DataUtils.ad_back,
            context = this,
            res = it,
            onShowCompleted = {
                finish()
            }
        )
    }

    private fun initSelectRecyclerView() {
        selectAdapter = CKOG(ecServiceBeanList)
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        binding.rvListDynamic.layoutManager = layoutManager
        binding.rvListDynamic.adapter = selectAdapter
        selectAdapter.setOnItemClickListener(object : CKOG.OnItemClickListener {
            override fun onItemClick(position: Int) {
                selectServerCall(position)
            }
        })
    }

    /**
     * 选中服务器
     */
    private fun selectServer(position: Int) {
        if (model.isSelect(ecServiceBeanList, checkSkServiceBeanClick, position)) {
            if (!whetherToConnect) {
                finish()
                lifecycleScope.launch {
                    SharedFlowBus.with<DynamicVpnBean>(DataUtils.notConnectedDynamicReturn).post(
                        checkSkServiceBean
                    )
                }
            }
            return
        }
        model.forEachIndexedFun(ecServiceBeanList, funData = {
            ecServiceBeanList[it].dynamic_check = position == it
            if (ecServiceBeanList[it].dynamic_check == true) {
                checkSkServiceBean = ecServiceBeanList[it]
            }
        })
        selectAdapter.notifyDataSetChanged()
        showDisconnectDialogCall()
    }


    /**
     * 回显服务器
     */
    private fun echoServer(it: MutableList<DynamicVpnBean>) {
        ecServiceBeanList = it
        ecServiceBeanList.forEachIndexed { index, _ ->
            model.forEachIndexedFun2(ecServiceBeanList, checkSkServiceBeanClick) {
                if (checkSkServiceBeanClick.dynamic_best == true) {
                    ecServiceBeanList[0].dynamic_check = true
                } else {
                    ecServiceBeanList[index].dynamic_check =
                        ecServiceBeanList[index].dynamic_ip == checkSkServiceBeanClick.dynamic_ip
                    ecServiceBeanList[0].dynamic_check = false
                }
            }
        }
        selectAdapter.addData(ecServiceBeanList)
    }

    /**
     * 返回主页
     */
    private fun returnToHomePage() {
        AdDynamicUtils.resultOf(DataUtils.ad_back).run {
            if (this == null) {
                finish()
            } else {
                showBackAd(this)
            }
        }
    }

    /**
     * 是否断开连接
     */
    private fun showDisconnectDialog() {
        if (!whetherToConnect) {
            finish()
            lifecycleScope.launch {
                SharedFlowBus.with<DynamicVpnBean>(DataUtils.notConnectedDynamicReturn).post(
                    checkSkServiceBean
                )
            }
            return
        }
        val dialog: AlertDialog? = AlertDialog.Builder(this)
            .setMessage("You will disconnect from the current server in order to connect to a new one. Are you sure you want to disconnect?")
            //设置对话框的按钮
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
                ecServiceBeanList.forEachIndexed { index, _ ->
                    ecServiceBeanList[index].dynamic_check =
                        (ecServiceBeanList[index].dynamic_ip == checkSkServiceBeanClick.dynamic_ip && ecServiceBeanList[index].dynamic_best == checkSkServiceBeanClick.dynamic_best)
                }
                selectAdapter.notifyDataSetChanged()
            }
            .setPositiveButton("DISCONNECT") { dialog, _ ->
                dialog.dismiss()
                finish()
                lifecycleScope.launch {
                    SharedFlowBus.with<DynamicVpnBean>(DataUtils.connectedDynamicReturn).post(
                        checkSkServiceBean
                    )
                }
            }.create()
        dialog?.show()
        val params = dialog!!.window!!.attributes
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.attributes = params
        dialog.setCancelable(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            delay(300L)
            DynamicUtils.putPointDynamic("Lig_plodous")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnToHomePageCall()
        }
        return true
    }

    override fun initializeCall() {
    }

    override fun getServerListDataCall() {
        getServerListData()
    }

    override fun initSelectRecyclerViewCall() {
        initSelectRecyclerView()
    }

    override fun selectServerCall(position: Int) {
        selectServer(position)
    }

    override fun echoServerCall(it: MutableList<DynamicVpnBean>) {
        echoServer(it)
    }

    override fun returnToHomePageCall() {
        returnToHomePage()
    }

    override fun showDisconnectDialogCall() {
        showDisconnectDialog()
    }
}