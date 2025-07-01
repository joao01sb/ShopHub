package com.joao01sb.shophub.features.auth.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSourceImp (
    val firebaseAuth: FirebaseAuth
) : AuthRemoteDataSource{

    override suspend fun registerUser(
        email: String,
        password: String
    ): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw FirebaseAuthException("100", "error saving user")
    }

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }


}