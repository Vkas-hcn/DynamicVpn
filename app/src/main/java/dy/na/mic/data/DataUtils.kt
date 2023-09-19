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

    // 服务器数据
    var PROFILE_UF_DATA: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("PROFILE_UF_DATA", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("PROFILE_UF_DATA", "") ?: ""

    // 最佳服务器
    var PROFILE_UF_DATA_FAST: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("PROFILE_UF_DATA_FAST", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("PROFILE_UF_DATA_FAST", "") ?: ""

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
    var INSTALL_REFERRER: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("INSTALL_REFERRER", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("INSTALL_REFERRER", "") ?: ""

    //uf_remote
    var UF_REMOTE: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("UF_REMOTE", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("UF_REMOTE", "") ?: ""

    // 下发服务器数据
    var DISTRIBUTE_SERVER_DATA: String? = null
        set(value) {
            dynaSp.edit().run {
                putString("DISTRIBUTE_SERVER_DATA", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("DISTRIBUTE_SERVER_DATA", null)

    //google广告id
    var GOOGLE_ADVERTISING_ID: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("GOOGLE_ADVERTISING_ID", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("GOOGLE_ADVERTISING_ID", "") ?: ""

    //当前IP
    var CURRENT_IP: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("CURRENT_IP", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("CURRENT_IP", "") ?: ""

    // Session Json
    var SESSION_JSON: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("SESSION_JSON", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("SESSION_JSON", "") ?: ""

    // Session Json
    var INSTALL_TYPE: Boolean = false
        set(value) {
            dynaSp.edit().run {
                putBoolean("INSTALL_TYPE", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getBoolean("INSTALL_TYPE", false)

    //VPN链接后的IP
    var IP_AFTER_VPN_LINK: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("IP_AFTER_VPN_LINK", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("IP_AFTER_VPN_LINK", "") ?: ""

    //VPN链接后的城市
    var IP_AFTER_VPN_CITY: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("IP_AFTER_VPN_CITY", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("IP_AFTER_VPN_CITY", "") ?: ""

    //1:打开黑名单用户屏蔽； //2:关闭黑名单用户屏蔽； //默认1
    var UNLIM_CLOAK: String = "1"
        set(value) {
            dynaSp.edit().run {
                putString("UNLIM_CLOAK", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("UNLIM_CLOAK", "1") ?: "1"

    //是否是黑名单用户
    var BLACKLIST_USER: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("BLACKLIST_USER", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("BLACKLIST_USER", "") ?: ""

    // 广告数据
    var ADVERTISING_UF_DATA: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("ADVERTISING_UF_DATA", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("ADVERTISING_UF_DATA", "") ?: ""

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

    //vpn状态
    var vpnStatus: Boolean = false
        set(value) {
            dynaSp.edit().run {
                putBoolean("vpnStatus", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getBoolean("vpnStatus", false)

    //vpn状态
    var vpnStatusName: String = ""
        set(value) {
            dynaSp.edit().run {
                putString("vpnStatusName", value)
                commit()
            }
            field = value
        }
        get() = dynaSp.getString("vpnStatusName", "") ?: ""

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


}