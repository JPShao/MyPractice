package com.fscloud.lib_base.ui.pay.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.fscloud.lib_base.R
import com.fscloud.lib_base.model.pay.PaymentData
import com.fscloud.lib_base.imageload.GlideUtil
import com.fscloud.lib_base.router.RouterTable
import com.fscloud.lib_base.ui.BaseActivity
import com.fscloud.lib_base.ui.pay.data.PaidOrderData
import com.fscloud.lib_base.ui.pay.xpopup.OrderPaymentPopup
import com.fscloud.lib_base.utils.AndroidBarUtils
import com.fscloud.lib_base.utils.servicemall.TextViewShowDealUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import kotlinx.android.synthetic.main.lib_base_activity_payment_result.*

/**
 * @author shaojunpei
 * @date 2020/11/19 21:18
 * @describe 订单支付结果
 */
class PaymentResultActivity : BaseActivity() {

    private var paidOrderData: PaidOrderData? = null

    override fun layoutId(): Int {
        return R.layout.lib_base_activity_payment_result
    }

    override fun initData() {
        val bundle = intent.extras
        bundle?.let {
            paidOrderData = it.getParcelable("paidOrderData")
        }
        LogUtils.i("获取的商品数据：$paidOrderData")
    }

    override fun initView() {
        AndroidBarUtils.transparencyBar(this)
        showPaidResult()
        setClickEvent()
    }

    override fun start() {
    }

    private fun showPaidResult(){
        paidOrderData?.let {
            //实付金额
            TextViewShowDealUtil.setShowPrice(it.realPayMoney,textRealMoney,0.35f)
            textTitle.text = it.goodsName
            //商品价格
            if (it.isMemberGoods){  //会员订单
                TextViewShowDealUtil.setShowPrice(it.realPayMoney,textProductMoney,0.55f)
            }else {
                TextViewShowDealUtil.setShowPrice(it.platformPrice.toString(),textProductMoney,0.55f)
            }
            //规格
            if (it.specTypeName.isNullOrEmpty()){
                TextViewShowDealUtil.setTextViewShowSize(it.quantityStr,textSpec,0.55f)
            }else {
                if (it.isMemberGoods){  //会员订单
                    TextViewShowDealUtil.setTextViewShowSize("${it.quantityStr}",
                        textSpec,0.55f)
                }else {
                    TextViewShowDealUtil.setTextViewShowSize("${it.specTypeName},${it.quantityStr}",
                        textSpec,0.55f)
                }
            }
            //商品图片
            GlideUtil.loadImage(this,it.goodsPicture,roundedImageView)
            if (it.payStatus == 1){  //1 支付成功，2 支付失败
                viewBg.isSelected = true
                textPayResult.isSelected = true
                layoutGoodsInfo.isSelected = true
                layoutPaySuccess.visibility = View.VISIBLE
                layoutPayFail.visibility = View.GONE
                textPayResult.text = "订单支付成功！"
            }else {
                viewBg.isSelected = false
                textPayResult.isSelected = false
                layoutGoodsInfo.isSelected = false
                layoutPayFail.visibility = View.VISIBLE
                layoutPaySuccess.visibility = View.GONE
                textPayResult.text = "订单支付失败"
            }
        }
    }

    private fun setClickEvent(){
        //返回
        ivBack.setOnClickListener {
            finish()
        }
        //查看订单
        textLookOrder.setOnClickListener {

        }
        textLookOrder2.setOnClickListener {

        }
        //在线咨询客服
        textTelService.setOnClickListener {
            val telNumber = resources.getString(R.string.lib_base_service_tel)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$telNumber")
            startActivity(intent)
        }
        //重新支付
        textRepayment.setOnClickListener {
            showPaymentPopup()
        }
    }

    //立即支付弹窗
    private fun showPaymentPopup(){
        paidOrderData?.let {
            val paymentData = PaymentData(
                it.type,it.goodsName,it.specTypeName,it.quantityStr,
                it.realPayMoney,it.tradeNo,it.signOrder,null,it.isMemberGoods
            )
            val popup = OrderPaymentPopup(this, paymentData)
            XPopup.Builder(this)
                .setPopupCallback(object : SimpleCallback(){
                    override fun onDismiss(popupView: BasePopupView?) {
                        super.onDismiss(popupView)
                        when(popup.payResult){
                            1 -> {  //支付成功
                                it.payStatus = 1
                                showPaidResult()
                            }
                            2 -> {}  //支付失败
                            3 -> {}  //支付取消
                        }
                    }
                })
                .asCustom(popup)
                .show()
        }
    }

}

















