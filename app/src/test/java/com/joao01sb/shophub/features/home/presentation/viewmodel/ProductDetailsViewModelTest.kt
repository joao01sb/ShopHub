package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.domain.mapper.toCartItem
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.usecase.GetProductByIdUseCase
import com.joao01sb.shophub.features.home.presentation.event.DetailsEvent
import com.joao01sb.shophub.features.home.presentation.event.DetailsUiEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getProductByIdUseCase: GetProductByIdUseCase
    private lateinit var cartManager: CartManager
    private lateinit var authManager: AuthManager
    private lateinit var viewModel: ProductDetailsViewModel

    @Before
    fun setup() {
        clearAllMocks()
        savedStateHandle = mockk(relaxed = true)
        getProductByIdUseCase = mockk(relaxed = true)
        cartManager = mockk(relaxed = true)
        authManager = mockk(relaxed = true)

        setupDefaultMocks()

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun setupDefaultMocks() {

        every { savedStateHandle.get<Int>("idUser") } returns 1

        every { authManager.getCurrentUserId() } returns Result.success("test_user_id")

        coEvery { getProductByIdUseCase(any()) } returns DomainResult.Success(
            MockUtils.productsDto.first().toDomain()
        )
    }

    private fun createViewModel() {
        viewModel = ProductDetailsViewModel(
            savedStateHandle,
            getProductByIdUseCase,
            cartManager,
            authManager
        )
    }

    @Test
    fun givenAddToCartEvent_whenOnEvent_thenAddToCartIsCalled() = runTest {
        val product = MockUtils.productsDto.first().toDomain().toCartItem()
        val userId = "test_user_id"
        val size = 1

        coEvery { cartManager.addItem(userId, product, size) } returns
                DomainResult.Success(Unit)

        createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onEvent(DetailsEvent.AddToCart)
            advanceUntilIdle()

            skipItems(1)

            val state = awaitItem()

            assert(state.error == null)
            assert(!state.isLoading)
            assert(state.isShowButtonToCart)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun givenAddToCartEvent_whenOnEvent_thenAddToCartIsCalled_returnError() = runTest {
        val product = MockUtils.productsDto.first().toDomain().toCartItem()
        val userId = "test_user_id"
        val size = 1
        val errorMessage = "Failed to add to cart"

        coEvery { cartManager.addItem(userId, product, size) } returns
                DomainResult.Error(
                    message = errorMessage,
                    type = ErrorType.UNKNOWN
                )

        createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onEvent(DetailsEvent.AddToCart)
            advanceUntilIdle()

            skipItems(1)

            val state = awaitItem()

            assert(state.error == errorMessage)
            assert(!state.isLoading)
            assert(!state.isShowButtonToCart)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun givenGetProductByIdUseCase_whenInit_thenGetProductByIdIsCalled_returnError() = runTest {
        val errorMessage = "Product not found"
        coEvery { getProductByIdUseCase(any()) } returns DomainResult.Error(
            message = errorMessage,
            type = ErrorType.UNKNOWN
        )

        createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()

            val stateResult = expectMostRecentItem()
            assert(!stateResult.isLoading)
            assert(stateResult.error == errorMessage)

        }

    }

    @Test
    fun givenNavigateToCartEvent_whenOnEvent_thenUiEventIsNavigateToCart() = runTest {
        createViewModel()
        advanceUntilIdle()

        viewModel.uiEvent.test {
            viewModel.onEvent(DetailsEvent.NavigateToCard)
            advanceUntilIdle()

            val event = awaitItem()

            assert(event is DetailsUiEvent.NavigateToCart)
            cancelAndIgnoreRemainingEvents()
        }

    }

}