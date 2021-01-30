package com.fscloud.lib_base.model.college

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author shaojunpei
 * @date 2020/12/17 17:58
 * @describe
 */

@Parcelize
data class VideoData(
    val cover: String,
    val id: Int,
    val title: String,
    val url: String,
    var isSelected: Boolean
): Parcelable

data class VideoChapterData(
    val children: List<VideoChapterData>,
    val id: Int,
    val level: Int,
    val pid: Int,
    val title: String
)

