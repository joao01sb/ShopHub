package com.joao01sb.shophub.features.auth.domain.datasource

interface AuthRemoteDataSource {
    suspend fun registerUser(email: String, password: String): String
    suspend fun login(email: String, password: String)
    fun logout()
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
