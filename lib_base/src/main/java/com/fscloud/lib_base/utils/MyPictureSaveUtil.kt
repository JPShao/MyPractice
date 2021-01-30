package com.fscloud.lib_base.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.blankj.utilcode.util.ToastUtils
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream


/**
 * @author shaojunpei
 * @date 2020/12/25 17:05
 * @describe  图片存储工具类
 */
object MyPictureSaveUtil {

    fun saveImageToAlbum(context: Context, fileName: String?, file: File) {
        val fis = FileInputStream(file)
        val bitmap = BitmapFactory.decodeStream(fis)
        try { //设置保存参数到ContentValues中
            val contentValues = ContentValues()
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
            //兼容Android Q和以下版本
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                 //RELATIVE_PATH是相对路径不是绝对路径
                 //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                 contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                 //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
             } else {
                 contentValues.put(
                     MediaStore.Images.Media.DATA,
                     Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                 )
             }*/
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG")
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            val uri: Uri? = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            if (uri != null) { //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    ToastUtils.showShort("图片已保存至相册")
                }else {
                    ToastUtils.showShort("图片保存失败")
                }
            }else {
                ToastUtils.showShort("图片保存失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("图片保存失败")
        }
    }

    fun saveImageToAlbum(context: Context, fileName: String, bitmap: Bitmap) {
        try { //设置保存参数到ContentValues中
            val contentValues = ContentValues()
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName)
            //兼容Android Q和以下版本
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                 //RELATIVE_PATH是相对路径不是绝对路径
                 //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                 contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                 //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
             } else {
                 contentValues.put(
                     MediaStore.Images.Media.DATA,
                     Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                 )
             }*/
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG")
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            val uri: Uri? = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            if (uri != null) { //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    ToastUtils.showShort("图片已保存至相册")
                }else {
                    ToastUtils.showShort("图片保存失败")
                }
            }else {
                ToastUtils.showShort("图片保存失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("图片保存失败")
        }
    }

}