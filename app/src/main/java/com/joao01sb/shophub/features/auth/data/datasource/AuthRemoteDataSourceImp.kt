package com.joao01sb.shophub.features.auth.data.datasource

import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource

class AuthRemoteDataSourceImp (

) : AuthRemoteDataSource{

    override suspend fun registerUser(
        email: String,
        password: String
    ): String {

    }

    override suspend fun login(email: String, password: String) {

    }

    override fun logout() {

    }

    override fun isUserLoggedIn(): Boolean {

    }

    override fun getUserId(): String? {

    }


}