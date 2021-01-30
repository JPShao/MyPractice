package com.fscloud.lib_base.model

/**
 * @author shaojunpei
 * @date 2020/11/2 17:11
 * @describe
 */
data class EnterpriseData(
    val creditCode: String = "",
    val businessAddress: String = "",
    val currentAuditRoleId: Int = 0,
    val entName: String = "",
    val id: Int = 0,
    val inTrayState: Int = 0,
    val inTrayStateString: String = "",
    val isAudit: Int = 0,
    val personName: String = "",
    val personPhone: String = "",
    val sourceType: Int = 0,
    val sourceTypeString: String = "",
    val tradeName: String = "",
    var selected: Boolean = false,
    var isRecordSuccess: Boolean = false    //备案成功后，通知首页刷新企业列表
)
