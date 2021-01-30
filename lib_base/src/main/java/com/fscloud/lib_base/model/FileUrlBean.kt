package com.fscloud.lib_base.model

import com.google.gson.annotations.SerializedName

/**
 * @ProjectName : kaidianbao
 * @Author : JackCui
 * @Time : 2020/9/29 10:27
 * @Description : 文件描述
 */
data class FileUrlBean(
    val code:String = "",
    val message:String = "",
    @SerializedName("data")
    val url:String = ""
) {

}