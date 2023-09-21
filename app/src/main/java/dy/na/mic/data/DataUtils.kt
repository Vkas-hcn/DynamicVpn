package dy.na.mic.data

import android.content.Context
import sdfhf.CKNO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DataUtils {
    // 分享地址
    const val shareDynamicAddress="https://play.google.com/store/apps/details?id="
    // privacy_agreement
    const val privacyDynamicAgreement="https://www.baidu.com/"
    val returnDynamicCurrentPage = "returnDynamicCurrentPage"

    val connectionDynamicStatus = "connectionDynamicStatus"

    val timerDynamicData = "timerDynamicData"

    val whetherDynamicConnected = "whetherDynamicConnected"

    val currentDynamicService = "currentDynamicService"

    val serverDynamicInformation = "serverDynamicInformation"

    val stopVpnConnectionDynamic = "stopVpnConnectionDynamic"

    val notConnectedDynamicReturn = "notConnectedDynamicReturn"

    val connectedDynamicReturn = "connectedDynamicReturn"

    val connectAdDynamicShow = "connectAdDynamicShow"

    val listDataDynamic = "listDataDynamic"
    var isStartDynamic: Boolean = true


    val ad_open = "Lig_anth"
    val ad_home = "Lig_fari"
    val ad_result = "Lig_fusia"
    val ad_connect = "Lig_plum"
    val ad_back = "Lig_pulch"

    val open = "oaire"
    val nav = "rise"
    val ins = "homise"

    private val dynaSp by lazy {
        CKNO.context.getSharedPreferences(
            "Dynamic",
            Context.MODE_PRIVATE
        )
    }

    // 当日日期
    var currentDateDynamic: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("currentDateDynamic", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("currentDateDynamic", "") ?: ""

    // 点击次数
    var clicksCountDynamic: Int = 0
        set(value) {
            dynaSp.edit().run {
                putInt("clicksCountDynamic", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getInt("clicksCountDynamic", 0)

    // 展示次数
    var showsCountDynamic: Int = 0
        set(value) {
            dynaSp.edit().run {
                putInt("showsCountDynamic", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getInt("showsCountDynamic", 0)


    //UUID值
    var UUID_VALUE: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("uuidValue", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("uuidValue", "") ?: ""

    // 最后时间（00：00：00）
    var LAST_TIME: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("LAST_TIME", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("LAST_TIME", "") ?: ""

    // 最后时间（秒）
    var LAST_TIME_SECOND: Int = 0
        set(value) {
            dynaSp.edit().run {
                putInt("LAST_TIME_SECOND", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getInt("LAST_TIME_SECOND", 0)

    //ip信息
    var IP_INFORMATION_DYNAMIC: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("IP_INFORMATION_DYNAMIC", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("IP_INFORMATION_DYNAMIC", "") ?: ""

    //ip信息
    var IP_INFORMATION_DYNAMIC2: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("IP_INFORMATION_DYNAMIC2", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("IP_INFORMATION_DYNAMIC2", "") ?: ""

    //installReferrer
    var install_referrer_dynamic: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("INSTALL_REFERRER_DYNAMIC", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("INSTALL_REFERRER_DYNAMIC", "") ?: ""

    val online_ref_tag = "Lig_brev"
    var dynamic_ref: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("dynamic_ref", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("dynamic_ref", "") ?: ""

    val online_ad_tag = "Lig_meet"
    // 广告数据
    var ad_dynamic_data: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("ad_dynamic_data", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("ad_dynamic_data", "") ?: ""

    val online_con_tag = "Lig_eatate"
    var con_dynamic_data: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("con_dynamic_data", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("con_dynamic_data", "") ?: ""
    //currentServerData
    var current_server_data_dynamic: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("current_server_data_dynamic", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("current_server_data_dynamic", "") ?: ""

    // 点击次数
    var blackRetriesUn: Int = 0
        set(value) {
            dynaSp.edit().run {
                putInt("blackRetriesUn", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getInt("blackRetriesUn", 0)

    var isPermissionDynamicData = false
        set(value) {
            dynaSp.edit().run {
                putBoolean("is_permission_dynamic_data", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getBoolean("is_permission_dynamic_data", false)

    var nativeHomeAdRefreshDynamic:Boolean = false
    var nativeResultAdRefreshDynamic:Boolean = false
    var isHotHome: Boolean = false

}