package com.joao01sb.shophub.features.auth.data.repository

import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.auth.data.datasource.AuthRemoteDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImpTest {

    val authRemoteDataSource: AuthRemoteDataSourceImp = mockk(relaxed = true)

    private lateinit var authRepository: AuthRepositoryImp

    @Before
    fun setup() {
        authRepository = AuthRepositoryImp(authRemoteDataSource)
    }

    @Test
    fun givenValidCredentials_whenRegister_thenSuccess() = runTest {
        val expectedUid = "Uid123"
        val email = "test@email.com"
        val password = "password123"

        coEvery { authRemoteDataSource.registerUser(email, password) } returns FirebaseResult.Success(expectedUid)

        val result = authRemoteDataSource.registerUser(email, password)

        assert(result is FirebaseResult.Success && result.data == expectedUid)

        coVerify(exactly = 1) { authRemoteDataSource.registerUser(email, password) }

    }

    @Test
    fun givenInvalidCredentials_whenRegister_thenFailure() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val errorMessage = "Invalid credentials"

        coEvery { authRemoteDataSource.registerUser(email, password) } returns
                FirebaseResult.UnknownError(Exception(errorMessage))

        val result = authRemoteDataSource.registerUser(email, password)

        assert(result is FirebaseResult.UnknownError)

        assert((result as FirebaseResult.UnknownError).exception.message == errorMessage)

        coVerify(exactly = 1) { authRemoteDataSource.registerUser(email, password) }

    }

}