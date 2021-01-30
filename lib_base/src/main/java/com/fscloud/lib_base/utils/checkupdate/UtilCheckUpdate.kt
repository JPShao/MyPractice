package com.fscloud.lib_base.utils.checkupdate

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.fscloud.lib_base.api.BaseApiUtil
import com.fscloud.lib_base.constant.GlobalVariable
import com.lxj.xpopup.XPopup
import org.jetbrains.anko.toast
import java.io.File
import com.fscloud.lib_base.net.startHttp
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import org.jetbrains.anko.longToast
import java.lang.IndexOutOfBoundsException

/**
 * @author shaojunpei
 * @date 2020/5/4
 * @describe 检查更新，
 * 参数isCheck我的模块的设置页面 点击检查更新使用，设置为true,view表示检查更新的布局
 */

class UtilCheckUpdate(val activity: FragmentActivity,
                      private val isCheck: Boolean = false,
                      val view: View? = null) {

    private var url = ""
    private var checkUpdatePopup: CheckUpdatePopup? = null

    fun checkUpdate(callBack: CheckUpdateCallBack? = null){
        BaseApiUtil.getBaseApi()
            .checkUpdate("android")
            .startHttp({ baseResponse ->
                if (baseResponse.code == "1"){
                    baseResponse.data?.let {
                        if (callBack == null){
                            url = it.downUrl
                            checkData(it)
                        }else {
                            url = it.downUrl
                            if (isNeedUpdate(it.verCode)){
                                callBack.onCheckResult(true)
                            }else {
                                callBack.onCheckResult(false)
                            }
                        }
                    }
                }
            },{
                view?.isEnabled = true  //检查跟新按钮可点击
            })
    }

    private fun checkData(data: CheckUpdateData){
        if (isNeedUpdate(data.verCode)){
            showUpdatePopup(data)
        }else {
            if (isCheck){ // 点击检查更新,提醒用户
                activity.toast("已是最新版本")
                view?.isEnabled = true  //检查跟新按钮可点击
            }
        }
    }

    private fun isNeedUpdate(newVersionName: String): Boolean{
        val oldVersionName = AppUtils.getAppVersionName()
        val oldVersion = oldVersionName.split(".")
        val newVersion = newVersionName.split(".")
        try {
            if (oldVersion[0].toInt() > newVersion[0].toInt()){    //旧主版本大于线上的主版本
                return false
            }else if (oldVersion[0].toInt() < newVersion[0].toInt()){    //主版本号
                return true
            }else if (oldVersion[1].toInt() > newVersion[1].toInt()){    //旧子版本大于线上的主版本
                return false
            }else if (oldVersion[1].toInt() < newVersion[1].toInt()){    //子版本号
                return true
            }else return (oldVersion[2].toInt() < newVersion[2].toInt())   //第三位版本号
        }catch (e: IndexOutOfBoundsException){
            return false
        }
    }

    private fun showUpdatePopup(data: CheckUpdateData){
        if (GlobalVariable.isCancelUpdate){  //用户取消了更新，不在提示
            return
        }
        checkUpdatePopup?.dismiss()
        checkUpdatePopup = CheckUpdatePopup(activity,data,this)
        XPopup.Builder(activity)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(checkUpdatePopup)
            .show()
    }

    fun startDownload(){
        val popup = UpdateProgressPopup(activity)
        val fileName = "${System.currentTimeMillis()}" + "app.apk"
        val file = File(activity.cacheDir,fileName)
        LogUtils.i("下载链接：$url")
        showDownloadingPopup(popup)
        FileDownloader.setup(activity)
        FileDownloader.getImpl().create(url)
            .setPath(file.absolutePath)
            .setListener(object : FileDownloadListener(){
                override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {}
                override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {}
                override fun warn(task: BaseDownloadTask?) {}
                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    val percent = (soFarBytes.toLong()*100)/totalBytes.toLong()
                    popup.setProgress(percent.toInt())
                }
                override fun completed(task: BaseDownloadTask?) {
                    popup.setProgress(100)
                    installApk(file)
                    popup.dismiss()
                }
                override fun error(task: BaseDownloadTask?, e: Throwable?) {
                    LogUtils.e("下载失败: $e")
                    popup.dismiss()
                    activity.longToast("下载失败：${e?.message}")
                }
            })
            .start()
    }

    private fun showDownloadingPopup(popup: UpdateProgressPopup){
        XPopup.Builder(activity)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .asCustom(popup)
            .show()
    }

    private fun installApk(file: File){
        AppUtils.installApp(file)
    }

}

interface CheckUpdateCallBack{
    fun onCheckResult(isNeedUpdate: Boolean)
}