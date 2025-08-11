package com.joao01sb.shophub.core.result.firebase

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeFirebaseCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    firebaseCall: suspend () -> T
): FirebaseResult<T> {
    return withContext(dispatcher) {
        try {
            FirebaseResult.Success(firebaseCall())
        } catch (e: FirebaseAuthException) {
            val errorMessage = errorMessage(e)
            FirebaseResult.AuthError(errorMessage)
        } catch (e: FirebaseException) {
            FirebaseResult.FirebaseError(
                e.message ?: "FIREBASE_ERROR",
                e.localizedMessage ?: "Firebase operation failed"
            )
        } catch (e: Exception) {
            FirebaseResult.UnknownError(e)
        }
    }
}

private fun errorMessage(e: FirebaseAuthException): String {
    val errorMessage = when (e.errorCode) {
        "ERROR_INVALID_EMAIL" -> "Invalid email address"
        "ERROR_WEAK_PASSWORD" -> "Password is too weak"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Email is already registered"
        "ERROR_USER_NOT_FOUND" -> "User not found"
        "ERROR_WRONG_PASSWORD" -> "Incorrect password"
        "ERROR_USER_DISABLED" -> "User account is disabled"
        "ERROR_NETWORK_REQUEST_FAILED" -> "Network error"
        else -> e.message ?: "Authentication failed"
    }
    return errorMessage
}