package com.hcn.dynamicvpn.utils

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.hcn.dynamicvpn.data.DataUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.*
import java.text.DecimalFormat

object DynamicTimeUtils {

    //判断一个时间在另一个时间之后
    fun dateAfterDate(startTime: String?, endTime: String?): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            val startDate: Date = format.parse(startTime)
            val endDate: Date = format.parse(endTime)
            val start: Long = startDate.getTime()
            val end: Long = endDate.getTime()
            if (end > start) {
                return true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
        return false
    }

    /**
     * @return 当前日期
     */
    fun formatDateNow(): String {
        val date = Date()
        val data = TimeUtils.date2String(date, "yyyy-MM-dd")
        return data
    }

    //当日日期
    var adDateOg = ""

    /**
     * 判断是否是当天打开
     */
    fun isAppOpenSameDayDynamic() {
        GlobalScope.launch {
            adDateOg = DataUtils.currentDateDynamic
            if (DataUtils.currentDateDynamic == "") {
                DataUtils.currentDateDynamic = formatDateNow()
            } else {
                if (dateAfterDate(DataUtils.currentDateDynamic, formatDateNow())) {
                    DataUtils.currentDateDynamic = formatDateNow()
                    DataUtils.clicksCountDynamic = 0
                    DataUtils.showsCountDynamic = 0
                }
            }
        }
    }


    private val job = Job()
    private val timerThread = CoroutineScope(job)
    var skTime = 0
    var isStopThread = true

    /**
     * 发送定时器信息
     */
    fun sendTimerInformation() {
        timerThread.launch {
            while (isActive) {
                skTime++
                if (!isStopThread) {
                    SharedFlowBus.with<String>(DataUtils.timerDynamicData).post(formatTime(skTime))
                }
                delay(1000)
            }
        }
    }

    /**
     * 开始计时
     */
    fun startTiming() {
        if (isStopThread) {
            skTime = 0
        }
        isStopThread = false
    }

    /**
     * 结束计时
     */
    suspend fun endTiming(isP: Boolean = false) {
        DataUtils.LAST_TIME = formatDateNow()
        if (!isP) {
//            P.toBuriedPointConnectionTimeMeteor("time_vpn_meteorem", skTime)
        }
        skTime = 0
        isStopThread = true
        SharedFlowBus.with<String>(DataUtils.timerDynamicData).post(formatTime(skTime))
    }

    /**
     * 设置时间格式
     */
    private fun formatTime(timerData: Int): String {
        val hh: String = DecimalFormat("00").format(timerData / 3600)
        val mm: String = DecimalFormat("00").format(timerData % 3600 / 60)
        val ss: String = DecimalFormat("00").format(timerData % 60)
        return "$hh:$mm:$ss"
    }
}