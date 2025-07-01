package com.joao01sb.shophub.features.auth.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
    private val dataSource: AuthRemoteDataSource,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun register(email: String, password: String, name: String) {
        val uid = dataSource.registerUser(email, password)

        val user = mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name
        )

        firestore.collection("users").document(uid).set(user).await()
    }

    override suspend fun login(email: String, password: String) {
        dataSource.login(email, password)
    }

    override suspend fun logout() {
        dataSource.logout()
    }

    override fun isUserLoggedIn(): Boolean {
        return dataSource.isUserLoggedIn()
    }

    override fun getUserId(): String? {
        return dataSource.getUserId()
    }
}
