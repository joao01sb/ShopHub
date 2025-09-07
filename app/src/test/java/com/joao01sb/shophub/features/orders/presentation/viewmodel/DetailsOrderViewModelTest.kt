package com.joao01sb.shophub.features.orders.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.orders.data.datasource.MockUtils
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrderByIdUseCase
import com.joao01sb.shophub.features.orders.presentation.state.OrderDetailsUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsOrderViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val getOrderByIdUseCase = mockk<GetOrderByIdUseCase>(relaxed = true)
    private val authManager = mockk<AuthManager>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var detailsOrderViewModel: DetailsOrderViewModel

    private val orderId = "orderId123"
    private val userId = "userId456"

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(testDispatcher)
        every { savedStateHandle.get<String>("orderId") } returns orderId
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenViewModel_whenInitialized_withValidOrderId_thenLoadOrderSuccessfully() = runTest {
        val mockOrder = MockUtils.orders.first()

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrderByIdUseCase(userId, orderId) } returns DomainResult.Success(mockOrder)

        detailsOrderViewModel = DetailsOrderViewModel(savedStateHandle, getOrderByIdUseCase, authManager)
        advanceUntilIdle()

        detailsOrderViewModel.orderDetailsUiState.test {
            val state = awaitItem()
            assert(state is OrderDetailsUiState.Success)
            val order = (state as OrderDetailsUiState.Success).order
            assert(order.orderNumber == "1")
            assert(order.items.size == 3)
        }

        verify(exactly = 1) { authManager.getCurrentUserId() }
    }

    @Test
    fun givenViewModel_whenInitialized_withAuthFailure_thenThrowException() = runTest {
        every { authManager.getCurrentUserId() } returns Result.failure(Exception("Auth failed"))

        try {
            detailsOrderViewModel = DetailsOrderViewModel(savedStateHandle, getOrderByIdUseCase, authManager)
            advanceUntilIdle()
            assert(false) { "Should have thrown exception" }
        } catch (e: IllegalStateException) {
            assert(e.message?.contains("Failed to get user ID") == true)
        }
    }

    @Test
    fun givenViewModel_whenGetOrderByIdReturnsNull_thenShowOrderNotFoundError() = runTest {
        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrderByIdUseCase(userId, orderId) } returns DomainResult.Success(null)

        detailsOrderViewModel = DetailsOrderViewModel(savedStateHandle, getOrderByIdUseCase, authManager)
        advanceUntilIdle()

        detailsOrderViewModel.orderDetailsUiState.test {
            val state = awaitItem()
            assert(state is OrderDetailsUiState.Error)
            val error = (state as OrderDetailsUiState.Error).message
            assert(error == "Order not found")
        }
    }

    @Test
    fun givenViewModel_whenGetOrderByIdFails_thenShowError() = runTest {
        val errorMessage = "Database connection failed"

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrderByIdUseCase(userId, orderId) } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.DATABASE
        )

        detailsOrderViewModel = DetailsOrderViewModel(savedStateHandle, getOrderByIdUseCase, authManager)
        advanceUntilIdle()

        detailsOrderViewModel.orderDetailsUiState.test {
            val state = awaitItem()
            assert(state is OrderDetailsUiState.Error)
            val error = (state as OrderDetailsUiState.Error).message
            assert(error == errorMessage)
        }
    }

    @Test
    fun givenViewModel_whenUseCaseReturnsErrorWithNullMessage_thenShowUnknownError() = runTest {
        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrderByIdUseCase(userId, orderId) } returns DomainResult.Error(
            message = "Unknown error",
            type = ErrorType.UNKNOWN
        )

        detailsOrderViewModel = DetailsOrderViewModel(savedStateHandle, getOrderByIdUseCase, authManager)
        advanceUntilIdle()

        detailsOrderViewModel.orderDetailsUiState.test {
            val state = awaitItem()
            assert(state is OrderDetailsUiState.Error)
            val error = (state as OrderDetailsUiState.Error).message
            assert(error == "Unknown error")
        }
    }
}