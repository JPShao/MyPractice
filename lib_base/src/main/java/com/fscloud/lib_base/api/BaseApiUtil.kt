package com.fscloud.lib_base.api

import com.fscloud.lib_base.net.RetrofitManager

/**
 * @author shaojunpei
 * @date 2020/11/19 18:27
 * @describe
 */
object BaseApiUtil {
    fun getBaseApi() = RetrofitManager.getInstance().service(BaseApi::class.java)
}