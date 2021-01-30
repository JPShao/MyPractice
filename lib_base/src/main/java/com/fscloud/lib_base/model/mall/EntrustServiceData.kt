package com.fscloud.lib_base.model.mall

import java.math.BigDecimal

/**
 * @author shaojunpei
 * @date 2020/11/19 17:41
 * @describe 商城商品列表
 */

data class EntrustServiceModel(
    val data: List<EntrustServiceData>? = null,
    val total: Int = 0
)

data class EntrustServiceData(
    val coverImg: String,
    val description: String,
    val id: Int,
    val marketPrice: BigDecimal = BigDecimal.ZERO,
    val memberPrice: BigDecimal = BigDecimal.ZERO,
    val name: String = "",
    val platformPrice: BigDecimal = BigDecimal.ZERO,
    val goodsSales: Int
)