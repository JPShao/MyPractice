package com.fscloud.lib_base.utils

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.blankj.utilcode.util.LogUtils
import com.fscloud.lib_base.constant.GlobalConfig
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import id.zelory.compressor.Compressor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import java.io.File

/**
 * @author shaojunpei
 * @date 2020/4/3
 * @describe 阿里云OSS上传文件, type： 0 只传图片，1 修改头像，2上传身份证,
 *           3上传图片或文件后再调接口获取url
 */

class UtilAliyunOSS(val context: Context,var path: String,
                    val uploadSuccess: ((String,String) -> Unit)? = null,
                    val type: Int = 0,val isFile: Boolean = false,
                    val uploadFail: (() -> Unit)? = null) {

    //发起网络请求时显示的弹窗
    var popupView: BasePopupView? = null
    private var isDestroy = false   //页面是否已经销毁

    fun startUpload(){
        var objName = UtilCommon.getUUID() + UtilCommon.getFileNameWithSuffix(path)
        objName = objName.substring(0,objName.lastIndexOf("."))
        objName = "$objName.jpg"
        LogUtils.i("objName: $objName; path: $path")
        showPopupWindow()
        if (isFile){
            startOSSUpload(objName)
        }else {
            compressPicture(objName)
        }
    }

    private fun compressPicture(objName: String){
        GlobalScope.launch {
            val compressFile = Compressor.compress(context, File(path))
            LogUtils.i("压缩前图片大小：${File(path).length()}")
            LogUtils.i("压缩后图片大小：${compressFile.length()}")
            path = compressFile.absolutePath
            startOSSUpload(objName)
        }
    }

    private fun startOSSUpload(objName: String){
        LogUtils.i("上传的路径：$path")
        val oss = initOSS()
        val put = PutObjectRequest(GlobalConfig.bucketName,objName,path)
        put.setProgressCallback(object : OSSProgressCallback<PutObjectRequest>{
            override fun onProgress(
                request: PutObjectRequest?,
                currentSize: Long,
                totalSize: Long
            ) {

            }
        })
        val tast = oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest,PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                context.runOnUiThread {
                    if (type == 0){          //只上传图片
                        popupView?.dismiss()
                        if (uploadSuccess != null){
                            uploadSuccess!!(objName,path)
                        }
                    }else {    //修改头像，上传身份证，图片上传成功后，还调后台接口修改头像,不关闭弹窗
                        if (uploadSuccess != null){
                            uploadSuccess!!(objName,path)
                        }
                    }
                }
            }
            override fun onFailure(request: PutObjectRequest?,
                                   clientException: ClientException?,
                                   serviceException: ServiceException?
            ) {
                if (isDestroy) return
                context.runOnUiThread {
                    popupView?.dismiss()
                    toast("上传失败")
                }
            }
        })
    }

    private fun initOSS(): OSSClient{
        val token = ""
        val conf = ClientConfiguration()
        conf.setConnectionTimeout(15 * 1000)       // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000)           // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5)            // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2)                   // 失败后最大重试次数，默认2次。
        val credentialProvider = OSSStsTokenCredentialProvider(GlobalConfig.accessKeyID, GlobalConfig.accessKeySecret,token)
        val oss = OSSClient(context, GlobalConfig.endpoint,credentialProvider,conf)
        return oss
    }

    private fun showPopupWindow(){
        popupView = XPopup.Builder(context)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(true)
            .asLoading("上传中")
            .show()
    }

    fun dismissPopupView(){
        popupView?.dismiss()
    }

    fun setDestroy(isDestroy: Boolean){
        this.isDestroy = isDestroy
    }

}