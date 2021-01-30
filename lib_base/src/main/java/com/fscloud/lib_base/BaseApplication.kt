package com.fscloud.lib_base

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.StrictMode
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.fscloud.lib_base.umstatistics.UmEventUtils
import com.tencent.mmkv.BuildConfig
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.properties.Delegates


abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        initConfig()
        initRouter()
        //initUM()
        initLogConfig()
        //initStrictModel()
        //initBugly()
        //initJpush()
    }

    private fun initJpush(){
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }

    private fun initConfig() {
        //初始化mmkv
        var rootDir = MMKV.initialize(this)
        //路由初始化
        Utils.init(this)
        context = applicationContext
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private fun initRouter() {
        //gradle高版本开启导致aRouter无法跳转
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }

    //初始化友盟
    private fun initUM(){
        var appKey = ""
        var channel = ""
        var appInfo: ApplicationInfo? = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
        appInfo?.let {
            appKey = it.metaData.getString("UM_APPKEY","")
            channel = it.metaData.getString("UM_CHANNEL","")
            //LogUtils.i("友盟appKey: $appKey")
            //LogUtils.i("友盟channel: $channel")
        }
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        if (!com.fscloud.lib_base.BuildConfig.DEBUG && appKey.isNotEmpty() && channel.isNotEmpty()) {
            UMConfigure.init(
                this, appKey, channel,
                UMConfigure.DEVICE_TYPE_PHONE, null
            )
            UMConfigure.setLogEnabled(false)
            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
            UmEventUtils.init(this)  //初始化友盟埋点上下文

        }
    }

    private fun initLogConfig(){
        val config = LogUtils.getConfig()
        config.isLogSwitch = com.fscloud.lib_base.BuildConfig.DEBUG
    }

    //性能检测
    private fun initStrictModel(){
        if (com.fscloud.lib_base.BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build()
            )
        }
    }

    /*private fun initBugly() {
        if (!BuildConfig.DEBUG) {
            //CrashReport.initCrashReport(this, Mapper.BUGLY_APPID, false)
            val context = applicationContext
            // 获取当前包名
            val packageName = context.packageName
            // 获取当前进程名
            val processName = getProcessName(Process.myPid())
            // 设置是否为上报进程
            val strategy = CrashReport.UserStrategy(context)
            strategy.isUploadProcess = processName == null || processName == packageName
            // 初始化Bugly
            CrashReport.initCrashReport(context, Mapper.BUGLY_APPID, true, strategy)
        }
    }*/

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

    companion object {
        private val TAG = this::class.java.canonicalName
        var context: Context by Delegates.notNull()
            private set

    }
}

private class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

    }
}