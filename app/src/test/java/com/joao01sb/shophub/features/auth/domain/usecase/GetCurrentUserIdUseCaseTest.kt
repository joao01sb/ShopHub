package com.joao01sb.shophub.features.auth.domain.usecase

import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCurrentUserIdUseCaseTest {

    private val repository: AuthRepository = mockk(relaxed = true)

    private lateinit var useCase: GetCurrentUserIdUseCase

    @Before
    fun setup() {
        useCase = GetCurrentUserIdUseCase(repository)
    }

    @Test
    fun givenUserIsLoggedIn_whenInvoke_thenReturnsUserId() {
        val userId = "testUserId"

        every { repository.getUserId() } returns userId

        val result = useCase.invoke()

        assertTrue(result.isSuccess)

        assertEquals(userId, result.getOrNull())

        verify { repository.getUserId() }

    }

    @Test
    fun givenUserIsNotLoggedIn_whenInvoke_thenReturnsFailure() {
        every { repository.getUserId() } returns null

        val result = useCase.invoke()

        assertTrue(result.isFailure)

        assertNull(result.getOrNull())

        verify { repository.getUserId() }

    }

    @Test
    fun givenRepositoryThrowsException_whenInvoke_thenReturnsFailure() {
        val exception = NullPointerException()

        every { repository.getUserId() } throws exception

        val result = useCase.invoke()

        assertTrue(result.isFailure)

        assertEquals(exception, result.exceptionOrNull())

        verify { repository.getUserId() }

    }

}