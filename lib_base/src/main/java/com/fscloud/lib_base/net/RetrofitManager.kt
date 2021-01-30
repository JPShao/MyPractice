package com.fscloud.lib_base.net

import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.*
import com.fscloud.lib_base.BaseApplication
import com.fscloud.lib_base.constant.GlobalVariable
import com.fscloud.lib_base.model.BaseResponse
import com.fscloud.lib_base.constant.Mapper
import com.fscloud.lib_base.constant.UrlConstant
import com.fscloud.lib_base.db.SpUtils
import com.fscloud.lib_base.router.RouterTable
import com.fscloud.lib_base.utils.NetworkUtil
import com.google.gson.JsonSyntaxException
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 网络请求
 */
class RetrofitManager private constructor() {
    private var retrofit: Retrofit? = null

    companion object {
        @Volatile
        private var instance: RetrofitManager? = null
        fun getInstance(): RetrofitManager = instance ?: synchronized(this) {
            instance ?: RetrofitManager().also { instance = it }
        }
    }

    fun <T> service(clazz: Class<T>): T {
        retrofit = getDefaultRetrofit()
        return retrofit!!.create(clazz)
    }

    /**
     * 拦截响应数据
     */
    private fun addResponseInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val responseBody = response.body()
            if (responseBody != null) {
                val string = responseBody.string()
                val mediaType = responseBody.contentType()
                LogUtils.json(LogUtils.I,"${request.url()}请求返回的数据：",string)
                if (string.contains("code")) {
                    try {
                        val baseResponse = GsonUtils.fromJson(
                            string,
                            BaseResponse<Any>()::class.java
                        )
                        if (baseResponse.code != "1"){
                            ToastUtils.showLong(baseResponse.message)
                        }
                        if (baseResponse.code == "401") {
                            synchronized(getInstance()) {
                                SpUtils.getInstance().clearAll()
                                ActivityUtils.finishAllActivities()
                                ARouter.getInstance().build(RouterTable.LOGIN)
                                    .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .withBoolean("isLogout", true)
                                    .navigation()
                                LogUtils.i("登录失效")
                            }
                        }
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                    }
                }
                response.newBuilder()
                    .body(ResponseBody.create(mediaType, string))
                    .build()
            } else {
                ToastUtils.showLong("获取数据失败")
                response
            }
        }
    }

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                // Provide your custom parameter here
//                    .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
//                    .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                // Provide your custom header here
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("client", "app")  //区别手机端和客如云，“app”手机app  “kry”客如云终端app
                .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(BaseApplication.context)) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(BaseApplication.context)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("nyn")
                    .build()
            }
            response
        }
    }

    fun getBaseUrlRetrofit(url: String): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
            .baseUrl(url)  //自己配置
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonConfigUtil.createGson()))
            .build()
    }

    private fun getDefaultRetrofit(): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
            .baseUrl(UrlConstant.BASE_URL)  //自己配置
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonConfigUtil.createGson()))
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        // 添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        // 可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //设置 请求的缓存的大小跟位置
        val cacheFile = File(BaseApplication.context.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小
        val sslParams = SslSocketFactory.sslSocketFactory

        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor())  //参数添加
            .addInterceptor(addHeaderInterceptor()) // token过滤
            .addInterceptor(TokenHeaderInterceptor())
            .addInterceptor(addCacheInterceptor())
            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
            .addInterceptor(addResponseInterceptor())
            .cache(cache)  //添加缓存
//            .sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    //在请求头里添加token的拦截器处理
    class TokenHeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val token: String = SpUtils.getInstance().decodeString(Mapper.TOKEN)
            return if (token.isEmpty()) {
                val originalRequest: Request = chain.request()
                chain.proceed(originalRequest)
            } else {
                val originalRequest: Request = chain.request()
                //key的话以后台给的为准，我这边是叫token
                val updateRequest = originalRequest.newBuilder().header("token", token).build()
                chain.proceed(updateRequest)
            }
        }
    }
}
