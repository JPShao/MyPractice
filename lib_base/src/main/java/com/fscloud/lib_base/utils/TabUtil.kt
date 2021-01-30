package com.fscloud.lib_base.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.androidkun.xtablayout.XTabLayout
import com.blankj.utilcode.util.ConvertUtils

/**
 * @author shaojunpei
 * @date 2021/1/27 16:47
 * @describe
 */
object TabUtil {

    fun setTabLayoutCustomView(tab: XTabLayout.Tab, text: String, context: Context){
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        val layoutParam = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParam
        textView.setTextColor(Color.parseColor("#666666"))
        val padding = ConvertUtils.dp2px(8f)
        textView.setPadding(padding,0,padding,0)
        textView.text = text
        tab.customView = textView
    }

}