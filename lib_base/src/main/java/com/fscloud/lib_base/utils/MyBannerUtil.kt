package com.fscloud.lib_base.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.fscloud.lib_base.R
import com.fscloud.lib_base.model.banner.BannerModel
import com.fscloud.lib_base.ui.webview.H5Activity
import com.fscloud.lib_base.umstatistics.UmClickKey
import com.fscloud.lib_base.umstatistics.UmEventUtils
import com.google.gson.Gson
import com.youth.banner.Banner
import com.youth.banner.indicator.RectangleIndicator

/**
 * @author shaojunpei
 * @date 2021/1/19 16:38
 * @describe bannner 工具类
 */
object MyBannerUtil {

    fun setBannerIndicator(banner: Banner<*,*>,
                           mIndicator: RectangleIndicator, context: Context){
        banner.also {
            //在布局文件中使用指示器，这样更灵活
            it.setIndicator(mIndicator, false)
            it.setIndicatorWidth(SizeUtils.dp2px(3f), SizeUtils.dp2px(8f))
            it.setIndicatorSelectedColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_0052FE
                )
            )
            it.setIndicatorNormalColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_CCCCCCFF
                )
            )
            it.setIndicatorHeight(SizeUtils.dp2px(3f))
            it.setIndicatorSpace(SizeUtils.dp2px(4f))
            it.setIndicatorRadius(SizeUtils.dp2px(1.5f))
        }
    }

    /**
     * 点击banner跳转
     */
    fun bannerSkip(context: Context,banner: BannerModel){

        LogUtils.i("点击跳转的banner: $banner")
        if (banner.url.isNotEmpty() && banner.url.contains("http")){
            UmEventUtils.onEventById(UmClickKey.BANNER_BY_ID,Gson().toJson(banner))
            H5Activity.launchActivity(context,banner.adName,banner.url)
        }
    }

}