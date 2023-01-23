package com.task.branch.util

sealed class Resource <out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    data class Exception<T>(val exception: Throwable) : Resource<T>()
    object Loading : Resource<Nothing>()
}