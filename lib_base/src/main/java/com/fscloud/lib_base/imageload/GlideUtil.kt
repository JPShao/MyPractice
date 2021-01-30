package com.fscloud.lib_base.imageload

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.*
import com.fscloud.lib_base.R
import com.lxj.xpopup.XPopup
import kotlin.concurrent.thread

/**
 * 图片加载工具类
 */
object GlideUtil {
    /**
     * Crop
     */
    fun loadCrop(context: Context, url: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(CropTransformation(width, height)))
            .into(imageView)
    }

    fun loadSimple(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }

    fun loadCropCircle(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url)
            .circleCrop()
            .into(imageView)
    }

    fun loadRoundedCorners(
        context: Context,
        url: Int,
        imageView: ImageView,
        radius: Int,
        margin: Int
    ) {
        val optionsRounded = RequestOptions().transform(
            RoundedCornersTransformation(
                radius,
                margin,
                RoundedCornersTransformation.CornerType.ALL
            )
        )
        Glide.with(context).load(url)
            .apply(optionsRounded)
            .into(imageView)
    }

    fun loadRoundedCorners(
        context: Context,
        url: String,
        imageView: ImageView,
        radius: Int,
        margin: Int
    ) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin)))
            .into(imageView)
    }

    fun loadCropSquare(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(CropSquareTransformation()))
            .into(imageView)
    }

    fun load(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(imageView)
    }

    /**
     * Color
     */
    fun loadColorFilter(context: Context, url: String, imageView: ImageView, color: Int) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(ColorFilterTransformation(color)))
            .into(imageView)
    }

    /**
     * 加载灰度变换
     */
    fun loadGrayscale(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(GrayscaleTransformation()))
            .into(imageView)
    }

    /**
     *   Blur
     */
    fun loadBlur(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(imageView)
    }

    /**
     * mask
     */
    fun loadMask(context: Context, url: String, imageView: ImageView, maskId: Int) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(MaskTransformation(maskId)))
            .into(imageView)
    }

    fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).asDrawable().load(url)
            .apply(GlideConfig.generalRequestOptions)
            .into(imageView)
    }

    /**
     * 加载vip图片
     */
    fun loadImageForVip(context: Context, url: String, imageView: ImageView) {
        val requestOptions = RequestOptions()
            .placeholder(R.mipmap.lib_base_bg_vip)
            .error(R.mipmap.lib_base_bg_vip)
        Glide.with(context).asDrawable().load(url)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 用于加载视频封面
     */
    fun loadImageForVideoCover(context: Context, url: String, imageView: ImageView) {
        val requestOptions = RequestOptions()
            .placeholder(R.color.color_000000)
            .error(R.color.color_000000)
        Glide.with(context).asDrawable().load(url)
            .apply(requestOptions)
            .into(imageView)
    }

    /**
     * 对图片进行灰度处理
     */
    fun loadImageGrey(context: Context, resourceId: Int, imageView: ImageView) {
        Glide.with(context).load(resourceId)
            .transform(GrayscaleTransformation())
            .into(imageView)
    }

    /**
     * 点击图片查看大图
     */
    fun viewBigPicture(context: Context, imageView: ImageView, url: String) {
        if (url.isNotEmpty() && url.contains("http")) {
            XPopup.Builder(context)
                .asImageViewer(imageView, url, ImageLoader(context))
                .isShowSaveButton(false)
                .show()
        }
    }

    /**
     * 磁盘缓存照片
     */
    fun loadImageByFileCache(context: Context, imageView: ImageView, url: String) {
        GlideApp.with(context)
            .load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(imageView);
    }

    /**
     * 清理内存缓存
     */
    fun cleanMemoryCache(context: Context) {
        //内存缓存清理（主线程）
        GlideApp.get(context).clearMemory();
    }

    /**
     * 清理磁盘缓存
     */
    fun cleanDiskCache(context: Context) {
        //磁盘缓存清理（子线程
        thread {
            GlideApp.get(context).clearDiskCache();
        }

    }

    fun cleanAllCache(context: Context) {
        cleanMemoryCache(context)
        cleanDiskCache(context)
    }

}