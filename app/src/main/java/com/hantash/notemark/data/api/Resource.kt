package com.hantash.notemark.data.api

sealed class Resource<T> {
    data class Success<T>(val data: T?): Resource<T>()
    data class Error<T>(val message: String?, val code: Int? = null, val throwable: Throwable? = null): Resource<T>()
    class Loading<T>: Resource<T>()
}