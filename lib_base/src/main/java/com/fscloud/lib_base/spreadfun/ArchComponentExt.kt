package com.fscloud.lib_base.spreadfun

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.fscloud.lib_base.model.BaseResponse
import retrofit2.Call
import java.lang.Exception

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observeLiveData(
    liveData: LiveData<Result<BaseResponse<out Any>>>,
    action: (t: T?) -> Unit
) {
    liveData.observe(this, Observer { result ->
        val data = result.getOrNull()
        data?.let {
            if (data.code == "1") {
                when (data.data) {
                    null -> {
                        action(null)
                    }
                    else -> {
                        var mData:T? = null
                        try {
                             mData = data.data as T
                        }catch (e:Exception){
                            LogUtils.e("observeLiveData()","类型转换异常${e.message}")
                            mData = null
                        }finally {
                            action(mData)
                        }
                    }
                }
            } else {
                return@let
            }
        }

    })
}





//fun <T> LifecycleOwner.observeEvent(liveData: LiveData<SingleEvent<T>>, action: (t: SingleEvent<T>) -> Unit) {
//    liveData.com.fscloud.lib_base.spreadfun.observe(this, Observer { it?.let { t -> action(t) } })
//}
