package com.fscloud.lib_base.utils

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 类名：NumberUtil
 * 包名：com.fscloud.lib_base.utils
 * 创建时间：2020/11/18 11:27
 * 创建人： shota
 * 描述：
 **/
class NumberUtil {
    companion object {
        /**
         * double类型如果小数点后为零则显示整数否则保留两位小数
         */
        fun formatDouble(d: Double): String? {
            val bg = BigDecimal(d).setScale(2, RoundingMode.UP)
            val num = bg.toDouble()
            if (Math.round(num) - num == 0.0) {
                return (num.toLong()).toString()
            }
            return num.toString()
        }
    }
}