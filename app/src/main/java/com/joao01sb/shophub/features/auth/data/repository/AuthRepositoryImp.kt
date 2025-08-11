package com.joao01sb.shophub.features.auth.data.repository

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.DomainResult.Error
import com.joao01sb.shophub.core.result.DomainResult.Success
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class AuthRepositoryImp(
    private val dataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ) : DomainResult<Unit> {
        return when (val result = dataSource.registerUser(email, password)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )
            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )
            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Registration failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ) : DomainResult<Unit> {
        return when (val result = dataSource.login(email, password)) {
            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Login failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun saveUser(
        uid: String,
        email: String,
        name: String
    ): DomainResult<Unit> {
        return when (val result = dataSource.saveUser(uid, email, name)) {
            is FirebaseResult.Success -> Success(Unit)
            is FirebaseResult.FirebaseError -> Error(
                message = result.message,
                type = ErrorType.DATABASE
            )
            else -> Error(
                message = "Failed to save user",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun logout() : DomainResult<Unit> {
        return when (val result = dataSource.logout()) {
            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Logout failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
            else -> DomainResult.Error(
                message = "Logout failed",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return dataSource.isUserLoggedIn()
    }

    override fun getUserId(): String? {
        return dataSource.getUserId()
    }
}
