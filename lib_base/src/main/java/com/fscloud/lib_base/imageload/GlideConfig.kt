package com.fscloud.lib_base.imageload

import com.bumptech.glide.request.RequestOptions
import com.fscloud.lib_base.R


/**
 * @author shaojunpei
 * @date 2020/12/2
 */

object GlideConfig{

    val generalRequestOptions = RequestOptions()
        .placeholder(R.drawable.lib_base_image_load_placeholder)
        .error(R.drawable.lib_base_image_load_error)


}