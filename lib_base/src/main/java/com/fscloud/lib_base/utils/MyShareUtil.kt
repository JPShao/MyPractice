package com.fscloud.lib_base.utils

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.DrawableRes

/**
 * @author shaojunpei
 * @date 2020/12/25 16:37
 * @describe  分享工具类
 */
object MyShareUtil {

    fun sharePictureToWeChat(activity: Activity,@DrawableRes id: Int){
        val bitmap = BitmapFactory.decodeResource(activity.resources,id)
        val uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.contentResolver,bitmap,"",""))
        val intent = Intent(Intent.ACTION_SEND)
        intent.setPackage("com.tencent.mm")
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        activity.startActivity(Intent.createChooser(intent, "分享"))
    }

    fun sharePictureToQQ(activity: Activity,@DrawableRes id: Int){
        val bitmap = BitmapFactory.decodeResource(activity.resources,id)
        val uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.contentResolver,bitmap,"",""))
        val intent = Intent(Intent.ACTION_SEND)
        intent.setPackage("com.tencent.mobileqq")
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        activity.startActivity(Intent.createChooser(intent, "分享"))
    }

}