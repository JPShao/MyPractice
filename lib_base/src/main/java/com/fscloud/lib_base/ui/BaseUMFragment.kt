package com.fscloud.lib_base.ui

import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.umeng.analytics.MobclickAgent
import org.jetbrains.anko.support.v4.act

/**
 * @ProjectName :
 * @author MrChen
 * @date :2021/1/25 13:51
 * @description: 友盟统计fragment
 */
open class BaseUMFragment : Fragment() {

    companion object {
        private const val TAG = "BaseUMFragment"

    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("${javaClass.simpleName}"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd("${javaClass.simpleName}");
    }

}