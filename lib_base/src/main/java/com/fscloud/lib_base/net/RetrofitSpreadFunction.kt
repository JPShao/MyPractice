package com.fscloud.lib_base.net

import com.blankj.utilcode.util.LogUtils
import com.fscloud.lib_base.BaseApplication
import com.fscloud.lib_base.model.BaseResponse
import com.fscloud.lib_base.net.exception.ExceptionHandle
import com.google.gson.JsonObject
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author shaojunpei
 * @date 2020/9/21 9:20
 * @describe Retrofit Call 的扩展函数
 */

suspend fun <T: BaseResponse<T>> Call<T>.startHttp(): T {
    return suspendCoroutine {
        enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null){
                    it.resume(body)
                }else {
                    it.resumeWithException(RuntimeException("retrofit response body is null"))
                    LogUtils.e("获取数据失败：response body is null")
                    //BaseApplication.context.toast("获取数据失败")
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(t)
                LogUtils.e("获取数据失败：$t")
                ExceptionHandle.toastExceptionMsg(t)
            }
        })
    }
}

fun <T: BaseResponse<T>> Call<T>.startHttp(onSuccess: ((T) -> Unit),onFailure: (() -> Unit)){
    enqueue(object : Callback<T>{
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val body = response.body()
            if (body != null){
                onSuccess(body)
            }else {
                onFailure()
                LogUtils.e("获取数据失败：response body is null")
                //BaseApplication.context.toast("获取数据失败")
            }
        }
        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure()
            LogUtils.e("获取数据失败：$t")
            ExceptionHandle.toastExceptionMsg(t)
        }
    })
}

fun Call<JsonObject>.startHttpBackSting(onSuccess: ((String) -> Unit),onFailure: (() -> Unit)){
    enqueue(object : Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val body = response.body()
            if (body != null){
                onSuccess(body.toString())
            }else {
                onFailure()
                LogUtils.e("获取数据失败：response body is null")
                //BaseApplication.context.toast("获取数据失败")
            }
        }
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            onFailure()
            LogUtils.e("获取数据失败：$t")
            ExceptionHandle.toastExceptionMsg(t)
        }
    })
}
