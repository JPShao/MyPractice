package com.fscloud.lib_base.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.db.SpUtils
import com.fscloud.lib_base.model.EnterpriseData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.jetbrains.anko.toast
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author shaojunpei
 * @date 2020/12/02 17:16
 * @describe
 */

object UtilCommon {

    /**
     * h5链接拼接enterpriseId 和 token
     */
    fun jointEnterpriseIdAndToken(url: String, context: Context): String{
        var newUrl = ""
        var enterpriseId = ""
        val token = SpUtils.getInstance().decodeString(Mapper.TOKEN)
        val enterprise = SpUtils.getInstance().decodeString(Mapper.ENTERPRISE)
        try {
            val enterpriseData = Gson().fromJson(enterprise, EnterpriseData::class.java)
            if (enterpriseData != null){
                enterpriseId = enterpriseData.id.toString()
                newUrl = "$url?id=$enterpriseId&token=$token"
            }else {
                context.toast("没有对应的企业")
            }
        }catch (e: JsonSyntaxException){
            e.printStackTrace()
            context.toast("数据解析错误")
        }
        return newUrl
    }

    /**
     * 获取文件名及后缀
     */
    fun getFileNameWithSuffix(path: String): String {
        if (path.isNullOrEmpty()) {
            return ""
        }
        val start = path.lastIndexOf("/")
        return if (start != -1) {
            path.substring(start + 1)
        } else {
            ""
        }
    }

    /**
     * 是否是Q版本以上
     */
    fun isAndroidQ(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 获取唯一UUID, 用于图片名
     */
    fun getUUID(): String{
        return UUID.randomUUID().toString().replace("-","")
    }

    /**
     * 替换身份证号的中间位为星号，只留开始和结束两位
     */
    fun dealIdCard(idCard: String): String{
        return if (idCard.length == 18) {
            idCard.replaceRange(2, 16, "**************")
        }else {
            idCard
        }
    }

    /**
     * 将电话号码的中间位设为*
     */
    fun dealPhone(phone: String): String{
        return when (phone.length) {
            11 -> {
                phone.replaceRange(3, 7, "****")
            }
            else -> {
                phone
            }
        }
    }

    /**
     * 验证号码 手机号 固话均可
     */

    /**
     * 将电话号码设置为131 0000 0000格式
     */
    fun dealPhone_344(phone: String): String {
        return when (phone.length) {
            11 -> {
                val buffer = StringBuffer(phone)
                buffer.insert(3, " ")
                buffer.insert(8, " ")
                buffer.toString()
            }
            else -> {
                phone
            }
        }
    }


    /*
	 * 验证号码 手机号 固话均可
	 *
	 */
    fun isPhoneNumber(phoneNumber: String): Boolean {
        var isValid = false
        if(phoneNumber.isNotEmpty()&&
            (phoneNumber.first() == '1'||phoneNumber.first() == '0')
            &&phoneNumber.length<12&&phoneNumber.length>6){
            isValid = true
        }
        return isValid
    }

    /**
     * 网络请求后数据解析失败提示
     */
    fun dataExceptionToast(context: Context) {
        context.toast("数据解析异常")
    }

    /**
     * 本地资源文件转url
     */
    fun imageTranslateUri(context: Context, resId: Int): String {
        val r = context.resources
        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(resId) + "/"
                    + r.getResourceTypeName(resId) + "/"
                    + r.getResourceEntryName(resId)
        )
        return uri.toString()
    }

}