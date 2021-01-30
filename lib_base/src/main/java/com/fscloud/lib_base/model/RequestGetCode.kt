package com.fscloud.lib_base.model

/**
 * @ProjectName : kaidianbao
 * @Author : JackCui
 * @Time : 2020/9/27 11:35
 * @Description : 获取验证码请求
 */
data class RequestGetCode(
    //手机号
    var phone: String? = null,
    //类型 1用于登录 ，2用于注册
    var type: Int? = null
) {
}