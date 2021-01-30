package com.fscloud.lib_base.view.address

/**
 * Description:
 */
interface OnItemClickListener {
    /**
     * @param city 返回地址列表对应点击的对象
     * @param tabPosition 对应tab的位置
     */
    fun itemClick(
        addressSelector: AddressSelector?,
        city: CityInterface?,
        tabPosition: Int,
        selectPosition: Int
    )
}