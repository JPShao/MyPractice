package com.fscloud.lib_base.db

import com.blankj.utilcode.util.GsonUtils
import com.fscloud.lib_base.model.LoggedInUser
import com.fscloud.lib_base.constant.Mapper

/**
 * @ProjectName : kaidianbao
 * @Author : JackCui
 * @Time : 2020/9/28 10:20
 * @Description : 用户信息获取
 */
object User {
    val userInfo: LoggedInUser by lazy {
        //获取userId
        var key = SpUtils.getInstance().decodeString(Mapper.ACCOUNT)
        //获取该用户的信息
        var gson = SpUtils.getInstance().decodeString(key)
        GsonUtils.fromJson(gson, LoggedInUser::class.java)
    }
}