package com.fscloud.lib_base.utils.checkupdate

/**
 * @author shaojunpei
 * @date 2020/5/4
 * @describe 检查更新数据模型
 */

data class CheckUpdateData(
    val downUrl: String = "",
    val interval: Interval = Interval(),
    val isForced: IsForced = IsForced(),
    val title: String = "",
    val updateContent: List<String> = listOf(),
    val updateTime: String = "",
    val verCode: String = "",
    val verNum: Int = 0
)

data class IsForced(
    val forced: Boolean = false,
    val minVerNum: String = ""
)

data class Interval(
    val everyTime: Boolean = false,
    val intervalTime: Int = 0
)








