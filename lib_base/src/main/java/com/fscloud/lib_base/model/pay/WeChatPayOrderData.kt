package com.fscloud.lib_base.model.pay

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author shaojunpei
 * @date 2020/12/28 16:39
 * @describe  微信支付需要的订单数据
 */
@Parcelize
data class WeChatPayOrderData(
    val appid: String,
    val noncestr: String,           //随机字符串
    val sign: String,                //签名
    val timestamp: String,           //时间戳
    val prepayid: String,            //预支付交易会话标识
    val partnerid: String,           //商户id
    val `package`: String,
    val result_code: String,         //业务结果
    val err_code: String,            //错误代码
    val err_code_des: String,        //错误代码描述
    val trade_type: String,          //交易类型
    val device_info: String         //设备号
): Parcelable