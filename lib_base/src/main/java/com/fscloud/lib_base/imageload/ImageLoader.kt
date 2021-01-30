package com.fscloud.lib_base.imageload

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

/**
 * @author shaojunpei
 * @date 2020/12/2
 * @describe 点击图片查看大图时使用，Xpopup的查看大图的图片加载器
 */

class ImageLoader(val context: Context) : XPopupImageLoader {
    override fun loadImage(position: Int, uri: Any, imageView: ImageView) {
        Glide.with(context).load(uri)
            .apply(GlideConfig.generalRequestOptions)
            .override(Target.SIZE_ORIGINAL)
            .into(imageView)
    }
    override fun getImageFile(context: Context, uri: Any): File? {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}