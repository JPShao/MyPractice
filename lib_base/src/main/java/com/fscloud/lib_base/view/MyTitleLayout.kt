package com.fscloud.lib_base.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.fscloud.lib_base.R
import kotlinx.android.synthetic.main.lib_base_layout_title.view.*


/**
 * @author shaojunpei
 * @date 2020/9/22
 * @describe 通用activity标题栏布局
 */

class MyTitleLayout : RelativeLayout {

    lateinit var titleRight: TextView
    private var myOnClickListen: MyOnClickListen? = null

    constructor(context: Context) : super(context) {
        inflateView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflateView(context)
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MyTitleLayout)
        val centerText = typedArray.getString(R.styleable.MyTitleLayout_center_title)
        val rightText = typedArray.getString(R.styleable.MyTitleLayout_right_title)
        val isBold = typedArray.getBoolean(R.styleable.MyTitleLayout_is_bold,true)
        val isHideBack = typedArray.getBoolean(R.styleable.MyTitleLayout_is_hide_back,false)
        val colorBg = typedArray.getColor(R.styleable.MyTitleLayout_colorBg, Color.WHITE)
        val colorRightTitle = typedArray.getColor(R.styleable.MyTitleLayout_color_right_title,Color.parseColor("#0052FE"))
        val centerTitleColor = typedArray.getColor(R.styleable.MyTitleLayout_center_title_color,Color.parseColor("#333333"))
        val iconLeft = typedArray.getResourceId(R.styleable.MyTitleLayout_left_icon,R.mipmap.lib_base_icon_back)
        centerText?.let {
            centerTitle.text = it
        }
        rightText?.let {
            rightTitle.text = it
        }
        //标题字体是否加粗
        if (isBold){
            val paint = centerTitle.paint
            paint.isFakeBoldText = true
        }
        //是否隐藏返回按钮
        if (isHideBack){
            clickBack.visibility = View.INVISIBLE
        }
        layout.setBackgroundColor(colorBg)
        rightTitle.setTextColor(colorRightTitle)
        clickBack.setImageResource(iconLeft)
        centerTitle.setTextColor(centerTitleColor)
    }

    private fun inflateView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.lib_base_layout_title, this, true)
        titleRight = rightTitle
        setClickEvent(context)
    }

    private fun setClickEvent(context: Context) {
        clickBack.setOnClickListener {
            if (myOnClickListen != null) {
                myOnClickListen!!.onClick()
            } else {
                (getContext() as Activity).finish()
            }
        }
    }

    fun setTitle(text: String) {
        centerTitle.text = text
    }

    fun setBackClickEvent(myOnClickListen: MyOnClickListen) {
        this.myOnClickListen = myOnClickListen
    }
}