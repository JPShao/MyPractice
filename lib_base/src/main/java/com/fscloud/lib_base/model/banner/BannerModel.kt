package com.fscloud.lib_base.model.banner

/**
 * @author shaojunpei
 * @date 2021/1/21 12:39
 * @describe
 */

data class BannerModel(
    val adType: String,
    val adName: String,
    val adPic: String,
    val url: String,
    val iconRes: Int?,
    val id:Int
)

object BannerType{
    const val banner1 = "banner1"
    const val banner2 = "banner2"
}