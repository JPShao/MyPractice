package com.fscloud.lib_base.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author shaojunpei
 * @date 2020/4/10
 */

object UtilTime {

    /**
     * unix时间戳转为 具体格式日期,yyyy-MM-dd-HH-mm-ss
     */
    fun formatYYMMdd(msDate: Long): String {
        return SimpleDateFormat("yyyy年MM月dd日").format(Date(msDate))
    }

    fun formatHHmm(msDate: Long): String {
        return SimpleDateFormat("HH:mm").format(Date(msDate))
    }

    fun formatYYMMddHHmm(msDate: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(msDate))
    }
    fun formatYY_MM_dd(msDate: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(msDate))
    }

    /**
     * 将字符串时间戳转为long
     * yyyy-MM-dd
     */
    fun timeStrToDate(str: String): Long? {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = simpleDateFormat.parse(str)
        return date?.time
    }

    /**
     * 比较两段时间相差多少小时
     * 时间格式 oldTime =2020-10-21  newTime = 2020-10-31
     */
    fun compareToDateByHour(nowTime: String,oldTime: String): Int? {
        try {
            val long1 = timeStrToDate(oldTime)
            val long2 = timeStrToDate(nowTime)
            val hour = (long2!!-long1!!)/1000/60/60
            return hour.toInt()
        } catch (e: Exception) {
            return null
        }
    }

    /**
     *  秒 转为 时分秒(00:25:35)
     */
    fun secondToHourMinuteSecond(second: Int): String{
        var hms = ""
        if (second > 0) {
            var h = second / (60 * 60)
            var m = (second - h * 60 * 60) / 60
            var s = second - h * 60 * 60 - m * 60
            hms = if (h < 10) "0$h" else "$h"
            hms += if (m < 10) ":0$m" else ":$m"
            hms += if (s < 10) ":0$s" else ":$s"
        }
        return hms
    }

    /**
     * 2020-12-25 02:39:10，去掉时分秒
     */
    fun removeHourMinuteSecond(time: String): String {
        return if (time.length >= 10) {
            time.substring(0, 10)
        } else {
            time
        }
    }
}