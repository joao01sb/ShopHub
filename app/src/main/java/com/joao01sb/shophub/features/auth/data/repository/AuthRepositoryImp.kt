package com.joao01sb.shophub.features.auth.data.repository

import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository

class AuthRepositoryImp(
    private val dataSource: AuthRemoteDataSource
) : AuthRepository{

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<Unit> {

    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

    }

    override suspend fun logout() {

    }

    override fun isUserLoggedIn(): Boolean {

    }

    override fun getUserId(): String? {

    }

}