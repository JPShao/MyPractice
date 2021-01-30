package com.fscloud.lib_base.model

/**
 * 返回实体
 */
data class BaseResponse<T>(
    val code:String = "",
    val message:String = "",
    val data:T? = null
)