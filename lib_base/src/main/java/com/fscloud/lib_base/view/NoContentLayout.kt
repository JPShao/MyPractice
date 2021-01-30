package com.fscloud.lib_base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.fscloud.lib_base.R

/**
 * @author shaojunpei
 * @data 2020/11/17 10:44
 * @describe 没有内容时，显示的布局
 */

class NoContentLayout : RelativeLayout{

    constructor(context: Context): super(context){
        initView(context)
    }
    constructor(context: Context,attributeSet: AttributeSet): super(context,attributeSet){
        initView(context)
    }

    private fun initView(context: Context){
        LayoutInflater.from(context).inflate(R.layout.lib_base_layout_no_content,this,true)

    }


}