package com.joao01sb.shophub.features.auth.domain.repository

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout()
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
