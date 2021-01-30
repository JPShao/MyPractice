package com.fscloud.lib_base.ui.xpopup

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.fscloud.lib_base.R
import com.fscloud.lib_base.router.RouterTable
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.lib_base_xpopup_auth.view.*

/**
 * @author shaojunpei
 * @date 2020/12/8 13:50
 * @describe isElectronic 表示是否是在申领电子证照页面使用
 */
class AuthPopup(mContext: Context,val isElectronic: Boolean = false): CenterPopupView(mContext) {

    override fun getImplLayoutId(): Int {
        return R.layout.lib_base_xpopup_auth
    }

    override fun onCreate() {
        super.onCreate()
        setClickEvent()
    }

    private fun setClickEvent(){
        textCancel.setOnClickListener {
            dismiss()
        }
        //去实名认证
        textAuth.setOnClickListener {
            if (isElectronic){

            }else {

            }
            dismiss()
        }
    }

}