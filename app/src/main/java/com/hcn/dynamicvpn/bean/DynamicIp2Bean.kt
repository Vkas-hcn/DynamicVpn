package com.hcn.dynamicvpn.bean

import androidx.annotation.Keep

@Keep
data class DynamicIp2Bean(
    val country: String,
    val cc: String,
    val ip: String
)
