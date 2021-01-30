package com.fscloud.lib_base.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class BaseResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : BaseResult<T>()

    data class Error(val exception: Exception) : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}