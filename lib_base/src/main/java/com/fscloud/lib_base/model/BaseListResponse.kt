package com.fscloud.lib_base.model

/**
 * 返回数组实体
 */
data class BaseListResponse<T>(
        val code: String = "",
        val message: String = "",
        val data: List<T>? = null
)