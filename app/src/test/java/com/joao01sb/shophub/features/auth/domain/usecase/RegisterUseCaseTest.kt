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

class RegisterUseCaseTest {

    private val mockAuthRepository: AuthRepository = mockk(relaxed = true)

    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setup() {
        registerUseCase = RegisterUseCase(mockAuthRepository)
    }

    @Test
    fun givenRegisterUseCase_whenInvoke_thenReturnsSuccess() = runTest {
        val email = "joao01sb@email"
        val password = "password123"
        val name = "Test User"
        val returnRegister = DomainResult.Success(Unit)

        coEvery { mockAuthRepository.register(email, password, name) } returns returnRegister
        coEvery { mockAuthRepository.getUserId() } returns "testUserId"
        coEvery {
            mockAuthRepository.saveUser(
                "testUserId",
                email,
                name
            )
        } returns DomainResult.Success(Unit)

        val result = registerUseCase.invoke(email, password, name)

        assertEquals(returnRegister, result)
        assertEquals(Unit, (result as DomainResult.Success).data)

        coVerify { mockAuthRepository.register(email, password, name) }

    }

    @Test
    fun givenRegisterUseCase_whenInvoke_thenReturnsError() = runTest {
        val email = "joao01sb@email"
        val password = "password123"
        val name = "Test User"
        val errorMessage = "Error"

        coEvery { mockAuthRepository.register(email, password, name) } returns
                DomainResult.Error(errorMessage, ErrorType.UNKNOWN)

        val result = registerUseCase.invoke(email, password, name)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)

        coVerify { mockAuthRepository.register(email, password, name) }

    }

    @Test
    fun givenRegisterUseCase_whenInvoke_thenReturnsErrorWithValidationMessage() = runTest {
        val email = ""
        val password = "password123"
        val name = "Test User"
        val errorMessage = "Email, password, and name cannot be empty"

        coEvery { mockAuthRepository.register(email, password, name) } returns
                DomainResult.Error(errorMessage, ErrorType.VALIDATION)
        coEvery { mockAuthRepository.getUserId() } returns null
        coEvery {
            mockAuthRepository.saveUser(
                "testUserId",
                email,
                name
            )
        } returns DomainResult.Success(Unit)

        val result = registerUseCase.invoke(email, password, name)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)

    }

}