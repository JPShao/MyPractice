package com.fscloud.lib_base.eventbus.employee

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/8 11:25
 * @description:MessageEmployee   员工    新增1  修改2  删除3 成功消息
 */
data class MessageEmployee(
    val type:Int,
    val empId:Int? = null
)
