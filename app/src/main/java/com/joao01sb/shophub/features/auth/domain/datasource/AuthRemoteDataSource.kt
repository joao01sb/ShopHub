package com.joao01sb.shophub.features.auth.domain.datasource

import com.joao01sb.shophub.core.result.firebase.FirebaseResult

interface AuthRemoteDataSource {
    suspend fun registerUser(email: String, password: String): FirebaseResult<String>
    suspend fun login(email: String, password: String) : FirebaseResult<Unit>
    suspend fun saveUser(uid: String, email: String, name: String) : FirebaseResult<Unit>
    fun logout() : FirebaseResult<Unit>
    fun isUserLoggedIn(): Boolean
    fun getUserId(): String?
}
