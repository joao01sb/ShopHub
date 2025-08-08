package com.joao01sb.shophub.core.result

import com.joao01sb.shophub.core.error.ErrorType

sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val message: String, val type: ErrorType) : DomainResult<Nothing>()
}

inline fun <T, R> DomainResult<T>.toDomainResult(transform: (T) -> R): DomainResult<R> {
    return when (this) {
        is DomainResult.Success -> DomainResult.Success(transform(this.data))
        is DomainResult.Error -> this
    }
}
