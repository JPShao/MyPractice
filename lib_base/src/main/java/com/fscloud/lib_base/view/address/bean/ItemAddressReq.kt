package com.fscloud.lib_base.view.address.bean

import com.fscloud.lib_base.view.address.CityInterface

class ItemAddressReq : CityInterface {
    /**
     * areaAbbName : 葵青
     * areaCode : 810308
     * areaEnName : KUI QING DISTRICT
     * areaName : 葵青区
     * areaType : 3
     * areaZip : 810308
     * id : 3720
     * parentId : 3712
     */
    var areaAbbName: String? = null
    var areaCode: String? = null
    var areaEnName: String? = null
    var areaName: String? = null
    var areaType: String? = null
    var areaZip: String? = null
    var id: String? = null
    var parentId: String? = null
    override fun cityName(): String? {
        return areaName
    }
}