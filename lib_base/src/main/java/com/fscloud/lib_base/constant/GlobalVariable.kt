package com.fscloud.lib_base.constant

import com.blankj.utilcode.util.GsonUtils
import com.fscloud.lib_base.db.SpUtils
import com.fscloud.lib_base.model.EnterpriseData
import com.fscloud.lib_base.model.LoggedInUser
import java.lang.Exception


/**
 * @author shaojunpei
 * @date 2020/10/7 12:03
 * @describe  全局变量
 */
object GlobalVariable {

    val token: String get() = SpUtils.getInstance().decodeString(Mapper.TOKEN)
    val userId: String get() = SpUtils.getInstance().decodeString(Mapper.ACCOUNT)
    val phone: String get() = SpUtils.getInstance().decodeString(Mapper.PHONE)

    /**
     * 用户是否取消了更新，如取消了，下次不在弹出
     */
    val isCancelUpdate: Boolean get() = SpUtils.getInstance().decodeBoolean(Mapper.IS_CANCEL_UPDATE)

    /**
     * 企业数据
     */
    val enterpriseData: EnterpriseData? get() = try {
        val enterpriseInfo = SpUtils.getInstance().decodeString(Mapper.ENTERPRISE)
        if (enterpriseInfo.isNotEmpty()){
            GsonUtils.fromJson(enterpriseInfo,EnterpriseData::class.java)
        }else {
            null
        }
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    /**
     * 是否有企业
     */
    val isHaveEnterprise: Boolean get() =
        SpUtils.getInstance().decodeString(Mapper.ENTERPRISE).isNotEmpty()

    /**
     * 本地是否已经保存了用户实名信息
     */
    val isHasAuthInfo: Boolean get() =
        SpUtils.getInstance().decodeString(Mapper.AUTH_INFO).isNotEmpty()

    /**
     * 实名认证信息
     */
    val authInfo: LoggedInUser.Auth? get() = try {
        val authInfo = SpUtils.getInstance().decodeString(Mapper.AUTH_INFO)
        if (authInfo.isNotEmpty()){
            GsonUtils.fromJson(authInfo,LoggedInUser.Auth::class.java)
        }else {
            null
        }
    }catch (e: Exception){
        e.printStackTrace()
        null
    }


}