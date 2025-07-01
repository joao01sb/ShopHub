package com.joao01sb.shophub.features.auth.domain.repository

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String)
    suspend fun login(email: String, password: String)
    suspend fun logout()
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
