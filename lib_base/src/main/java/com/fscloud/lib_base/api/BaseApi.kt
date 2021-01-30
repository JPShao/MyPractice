package com.fscloud.lib_base.api

import com.fscloud.lib_base.model.BaseResponse
import com.fscloud.lib_base.model.pay.WeChatPayOrderData
import com.fscloud.lib_base.utils.checkupdate.CheckUpdateData
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.*

interface BaseApi {
    @GET("{path}")
    fun test(@Path("path") path:String):Observable<BaseResponse<*>>

    @GET("order/Alipay/signOrder/{outTradeNo}")       //根据订单交易号，获取支付宝加密订单信息
    fun getAlipaySignOrder(@Path("outTradeNo") outTradeNo: String): Call<BaseResponse<String>>

    @POST("order/Weixinpay/pay")                      //根据商品信息，获取微信支付需要的订单信息
    fun getWeChatPayOrderData(@Body map: MutableMap<String,Any>): Call<BaseResponse<WeChatPayOrderData>>

    @POST("order/app/order/create")                   //生成订单
    fun createOrder(@Body map: MutableMap<String, Any>): Call<BaseResponse<String>>

    /**
     * 是否实名认证
     */
    @GET("management/user/validatedRealName/{userId}")
    fun checkIsAuth(@Path("userId") userId: String): Call<BaseResponse<Boolean>>

    /**
     * 检查更新
     */
    @GET("dynamicfrom/update/latest")
    fun checkUpdate(@Query("device")device: String): Call<BaseResponse<CheckUpdateData>>



    /**
     * 获取加密公钥
     */
    @GET("management/user/getPublicKey")
   suspend fun getRsaPublicKey() : BaseResponse<String>

}