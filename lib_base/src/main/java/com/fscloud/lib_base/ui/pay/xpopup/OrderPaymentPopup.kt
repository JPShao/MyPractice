package com.fscloud.lib_base.ui.pay.xpopup

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.fscloud.lib_base.R
import com.fscloud.lib_base.model.pay.PaymentData
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.db.SpUtils
import com.fscloud.lib_base.api.BaseApiUtil
import com.fscloud.lib_base.model.pay.WeChatPayOrderData
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.tbruyelle.rxpermissions3.RxPermissions
import com.xgr.alipay.alipay.AliPay
import com.xgr.alipay.alipay.AlipayInfoImpli
import com.xgr.easypay.EasyPay
import com.xgr.easypay.callback.IPayCallback
import kotlinx.android.synthetic.main.lib_base_xpopup_order_payment.view.*
import org.jetbrains.anko.toast
import com.fscloud.lib_base.net.startHttp
import com.fscloud.lib_base.router.RouterTable
import com.fscloud.lib_base.umstatistics.UmClickKey
import com.fscloud.lib_base.umstatistics.UmEventUtils
import com.xgr.wechatpay.wxpay.WXPay
import com.xgr.wechatpay.wxpay.WXPayInfoImpli

/**
 * @author shaojunpei
 * @date 2020/11/17 15:23
 * @describe 订单支付弹窗 isOrderList是否是订单列表进入
 */

