package com.joao01sb.shophub.features.auth.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.core.result.firebase.safeFirebaseCall
import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSourceImp(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : AuthRemoteDataSource {

    override suspend fun registerUser(
        email: String,
        password: String
    ): FirebaseResult<String> =
        safeFirebaseCall {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid ?: throw NullPointerException("Failed to create user - UID is null")
        }

    override suspend fun login(
        email: String,
        password: String
    ): FirebaseResult<Unit> =
        safeFirebaseCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Unit
        }

    override suspend fun saveUser(
        uid: String,
        email: String,
        name: String
    ): FirebaseResult<Unit> =
        safeFirebaseCall {
            val user = hashMapOf(
                "uid" to uid,
                "email" to email,
                "name" to name
            )
            firestore.collection("users").document(uid).set(user).await()
        }

    override fun logout(): FirebaseResult<Unit> {
        return try {
            firebaseAuth.signOut()
            FirebaseResult.Success(Unit)
        } catch (e: Exception) {
            FirebaseResult.UnknownError(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

}