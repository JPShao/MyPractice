package com.fscloud.lib_base.umstatistics

import android.content.Context
import com.umeng.analytics.MobclickAgent

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/26 16:35
 * @description:
 */
object UmEventUtils {
   private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }


    fun onEvent(key: String) {
        context?.let {
            MobclickAgent.onEvent(context,key)
        }
    }

    fun onEventById(key: String,id: String) {
        context?.let {
            MobclickAgent.onEvent(context,key,id)
        }
    }

    fun onEventByObject(key: String, map: MutableMap<String,Any>) {
        context?.let {
            MobclickAgent.onEventObject(context, key, map)
        }
    }


}