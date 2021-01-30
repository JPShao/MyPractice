package com.fscloud.lib_base.utils.servicemall

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils

/**
 * @author shaojunpei
 * @date 2020/11/20 14:25
 * @describe  价格显示处理
 */
object TextViewShowDealUtil {

    fun setShowVipPrice(price: String,textVipMoney: TextView){
        if (price.isNotEmpty()) {
            val content = "会员价 ¥${price}"
            val span = SpannableString(content)
            val length = content.length
            val spanSize1 = RelativeSizeSpan(0.5f)
            val spanSize2 = RelativeSizeSpan(1f)
            span.setSpan(spanSize1, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            span.setSpan(spanSize2, 5, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            textVipMoney.text = span
        }
    }

    fun setShowTotalPrice(price: String,textTotalMoney: TextView){
        LogUtils.i("setShowTotalPrice 金额：$price")
        if (price.isNotEmpty()) {
            val content = "¥${price}"
            val span = SpannableString(content)
            val length = content.length
            val spanSize1 = RelativeSizeSpan(0.4f)
            val spanSize2 = RelativeSizeSpan(1f)
            span.setSpan(spanSize1, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            span.setSpan(spanSize2, 1, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            textTotalMoney.text = span
        }
    }

    fun setTextViewHalfSize(content: String,textView: TextView){
        if (content.isNotEmpty()) {
            val span = SpannableString(content)
            val length = content.length
            val spanSize1 = RelativeSizeSpan(0.5f)
            span.setSpan(spanSize1, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.text = span
        }
    }

    fun setShowPrice(price: String,textView: TextView,proportion: Float){
        if (price.isNotEmpty()) {
            val content = "¥${price}"
            val span = SpannableString(content)
            val length = content.length
            val spanSize1 = RelativeSizeSpan(proportion)
            val spanSize2 = RelativeSizeSpan(1f)
            span.setSpan(spanSize1, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            span.setSpan(spanSize2, 1, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.text = span
        }
    }

    fun setTextViewShowSize(content: String,textView: TextView,proportion: Float){
        if (content.isNotEmpty()) {
            val span = SpannableString(content)
            val length = content.length
            val spanSize1 = RelativeSizeSpan(proportion)
            span.setSpan(spanSize1, 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.text = span
        }
    }

}