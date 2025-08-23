package com.joao01sb.shophub.features.auth.presentation.viewmodel

import app.cash.turbine.test
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.presentation.event.AuthEvent
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel

    private val mockAuthManager: AuthManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockAuthManager.isUserLoggedIn() } returns Result.success(false)
        viewModel = AuthViewModel(mockAuthManager)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun givenEventLogin_whenOnEvent_thenLoginMethodIsCalled() = runTest {
        val userId = "0001"
        every { mockAuthManager.getCurrentUserId() } returns Result.success(userId)
        every { mockAuthManager.isUserLoggedIn() } returns Result.success(true)
        coEvery { mockAuthManager.loginUser(any(), any()) } returns DomainResult.Success(Unit)

        viewModel.uiState.test {

            val stateInitial = awaitItem()
            assert(!stateInitial.isLoading)

            viewModel.onEvent(AuthEvent.Login)

            val stateLoading = awaitItem()
            assert(stateLoading.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val stateResult = expectMostRecentItem()
            assert(!stateResult.isLoading)
            assert(stateResult.error == null)

        }

    }

}