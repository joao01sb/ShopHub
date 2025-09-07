package com.joao01sb.shophub.features.orders.presentation.viewmodel

import app.cash.turbine.test
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.orders.data.datasource.MockUtils
import com.joao01sb.shophub.features.orders.domain.usecase.GetOrdersUseCase
import com.joao01sb.shophub.features.orders.presentation.event.OrderUiEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrdersEvent
import com.joao01sb.shophub.features.orders.presentation.state.OrdersUiState
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
class OrdersViewModelTest {

    private val getOrdersUseCase = mockk<GetOrdersUseCase>(relaxed = true)
    private val authManager = mockk<AuthManager>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var ordersViewModel: OrdersViewModel

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenViewModel_whenInitialized_withValidUser_thenLoadOrdersSuccessfully() = runTest {
        val userId = "userId123"
        val mockOrders = MockUtils.orders

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Success(mockOrders)

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            val state = awaitItem()
            assert(state is OrdersUiState.Success)
            val orders = (state as OrdersUiState.Success).orders
            assert(orders.size == 2)
            assert(orders[0].orderNumber == "1")
            assert(orders[1].orderNumber == "2")
        }

        verify(exactly = 1) { authManager.getCurrentUserId() }
    }

    @Test
    fun givenViewModel_whenInitialized_withInvalidUser_thenShowAuthError() = runTest {
        every { authManager.getCurrentUserId() } returns Result.failure(Exception("Auth failed"))

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            val state = awaitItem()
            assert(state is OrdersUiState.Error)
            val error = (state as OrdersUiState.Error).message
            assert(error == "User not authenticated")
        }
    }

    @Test
    fun givenViewModel_whenGetOrdersFails_thenShowError() = runTest {
        val userId = "userId123"
        val errorMessage = "Database connection failed"

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.DATABASE
        )

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            val state = awaitItem()
            assert(state is OrdersUiState.Error)
            val error = (state as OrdersUiState.Error).message
            assert(error == errorMessage)
        }
    }

    @Test
    fun givenViewModel_whenRefreshOrdersEvent_thenReloadOrders() = runTest {
        val userId = "userId123"
        val mockOrders = MockUtils.orders

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Success(mockOrders)

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            awaitItem()

            ordersViewModel.onEvent(OrdersEvent.RefreshOrders)
            advanceUntilIdle()

            val loadingState = awaitItem()
            assert(loadingState is OrdersUiState.Loading)

            val successState = awaitItem()
            assert(successState is OrdersUiState.Success)
            val orders = (successState as OrdersUiState.Success).orders
            assert(orders.size == 2)
        }
    }

    @Test
    fun givenViewModel_whenLogoutEventSuccess_thenEmitLogoutUiEvent() = runTest {
        val userId = "userId123"

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Success(emptyList())
        coEvery { authManager.logoutUser() } returns DomainResult.Success(Unit)

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.orderUiEvent.test {
            ordersViewModel.onEvent(OrdersEvent.Logout)
            advanceUntilIdle()

            val event = awaitItem()
            assert(event is OrderUiEvent.Logout)
        }
    }

    @Test
    fun givenViewModel_whenLogoutEventFails_thenShowErrorState() = runTest {
        val userId = "userId123"
        val errorMessage = "Logout failed"

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Success(emptyList())
        coEvery { authManager.logoutUser() } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.AUTHENTICATION
        )

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            awaitItem()

            ordersViewModel.onEvent(OrdersEvent.Logout)
            advanceUntilIdle()

            val state = awaitItem()
            assert(state is OrdersUiState.Error)
            val error = (state as OrdersUiState.Error).message
            assert(error == "Erro ao fazer logout: $errorMessage")
        }
    }

    @Test
    fun givenViewModel_whenGetOrdersReturnsEmptyList_thenShowSuccessWithEmptyList() = runTest {
        val userId = "userId123"

        every { authManager.getCurrentUserId() } returns Result.success(userId)
        coEvery { getOrdersUseCase(userId) } returns DomainResult.Success(emptyList())

        ordersViewModel = OrdersViewModel(getOrdersUseCase, authManager)
        advanceUntilIdle()

        ordersViewModel.ordersUiState.test {
            val state = awaitItem()
            assert(state is OrdersUiState.Success)
            val orders = (state as OrdersUiState.Success).orders
            assert(orders.isEmpty())
        }
    }
}