class OrderPaymentPopup(val activity: FragmentActivity, private val paymentData: PaymentData, private val isOrderList: Boolean = false)
    : BottomPopupView(activity) {

    private var popupView: BasePopupView? = null

    private var type = 0   //1 支付宝 2 微信
    var payResult = 0      //1 支付成功，2 支付失败， 3 支付取消

    override fun getImplLayoutId(): Int {
        return R.layout.lib_base_xpopup_order_payment
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        setClickEvent()
    }

    override fun onDismiss() {
        super.onDismiss()
    }

    private fun initView(){
        textMoney.text = "￥${paymentData.realPayMoney}"
        if (paymentData.isMemberGoods){  //购买会员
            textDescribe.text = "${paymentData.goodsName},${paymentData.quantityStr}"
        }else {
            if (paymentData.specTypeName.isEmpty()){
                textDescribe.text = "${paymentData.goodsName},${paymentData.quantityStr},${paymentData.specTypeName}"
            }else {
                textDescribe.text = "${paymentData.goodsName},${paymentData.quantityStr}"
            }
        }
        //默认选择支付宝
        type = 1
        textAlipay.isSelected = true
        textWeChat.isSelected = false
    }

    private fun setClickEvent(){
        //关闭弹窗
        imageClose.setOnClickListener {
            dismiss()
        }
        textAlipay.setOnClickListener {
            type = 1
            textAlipay.isSelected = true
            textWeChat.isSelected = false
        }
        textWeChat.setOnClickListener {
            /*context.toast("暂未开通微信支付")
            return@setOnClickListener*/
            type = 2
            textAlipay.isSelected = false
            textWeChat.isSelected = true
        }
        //立即支付
        textPayment.setOnClickListener {
            UmEventUtils.onEvent(UmClickKey.MEMBER_PAY)
            when(type){
                1 -> {   //支付宝
                    if (paymentData.isMemberGoods){  //购买会员，先创建订单
                        if (isOrderList){  //订单列表进入，不创建订单
                            if (paymentData.signOrder.isNotEmpty()){
                                requestPermission(paymentData.signOrder)
                            }else {
                                getAlipaySignOrder(paymentData.tradeNo,true)
                            }
                        }else {
                            createOrder(1)
                        }
                    }else {
                        if (paymentData.signOrder.isNotEmpty()){
                            requestPermission(paymentData.signOrder)
                        }else {
                            getAlipaySignOrder(paymentData.tradeNo)
                        }
                    }
                }
                2 -> {   //微信
                    if (paymentData.isMemberGoods){  //购买会员，先创建订单
                        if (isOrderList){  //订单列表进入，不创建订单
                            if (paymentData.weChatPayData != null){
                                requestPermission(paymentData.weChatPayData)
                            }else {
                                getWeChatOrderData(paymentData.tradeNo,true)
                            }
                        }else {
                            createOrder(2)
                        }
                    }else {
                        if (paymentData.weChatPayData != null){
                            requestPermission(paymentData.weChatPayData)
                        }else {
                            getWeChatOrderData(paymentData.tradeNo)
                        }
                    }
                }
            }
        }
    }

    //isMember 是否是会员中心购买会员
    private fun getAlipaySignOrder(tradeNo: String,isMember: Boolean = false){
        if (!isMember){
            showLoadingPopup()
        }
        BaseApiUtil.getBaseApi().getAlipaySignOrder(tradeNo)
            .startHttp({
                dismissLoadingPopup()
                if (it.code != "1") return@startHttp
                if (!it.data.isNullOrEmpty()) {
                    requestPermission(it.data)
                } else {
                    context.toast("获取的支付宝订单信息为空")
                }
            }, {
                dismissLoadingPopup()
            })
    }

    //isMember 是否是会员中心购买会员
    private fun getWeChatOrderData(tradeNo: String,isMember: Boolean = false){
        if (!isMember){
            showLoadingPopup()
        }
        val map: MutableMap<String,Any> = mutableMapOf(
            //"body" to paymentData.goodsName,
            "out_trade_no" to tradeNo,
            "trade_type" to "APP"
        )
        BaseApiUtil.getBaseApi().getWeChatPayOrderData(map)
            .startHttp({
                dismissLoadingPopup()
                if (it.code != "1") return@startHttp
                if (it.data != null) {
                    requestPermission(it.data)
                } else {
                    context.toast("获取的支付宝订单信息为空")
                }
            }, {
                dismissLoadingPopup()
            })
    }

    private fun requestPermission(orderInfo: String){
        val rxPermissions = RxPermissions(activity)
        rxPermissions
            .request(Manifest.permission.READ_PHONE_STATE)
            .subscribe {
                if (it){
                    startAlipay(orderInfo)
                }else {
                    context.toast("未允许权限无法进行支付宝支付")
                }
            }
    }

    private fun requestPermission(data: WeChatPayOrderData){
        val rxPermissions = RxPermissions(activity)
        rxPermissions
            .request(Manifest.permission.READ_PHONE_STATE)
            .subscribe {
                if (it){
                    startWeChatPay(data)
                }else {
                    activity.toast("未允许权限无法进行微信支付")
                }
            }
    }

    private fun startAlipay(orderInfo: String){
        val alipay = AliPay()
        val alipayInfo = AlipayInfoImpli()
        alipayInfo.orderInfo = orderInfo
        EasyPay.pay(alipay,activity,alipayInfo,object : IPayCallback{
            override fun success() {
                dismiss()
                payResult = 1
                activity.toast("支付成功")
            }
            override fun failed(code: Int, message: String?) {
                dismiss()
                payResult = 2
                activity.toast("$message")
            }
            override fun cancel() {
                dismiss()
                payResult = 3
                activity.toast("支付取消")
            }
        })
    }

    /**
     * 微信支付
     */
    private fun startWeChatPay(data: WeChatPayOrderData){
        val wxPay = WXPay.getInstance()
        val wxPayInfoImpli = WXPayInfoImpli()
        wxPayInfoImpli.apply {
            timestamp = data.timestamp    //时间戳，秒
            sign = data.sign                       //签名
            prepayId = data.prepayid              //预支付交易会话ID
            appid = data.appid
            partnerid = data.partnerid                //商户id
            nonceStr = data.noncestr                 //随机字符串
            packageValue = data.`package`              //扩展字段,暂填写固定值Sign=WXPay
        }
        EasyPay.pay(wxPay,activity,wxPayInfoImpli,object : IPayCallback{
            override fun success() {
                dismiss()
                payResult = 1
                activity.toast("支付成功")
            }

            override fun failed(code: Int, message: String?) {
                dismiss()
                payResult = 2
                activity.toast("$message")
            }

            override fun cancel() {
                dismiss()
                payResult = 3
                activity.toast("支付取消")
            }
        })
    }

    private fun createOrder(payType: Int){
        val userId = SpUtils.getInstance().decodeString(Mapper.ACCOUNT)
        if (paymentData.pricePlanId != null){
            showLoadingPopup()
            val map: MutableMap<String,Any> = mutableMapOf(
                "isMemberGoods" to paymentData.isMemberGoods,
                "isOpenMember" to 1,
                "pricePlanId" to paymentData.pricePlanId,
                "type" to paymentData.type,
                "userId" to userId.toInt()
            )
            BaseApiUtil.getBaseApi().createOrder(map)
                .startHttp({
                    if (it.code == "1"){
                        when(payType){
                            1 -> getAlipaySignOrder("${it.data}")
                            2 -> getWeChatOrderData("${it.data}")
                        }

                    }else {
                        dismissLoadingPopup()
                    }
                },{
                    dismissLoadingPopup()
                })
        }else {
            context.toast("会员id为空")
        }
    }

    private fun showLoadingPopup(){
        popupView?.dismiss()
        popupView = XPopup.Builder(context)
            .hasShadowBg(false)
            .dismissOnTouchOutside(false)
            .asLoading()
            .show()
    }

    private fun dismissLoadingPopup(){
        popupView?.dismiss()
    }

}