package sdfhm

import android.content.Context
import com.tencent.mmkv.MMKV
import android.net.VpnService
import com.github.shadowsocks.bean.AroundFlowBean
import com.github.shadowsocks.utils.JsonUtils

import com.google.gson.reflect.TypeToken

object CKOP {
//    fun brand(builder: CKNV.Builder, myPackageName: String) {
//        (listOf(myPackageName) + listGmsPackages())
//            .iterator()
//            .forEachRemaining {
//                runCatching { builder.addDisallowedApplication(it) }
//            }
//    }

    /**
     * 默认黑名单
     */
    private fun listGmsPackages(): List<String> {
        return listOf(
            "com.google.android.gms",
            "com.google.android.ext.services",
            "com.google.process.gservices",
            "com.android.vending",
            "com.google.android.gms.persistent",
            "com.google.android.cellbroadcastservice",
            "com.google.android.packageinstaller"
        )
    }
}