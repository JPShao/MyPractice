package com.fscloud.lib_base.ui.adapter.mall

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fscloud.lib_base.R
import com.fscloud.lib_base.imageload.GlideUtil
import com.fscloud.lib_base.model.mall.EntrustServiceData
import com.fscloud.lib_base.utils.servicemall.TextViewShowDealUtil

/**
 * @author shaojunpei
 * @date 2020/11/19 17:42
 * @describe 代办服务列表
 */

class MallListAdapter :
    BaseQuickAdapter<EntrustServiceData, BaseViewHolder>(R.layout.lib_base_mall_item_rc_mall_list) {
    override fun convert(holder: BaseViewHolder, item: EntrustServiceData) {
        val roundImageView = holder.getView<ImageView>(R.id.roundImageView)
        val textTitle = holder.getView<TextView>(R.id.textTitle)
        val textDescribe = holder.getView<TextView>(R.id.textDescribe)
        val textVipMoney = holder.getView<TextView>(R.id.textVipMoney)
        val tvSalesVolume = holder.getView<TextView>(R.id.tvSalesVolume)
        //val textMarketMoney = holder.getView<TextView>(R.id.textMarketMoney)
        val textPlatformMoney = holder.getView<TextView>(R.id.textPlatformMoney)
        textTitle.text = "${item.name}"
        textDescribe.text = "${item.description}"
        if (item.memberPrice != null){
            TextViewShowDealUtil.setShowVipPrice(item.memberPrice.toString(),textVipMoney)
        }
        //textMarketMoney.text = "¥ ${item.marketPrice}"
        textPlatformMoney.text = "平台价 ¥ ${item.platformPrice}"
        //销量
        if (item.goodsSales > 0){
            tvSalesVolume.text = "销量 ${item.goodsSales}"
        }else {
            tvSalesVolume.text = "销量 0"
        }
        //加载图片
        if (!item.coverImg.isNullOrEmpty()){
            GlideUtil.loadImage(context,item.coverImg,roundImageView)
        }
    }


}