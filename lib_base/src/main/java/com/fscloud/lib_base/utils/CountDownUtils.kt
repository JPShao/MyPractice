package com.fscloud.lib_base.utils

import android.os.CountDownTimer
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @ProjectName : kaidianbao
 * @Author : JackCui
 * @Time : 2020/9/21 9:38
 * @Description : 文件描述
 */
class CountDownUtils : LifecycleObserver {
    companion object {
        @Volatile
        private var instance: CountDownUtils? = null
        fun getInstance(): CountDownUtils = instance ?: synchronized(CountDownUtils::class) {
            instance ?: CountDownUtils().also { instance = it }
        }
    }

    private var countDownTimer: CountDownTimer? = null
    fun countdown(textView: TextView, duration: Long, interval: Long) {
        textView.isEnabled = false
        countDownTimer = object : CountDownTimer(duration, interval) {
            override fun onTick(p0: Long) {
                var tick = p0 / 1000
                textView.text = "$tick s"
            }

            override fun onFinish() {
                textView.text = "重新获取"
                textView.isEnabled = true
            }
        }
        countDownTimer?.start()
    }

    private fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer?.onFinish()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        onDestroy()
    }
}