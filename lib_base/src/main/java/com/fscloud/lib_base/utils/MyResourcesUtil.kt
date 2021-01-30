package com.fscloud.lib_base.utils

import android.content.ContentResolver
import android.content.res.Resources
import androidx.annotation.DrawableRes

/**
 * @author shaojunpei
 * @date 2020/12/25 16:22
 * @describe  资源文件处理工具类
 */
object MyResourcesUtil {

    /**
     * 获取资源文件路径
     */
    fun getResourcesUriPath(@DrawableRes id: Int, resources: Resources): String {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id)
    }


}