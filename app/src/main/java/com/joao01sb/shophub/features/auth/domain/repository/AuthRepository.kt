package com.joao01sb.shophub.features.auth.domain.repository

import com.joao01sb.shophub.core.result.DomainResult

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): DomainResult<Unit>
    suspend fun login(email: String, password: String): DomainResult<Unit>

    suspend fun saveUser(uid: String, email: String, name: String): DomainResult<Unit>
    suspend fun logout(): DomainResult<Unit>
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
