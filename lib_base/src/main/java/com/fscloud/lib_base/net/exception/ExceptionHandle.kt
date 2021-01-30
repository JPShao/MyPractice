package com.fscloud.lib_base.net.exception

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParseException
import org.json.JSONException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

object ExceptionHandle {

    private val TAG = this::class.java.canonicalName
    private var errorCode = ErrorCode.UNKNOWN_ERROR
    private var errorMsg = "请求失败，请稍后重试"

    private fun handleException(e: Throwable): String {
        e.printStackTrace()
        if (e is SocketTimeoutException) {//网络超时
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is ConnectException) { //均视为网络错误
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {   //均视为解析错误
            Timber.e(TAG, "数据解析异常: " + e.message)
            errorMsg = "数据解析异常"
            errorCode = ErrorCode.SERVER_ERROR
        } else if (e is ApiException) {//服务器返回的错误信息
            errorMsg = e.message.toString()
            errorCode = ErrorCode.SERVER_ERROR
        } else if (e is UnknownHostException) {
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is IllegalArgumentException) {
            errorMsg = "参数错误"
            errorCode = ErrorCode.SERVER_ERROR
        } else {//未知错误
            try {
                Timber.e(TAG, "错误: " + e.message)
            } catch (e1: Exception) {
                Timber.e(TAG, "未知错误Debug调试 ")
            }
            errorMsg = "获取数据失败"
            errorCode = ErrorCode.UNKNOWN_ERROR
            if (!e.message.isNullOrEmpty() && e.message!!.contains("401")){
                errorMsg = "请重新登录"
            }
        }
        return errorMsg
    }

    private fun handleException(e: Exception): String {
        e.printStackTrace()
        if (e is SocketTimeoutException) {//网络超时
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is ConnectException) { //均视为网络错误
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {   //均视为解析错误
            Timber.e(TAG, "数据解析异常: " + e.message)
            errorMsg = "数据解析异常"
            errorCode = ErrorCode.SERVER_ERROR
        } else if (e is ApiException) {//服务器返回的错误信息
            errorMsg = e.message.toString()
            errorCode = ErrorCode.SERVER_ERROR
        } else if (e is UnknownHostException) {
            Timber.e(TAG, "网络连接异常: " + e.message)
            errorMsg = "网络连接异常"
            errorCode = ErrorCode.NETWORK_ERROR
        } else if (e is IllegalArgumentException) {
            errorMsg = "参数错误"
            errorCode = ErrorCode.SERVER_ERROR
        } else {//未知错误
            try {
                Timber.e(TAG, "错误: " + e.message)
            } catch (e1: Exception) {
                Timber.e(TAG, "未知错误Debug调试 ")
            }
            errorMsg = "获取数据失败"
            errorCode = ErrorCode.UNKNOWN_ERROR
            if (!e.message.isNullOrEmpty() && e.message!!.contains("401")){
                errorMsg = "请重新登录"
            }
        }
        return errorMsg
    }

    fun toastExceptionMsg(e: Exception){
        val errMsg = handleException(e)
        ToastUtils.showShort(errMsg)
    }

    fun toastExceptionMsg(e: Throwable){
        val errMsg = handleException(e)
        ToastUtils.showShort(errMsg)
    }

}