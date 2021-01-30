package com.fscloud.lib_base.model

data class BasePageResponse<T>(
        var data: List<T>? = null,
        var total: Int = 0
) {
}