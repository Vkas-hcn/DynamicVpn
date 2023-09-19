package com.hcn.dynamicvpn.bean

import androidx.annotation.Keep

@Keep
data class DynamicVpnBean(
    var dynamic_city: String? = null,
    var dynamic_country: String? = null,
    var dynamic_ip: String? = null,
    var dynamic_protocol: String? = null,
    var dynamic_port: Int? = null,
    var dynamic_password: String? = null,
    var dynamic_check: Boolean? = false,
    var dynamic_best: Boolean? = false
)