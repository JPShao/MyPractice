package com.fscloud.lib_base.ui.pay.data

import android.os.Parcelable
import com.fscloud.lib_base.model.pay.WeChatPayOrderData
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.math.BigDecimal

/**
 * @author shaojunpei
 * @date 2020/11/21 20:26
 * @describe 用于订单支付结果传递数据
 */
@Parcelize
data class PaidOrderData(
    val type: String = "",                //对应商品一级分类，以前商品类型 1:代运营类; 2:证照代办类(没规格) 3: 会员
    val goodsId: Int = 0,
    val goodsName: String = "",
    val goodsPicture: String = "",
    val marketPrice: BigDecimal = BigDecimal(0),
    val memberPrice: BigDecimal = BigDecimal(0),
    val platformPrice: BigDecimal = BigDecimal(0),
    val specType: Int?,                  //商品规格类型1：单平台 2：双平台
    val quantity: Int?,                  //商品数量：1次 或 1个月
    val specTypeName: String = "",
    val quantityStr: String = "",
    val tradeNo: String = "",                //订单交易号
    val signOrder: String = "",              //支付宝加密订单信息
    var payStatus: Int = 0,                  //1 支付成功，2 支付失败
    val realPayMoney: String = "",           //实付金额
    val isMemberGoods: Boolean = false,      //主商品是否是会员商品
    val weChatPayData: WeChatPayOrderData? = null
): Parcelable