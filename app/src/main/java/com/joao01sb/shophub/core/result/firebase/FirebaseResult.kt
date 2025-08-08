package com.joao01sb.shophub.core.result.firebase

sealed class FirebaseResult<out T> {
    data class Success<T>(val data: T) : FirebaseResult<T>()
    data class FirebaseError(val code: String, val message: String) : FirebaseResult<Nothing>()
    data class AuthError(val message: String) : FirebaseResult<Nothing>()
    data class UnknownError(val exception: Exception) : FirebaseResult<Nothing>()
}