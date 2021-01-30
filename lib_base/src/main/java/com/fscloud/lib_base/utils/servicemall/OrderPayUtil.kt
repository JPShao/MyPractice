package com.fscloud.lib_base.utils.servicemall

/**
 * @author shaojunpei
 * @date 2020/11/24 15:21
 * @describe
 */

object OrderPayUtil {

    fun getPlatformName(specType: Int): String{
        return when(specType){
            1 -> "美团"
            2 -> "饿了么"
            3 -> "双平台"
            else -> ""
        }
    }

    fun getQuantityStr(quantity: Int,unit: String): String{
        return "$quantity$unit"
    }

}