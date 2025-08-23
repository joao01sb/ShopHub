package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsUserLoggedInUseCaseTest {

    private val mockAuthRepository: AuthRepository = mockk(relaxed = true)

    private lateinit var useCase: IsUserLoggedInUseCase

    @Before
    fun setup() {
        useCase = IsUserLoggedInUseCase(mockAuthRepository)
    }

    @Test
    fun givenUserIsLoggedIn_whenInvoke_thenReturnsTrue() {
        every { mockAuthRepository.isUserLoggedIn() } returns true

        val result = useCase.invoke()

        assert(result.isSuccess)

        val successResult = result.getOrNull()

        assertNotNull(successResult)

        assertTrue(successResult!!)

    }

    @Test
    fun givenUserIsNotLoggedIn_whenInvoke_thenReturnsFalse() {
        every { mockAuthRepository.isUserLoggedIn() } returns false

        val result = useCase.invoke()

        assert(result.isSuccess)

        val successResult = result.getOrNull()

        assertNotNull(successResult)

        assertFalse(successResult!!)

    }

    @Test
    fun givenRepositoryThrowsException_whenInvoke_thenReturnsFailure() {
        val exception = NullPointerException()

        every { mockAuthRepository.isUserLoggedIn() } throws exception

        val result = useCase.invoke()

        assert(result.isFailure)

        val failureResult = result.exceptionOrNull()

        assertNotNull(failureResult)

        assertEquals(exception, failureResult)

    }

}