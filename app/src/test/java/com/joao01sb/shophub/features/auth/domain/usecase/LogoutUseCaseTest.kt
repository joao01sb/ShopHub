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

class LogoutUseCaseTest {

    private val mockAuthRepository: AuthRepository = mockk()

    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setup() {
        logoutUseCase = LogoutUseCase(mockAuthRepository)
    }

    @Test
    fun givenUserIsLoggedIn_whenLogout_thenReturnsSuccess() = runTest {
        coEvery { mockAuthRepository.logout() } returns
                DomainResult.Success(Unit)

        val result = logoutUseCase.invoke()

        assertTrue(result is DomainResult.Success)
        assertEquals(Unit, (result as DomainResult.Success).data)

        coVerify { mockAuthRepository.logout() }

    }

    @Test
    fun givenUserIsLoggedOut_whenLogout_thenReturnsError() = runTest {
        coEvery { mockAuthRepository.logout() } returns DomainResult.Error(
            "Error",
            ErrorType.UNKNOWN
        )

        val result = logoutUseCase.invoke()

        assertTrue(result is DomainResult.Error)
        assertEquals("Error", (result as DomainResult.Error).message)

        coVerify { mockAuthRepository.logout() }

    }

}