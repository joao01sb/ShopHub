package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private val authRepository: AuthRepository = mockk()

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun givenValidCredentials_whenInvoke_thenReturnsSuccess() = runTest {
        val email = "joao01sb@email"
        val password = "password123"

        coEvery { authRepository.login(email, password) } returns
                DomainResult.Success(Unit)

        val result = loginUseCase.invoke(email, password)

        assertTrue(result is DomainResult.Success)
        assertEquals(Unit, (result as DomainResult.Success).data)

        coVerify { authRepository.login(email, password) }

    }

    @Test
    fun givenInvalidCredentials_whenInvoke_thenReturnsError() = runTest {
        val email = "joao01sb@email"
        val password = "password123"
        val errorMessage = "Invalid credentials"

        coEvery { authRepository.login(email, password) } returns
                DomainResult.Error(errorMessage, ErrorType.AUTHENTICATION)

        val result = loginUseCase.invoke(email, password)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)

        coVerify { authRepository.login(email, password) }

    }

    @Test
    fun givenRepositoryThrowsException_whenInvoke_thenReturnsError() = runTest {
        val email = "joao01sb@email"
        val password = "password123"
        val errorMessage = "Error"

        coEvery { authRepository.login(email, password) } returns
                DomainResult.Error(errorMessage, ErrorType.UNKNOWN)

        val result = loginUseCase.invoke(email, password)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)

        coVerify { authRepository.login(email, password) }

    }

}