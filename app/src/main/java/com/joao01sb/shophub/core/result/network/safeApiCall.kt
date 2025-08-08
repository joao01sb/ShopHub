package com.joao01sb.shophub.core.result.network

import com.joao01sb.shophub.core.result.network.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ApiResult<T> {
    return withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall())
        } catch (e: IOException) {
            when (e) {
                is UnknownHostException -> ApiResult.NetworkError(
                    IOException("No internet connection")
                )
                is SocketTimeoutException -> ApiResult.NetworkError(
                    IOException("Request timeout")
                )
                else -> ApiResult.NetworkError(e)
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                400 -> "Bad request"
                401 -> "Unauthorized"
                403 -> "Forbidden"
                404 -> "Not found"
                500 -> "Internal server error"
                else -> "HTTP ${e.code()}: ${e.message()}"
            }
            ApiResult.HttpError(e.code(), errorMessage)
        } catch (e: Exception) {
            ApiResult.UnknownError(e)
        }
    }
}

suspend fun <T> (() -> T).safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): ApiResult<T> = safeApiCall(dispatcher, this)