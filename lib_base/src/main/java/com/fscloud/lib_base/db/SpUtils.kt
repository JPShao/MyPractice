package com.fscloud.lib_base.db

import android.os.Parcelable
import com.fscloud.lib_base.constant.Mapper
import com.tencent.mmkv.MMKV

/**
 * 微信MMKV存储代替
 */
class SpUtils {
    private var mv: MMKV? = null

    private constructor() {
        mv = MMKV.defaultMMKV()
    }

    companion object {
        @Volatile
        private var mInstance: SpUtils? = null

        /**
         * 初始化MMKV,只需要初始化一次，建议在Application中初始化
         *
         */
        fun getInstance(): SpUtils = mInstance ?: synchronized(SpUtils.javaClass) {
            mInstance ?: SpUtils()
        }
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    fun encode(key: String, `object`: Any) {
        when (`object`) {
            is String -> {
                mv?.encode(key, `object`)
            }
            is Int -> {
                mv?.encode(key, `object`)
            }
            is Boolean -> {
                mv?.encode(key, `object`)
            }
            is Float -> {
                mv?.encode(key, `object`)
            }
            is Long -> {
                mv?.encode(key, `object`)
            }
            is Double -> {
                mv?.encode(key, `object`)
            }
            is ByteArray -> {
                mv?.encode(key, `object`)
            }
            else -> {
                mv?.encode(key, `object`.toString())
            }
        }
    }

    fun encodeSet(key: String, sets: Set<String>) {
        mv?.encode(key, sets)
    }

    fun encodeParcelable(key: String, obj: Parcelable) {
        mv?.encode(key, obj)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    fun decodeInt(key: String): Int {
        return mv!!.decodeInt(key, 0)
    }

    fun decodeDouble(key: String): Double {
        return mv!!.decodeDouble(key, 0.00)
    }

    fun decodeLong(key: String): Long {
        return mv!!.decodeLong(key, 0L)
    }

    fun decodeBoolean(key: String): Boolean {
        return mv!!.decodeBool(key, false)
    }

    fun decodeFloat(key: String): Float {
        return mv!!.decodeFloat(key, 0f)
    }

    fun decodeBytes(key: String): ByteArray {
        return mv!!.decodeBytes(key)
    }

    fun decodeString(key: String): String {
        return mv!!.decodeString(key, "")
    }

    fun decodeStringSet(key: String): Set<String> {
        return mv!!.decodeStringSet(key, emptySet())
    }

//        fun <T:Parcelable>decodeParcelable(key: String,clazz: Class<T>): Parcelable {
//            return mv!!.decodeParcelable(key, clazz)
//        }

    /**
     * 移除某个key对
     *
     * @param key
     */
    fun removeKey(key: String) {
        mv?.removeValueForKey(key)
    }

    /**
     * 清除所有key
     */
    fun clearAll() {
        mv?.clearAll()
        mv?.clear()
    }

}