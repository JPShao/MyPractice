package com.fscloud.lib_base.ui.adapter.banner

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.fscloud.lib_base.R
import com.fscloud.lib_base.imageload.GlideUtil
import com.fscloud.lib_base.model.banner.BannerModel
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.youth.banner.adapter.BannerAdapter

/**
 * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 * url图片资源
 */

class BannerAdapterForUrl(val images: List<BannerModel>?,
                          val context: Context,
                          private val isRadius: Boolean = false) :
    BannerAdapter<BannerModel, BannerAdapterForUrl.BannerViewHolder>(images) {

    val data: List<BannerModel> get() = images ?: listOf()

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        if (isRadius){
            val imageView = QMUIRadiusImageView(parent.context)
            imageView.cornerRadius = SizeUtils.dp2px(8f)
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            return BannerViewHolder(imageView)
        }else {
            val imageView = ImageView(parent.context)
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            return BannerViewHolder(imageView)
        }
    }

    override fun onBindView(holder: BannerViewHolder, data: BannerModel, position: Int, size: Int) {
        if (data.adPic.isNotEmpty()){
            GlideUtil.loadImage(context,data.adPic,holder.imageView)
        }else {
            holder.imageView.setImageResource(data.iconRes ?: R.mipmap.lib_base_icon_banner_default)
        }

    }


    inner class BannerViewHolder(@NonNull view: ImageView) :
        RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view
    }
}
