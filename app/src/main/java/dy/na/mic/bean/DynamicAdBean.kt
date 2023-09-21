package dy.na.mic.bean

import androidx.annotation.Keep

@Keep
data class DynamicAdBean(
    var Lig_anth: MutableList<DynamicAdDetailBean> = ArrayList(),
    var Lig_fari: MutableList<DynamicAdDetailBean> = ArrayList(),
    var Lig_fusia: MutableList<DynamicAdDetailBean> = ArrayList(),
    var Lig_plum: MutableList<DynamicAdDetailBean> = ArrayList(),
    var Lig_pulch: MutableList<DynamicAdDetailBean> = ArrayList(),
    var Lig_acity: Int = 0,
    var Lig_ophit: Int = 0
)

@Keep
data class DynamicAdDetailBean(
    val Lig_hster: String,
    val Lig_minen: String,
    val Lig_thful: String,
    val Lig_ofag: Int
)
