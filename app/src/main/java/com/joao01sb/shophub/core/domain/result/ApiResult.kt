package com.joao01sb.shophub.core.domain.result

import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class NetworkError(val exception: IOException) : ApiResult<Nothing>()
    data class HttpError(val code: Int, val message: String) : ApiResult<Nothing>()
    data class UnknownError(val exception: Exception) : ApiResult<Nothing>()
}