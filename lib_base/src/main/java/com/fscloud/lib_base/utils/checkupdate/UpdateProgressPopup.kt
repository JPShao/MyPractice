package com.fscloud.lib_base.utils.checkupdate

import android.content.Context
import com.fscloud.lib_base.R
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.xpopup_app_upgrade_progress.view.*


/**
 * @author shaojunpei
 * @date 2020/5/4
 * @describe 检查更新的弹窗
 */

class UpdateProgressPopup(mContext: Context) : CenterPopupView(mContext) {

    override fun getImplLayoutId(): Int {
        return R.layout.xpopup_app_upgrade_progress
    }

    override fun onCreate() {
        super.onCreate()
        setSeekBar()
        setClickEvent()
    }

    fun setProgress(percent: Int){
        textPercent.text = "${percent}%"
        seekBar.progress = percent
    }

    private fun setSeekBar(){
        seekBar.isEnabled = false //不可点击
        seekBar.setPadding(0, 0, 0, 0)
    }

    private fun setClickEvent(){

    }


}