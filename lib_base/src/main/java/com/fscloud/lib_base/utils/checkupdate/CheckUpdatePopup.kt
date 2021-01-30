package com.fscloud.lib_base.utils.checkupdate

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.fscloud.lib_base.R
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.db.SpUtils
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.xpopup_app_upgrade.view.*
import java.nio.MappedByteBuffer


/**
 * @author shaojunpei
 * @date 2020/5/4
 * @describe 检查更新的弹窗
 */

class CheckUpdatePopup(val activity: FragmentActivity, val data: CheckUpdateData,
                       private val utilCheckUpdate: UtilCheckUpdate) : CenterPopupView(activity) {

    override fun getImplLayoutId(): Int {
        return R.layout.xpopup_app_upgrade
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        setClickEvent()
    }

    private fun initView(){
        if (data.title.isNotEmpty()){
            textTitle.text = data.title
        }
        data.updateContent.also {
            for (i in it.indices){
                var text = ""
                if (i < it.size - 1){
                    text = it[i] + "\n"
                }else {
                    text = it[i]
                }
                textUpgrade.text = text
            }
        }
        if (data.isForced.forced){
            clickUpdateNow.visibility = View.VISIBLE
            layoutCanNotUpdate.visibility = View.GONE
        }else {
            clickUpdateNow.visibility = View.GONE
            layoutCanNotUpdate.visibility = View.VISIBLE
        }
    }

    private fun setClickEvent(){
        //立即更新
        clickUpdateNow.setOnClickListener {
            startDownload()
        }
        //更新
        clickUpdate.setOnClickListener {
            startDownload()
        }
        //取消
        clickCancel.setOnClickListener {
            SpUtils.getInstance().encode(Mapper.IS_CANCEL_UPDATE,true)
            dismiss()
        }
    }

    private fun startDownload(){
        utilCheckUpdate.startDownload()
        dismiss()
    }


}