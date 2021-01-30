package com.fscloud.lib_base.model.pay

/**
 * @author shaojunpei
 * @date 2020/11/19 21:01
 * @describe 弹出支付弹窗时，需要的数据模型
 */

data class PaymentData(
    val type: String = "",                      //对应商品一级分类，以前商品类型 1:代运营类; 2:证照代办类(没规格)
    val goodsName: String = "",
    val specTypeName: String = "",              //规格，平台
    val quantityStr: String = "",               //数量
    val realPayMoney: String = "",              //实付金额
    val tradeNo: String = "",                   //订单交易号
    val signOrder: String = "",                 //支付宝加密订单信息
    val pricePlanId: Int? = null,                //会员id
    val isMemberGoods: Boolean = false,           //主商品是否是会员商品
    val weChatPayData: WeChatPayOrderData? = null  //微信支付订单信息
)