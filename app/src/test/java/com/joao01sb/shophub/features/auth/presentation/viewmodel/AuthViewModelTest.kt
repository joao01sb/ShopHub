package com.joao01sb.shophub.features.auth.presentation.viewmodel

import app.cash.turbine.test
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.auth.presentation.event.AuthEvent
import com.joao01sb.shophub.features.auth.presentation.event.AuthUiEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var testDispatcher: TestDispatcher

    private lateinit var viewModel: AuthViewModel

    private lateinit var mockAuthManager: AuthManager

    @Before
    fun setUp() {
        clearAllMocks()
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        mockAuthManager = mockk(relaxed = true)
        every { mockAuthManager.isUserLoggedIn() } returns Result.success(false)
        viewModel = AuthViewModel(mockAuthManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun givenEventLogin_whenOnEvent_thenLoginMethodIsCalled_returnSuccess() = runTest {
        val userId = "0001"
        every { mockAuthManager.getCurrentUserId() } returns Result.success(userId)
        every { mockAuthManager.isUserLoggedIn() } returns Result.success(true)
        coEvery { mockAuthManager.loginUser(any(), any()) } returns
                DomainResult.Success(Unit)

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

    @Test
    fun givenEventLogin_whenOnEvent_thenLoginMethodIsCalled_returnError() = runTest {
        val userId = "0001"
        val errorMessage = "firebase authentication"
        every { mockAuthManager.getCurrentUserId() } returns Result.success(userId)
        every { mockAuthManager.isUserLoggedIn() } returns Result.failure(Exception(errorMessage))

        coEvery { mockAuthManager.loginUser(any(), any()) } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.AUTHENTICATION
        )

        viewModel.uiState.test {

            val stateInitial = awaitItem()
            assert(!stateInitial.isLoading)

            viewModel.onEvent(AuthEvent.Login)

            val stateLoading = awaitItem()
            assert(stateLoading.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val stateResult = expectMostRecentItem()
            assert(!stateResult.isLoading)
            assert(stateResult.error == errorMessage)

        }

    }

    @Test
    fun givenEventRegister_whenOnEvent_thenRegisterMethodIsCalled_returnSuccess() = runTest {
        val userId = "0001"
        every { mockAuthManager.getCurrentUserId() } returns Result.success(userId)
        every { mockAuthManager.isUserLoggedIn() } returns Result.success(true)

        coEvery { mockAuthManager.registerUser(any(), any(), any()) } returns DomainResult.Success(Unit)

        viewModel.uiState.test {

            skipItems(1)

            viewModel.onEvent(AuthEvent.Register)

            val stateLoading = awaitItem()
            assert(stateLoading.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val stateResult = expectMostRecentItem()
            assert(!stateResult.isLoading)
            assert(stateResult.error == null)

        }

    }

    @Test
    fun givenEventRegister_whenOnEvent_thenRegisterMethodIsCalled_returnError() = runTest {
        val userId = "0001"
        val errorMessage = "email already in use"
        every { mockAuthManager.getCurrentUserId() } returns Result.success(userId)
        every { mockAuthManager.isUserLoggedIn() } returns Result.failure(Exception(errorMessage))
        coEvery { mockAuthManager.registerUser(any(), any(), any()) } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.AUTHENTICATION
        )

        viewModel.uiState.test {

            skipItems(1)

            viewModel.onEvent(AuthEvent.Register)

            val stateLoading = awaitItem()
            assert(stateLoading.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val stateResult = expectMostRecentItem()
            assert(!stateResult.isLoading)
            assert(stateResult.error == errorMessage)
        }

    }

    @Test
    fun givenEventSingUp_whenOnEvent_thenNavigateToRegister() = runTest {
        viewModel.uiEvent.test {

            viewModel.onEvent(AuthEvent.SingUp)

            val event = awaitItem()
            assert(event is AuthUiEvent.NavigateToRegister)

        }

    }

    @Test
    fun givenEventNavigateToLogin_whenOnEvent_thenNavigateToLogin() = runTest {
        viewModel.uiEvent.test {

            viewModel.onEvent(AuthEvent.NavigateToLogin)

            val event = awaitItem()
            assert(event is AuthUiEvent.NavigateToLogin)

        }

    }

    @Test
    fun givenEventEmailChanged_whenOnEvent_thenEmailIsUpdated() = runTest {
        val email = "joao01sb@gmail.com"
        viewModel.uiState.test {

            skipItems(1)

            viewModel.onEvent(AuthEvent.EmailChanged(email))

            val state = awaitItem()
            assert(state.email == email)

        }

    }

    @Test
    fun givenEventPasswordChanged_whenOnEvent_thenPasswordIsUpdated() = runTest {
        val password = "123456"

        viewModel.uiState.test {

            skipItems(1)

            viewModel.onEvent(AuthEvent.PasswordChanged(password))

            val state = awaitItem()
            assert(state.password == password)

        }

    }

    @Test
    fun givenEventPasswordChanged_whenOnEvent_thenNameIsUpdated() = runTest {
        val name = "joao"

        viewModel.uiState.test {

            skipItems(1)

            viewModel.onEvent(AuthEvent.NameChanged(name))

            val state = awaitItem()
            assert(state.name == name)

        }

    }

    @Test
    fun givenClearState_whenOnEvent_thenStateIsCleared() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.onEvent(AuthEvent.EmailChanged("email"))
            viewModel.onEvent(AuthEvent.PasswordChanged("password"))
            viewModel.onEvent(AuthEvent.NameChanged("name"))
            viewModel.clearState()

            testDispatcher.scheduler.advanceUntilIdle()

            val stateInitial = expectMostRecentItem()
            assert(stateInitial.email.isEmpty())
            assert(stateInitial.password.isEmpty())

        }

    }

}