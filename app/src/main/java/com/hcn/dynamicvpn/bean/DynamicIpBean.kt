package com.hcn.dynamicvpn.bean

import androidx.annotation.Keep

@Keep
data class DynamicIpBean(
    var country: String?=null,
    var country_code: String?=null,
    var country_code3: String?=null,
    var ip: String?=null
)
