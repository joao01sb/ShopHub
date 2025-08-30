package com.joao01sb.shophub.features.cart.presentation.viewmodel

import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils
import com.joao01sb.shophub.features.cart.presentation.event.CartEvent
import com.joao01sb.shophub.features.cart.presentation.state.CartUiEvent
import com.joao01sb.shophub.features.cart.presentation.state.CartUiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private val cartManager: CartManager = mockk()
    private val authManager: AuthManager = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(testDispatcher)
        every { authManager.getCurrentUserId() } returns Result.success("userId")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun givenUpdatedCart_whenOnEvent_thenUpdateCartIsCalled() = runTest {
        val products = MockUtils.mockCartItems.take(3).toMutableList()
        val productUpdate = products.first()
        val newQuantity = 2
        val updatedProduct = productUpdate.copy(quantity = newQuantity)
        val updatedProducts = products.toMutableList().apply {
            set(0, updatedProduct)
        }

        val cartFlow = MutableStateFlow(products)

        every { cartManager.getItems(any()) } returns cartFlow
        coEvery { cartManager.addItem(any(), any(), any()) } coAnswers {
            cartFlow.value = updatedProducts
            DomainResult.Success(Unit)
        }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)

            viewModel.onEvent(CartEvent.UpdateCartItem(productUpdate, newQuantity))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState is CartUiState.Success)
            assert((updatedState as CartUiState.Success).cart.first().quantity == newQuantity)

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify { cartManager.addItem("userId", productUpdate, newQuantity) }

    }

    @Test
    fun givenUpdatedCart_whenOnEvent_thenUpdateCartIsCalled_error() = runTest {
        val products = MockUtils.mockCartItems
        val productUpdate = products.first()
        val newQuantity = 2

        every { cartManager.getItems(any()) } returns flowOf(products)
        coEvery { cartManager.addItem(any(), any(), any()) } returns
                DomainResult.Error("Error updating cart", ErrorType.UNKNOWN)

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)

            viewModel.onEvent(CartEvent.UpdateCartItem(productUpdate, newQuantity))
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assert(errorState is CartUiState.Error)
            assert((errorState as CartUiState.Error).message == "Error updating cart")

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify { cartManager.addItem("userId", productUpdate, newQuantity) }
    }

    @Test
    fun givenRemoveCartItem_whenOnEvent_thenRemoveCartItemIsCalled() = runTest {
        val products =
            MockUtils.mockCartItems.take(3).toMutableList()
        val productToRemove = products.first()
        val cartFlow = MutableStateFlow(products)

        every { cartManager.getItems(any()) } returns cartFlow
        coEvery { cartManager.removeItem(any(), any()) } coAnswers {
            cartFlow.value =
                products.filter { it.productId != productToRemove.productId }.toMutableList()
            DomainResult.Success(Unit)
        }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)
            assert((initialState as CartUiState.Success).cart.size == 3)

            viewModel.onEvent(CartEvent.RemoveCartItem(productToRemove))
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState is CartUiState.Success)
            assert((updatedState as CartUiState.Success).cart.size == 2)
            assert(updatedState.cart.none { it.productId == productToRemove.productId })

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify {
            cartManager.removeItem(
                "userId",
                productToRemove.productId.toString()
            )
        }
    }

    @Test
    fun givenRemoveCartItem_whenOnEvent_thenRemoveCartItemIsCalled_error() = runTest {
        val products = MockUtils.mockCartItems
        val productToRemove = products.first()

        every { cartManager.getItems(any()) } returns flowOf(products)
        coEvery { cartManager.removeItem(any(), any()) } returns
                DomainResult.Error("Error removing item", ErrorType.UNKNOWN)

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)

            viewModel.onEvent(CartEvent.RemoveCartItem(productToRemove))
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assert(errorState is CartUiState.Error)
            assert((errorState as CartUiState.Error).message == "Error removing item")

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify {
            cartManager.removeItem(
                "userId",
                productToRemove.productId.toString()
            )
        }
    }

    @Test
    fun givenClearCart_whenOnEvent_thenClearCartIsCalled() = runTest {
        val products = MockUtils.mockCartItems.take(3)
        val cartFlow = MutableStateFlow(products)

        every { cartManager.getItems(any()) } returns cartFlow
        coEvery { cartManager.clearCart(any()) } coAnswers {
            cartFlow.value = emptyList()
            DomainResult.Success(Unit)
        }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)

            viewModel.onEvent(CartEvent.ClearCart)
            testDispatcher.scheduler.advanceUntilIdle()

            val updatedState = awaitItem()
            assert(updatedState is CartUiState.Success)
            assert((updatedState as CartUiState.Success).cart.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify { cartManager.clearCart("userId") }
    }

    @Test
    fun givenClearCart_whenOnEvent_thenClearCartIsCalled_error() = runTest {
        val products = MockUtils.mockCartItems

        every { cartManager.getItems(any()) } returns
                MutableStateFlow(products)
        coEvery { cartManager.clearCart(any()) } returns
                DomainResult.Error("Error clearing cart", ErrorType.UNKNOWN)

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)

            val initialState = awaitItem()
            assert(initialState is CartUiState.Success)

            viewModel.onEvent(CartEvent.ClearCart)
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assert(errorState is CartUiState.Error)
            assert((errorState as CartUiState.Error).message == "Error clearing cart")

            cancelAndIgnoreRemainingEvents()
        }

        verify { cartManager.getItems("userId") }
        coVerify { cartManager.clearCart("userId") }
    }

    @Test
    fun givenRetry_whenOnEvent_thenGetItemsIsCalled_success() = runTest {
        coEvery { cartManager.getItems("userId") } returns flow {
            emit(MockUtils.mockCartItems)
        }

        viewModel = CartViewModel(cartManager, authManager)
        viewModel.onEvent(CartEvent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 2) { cartManager.getItems("userId") }
    }

    @Test
    fun `givenRetry_whenOnEvent_thenGetItemsIsCalled_error`() = runTest {
        coEvery { cartManager.getItems("userId") } returns flow {
            throw FirebaseFirestoreException(
                "Error loading items",
                FirebaseFirestoreException.Code.UNKNOWN
            )
        }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.cartItems.test {
            skipItems(1)
            viewModel.onEvent(CartEvent.Retry)
            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assert(errorState is CartUiState.Error)
            assert((errorState as CartUiState.Error).message == "Error fetching cart items")

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 2) { cartManager.getItems("userId") }
    }

    @Test
    fun `givenBack_whenOnEvent_thenEmitBackEvent`() = runTest {
        coEvery { cartManager.getItems("userId") } returns
                flow { emit(MockUtils.mockCartItems) }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.uiEvent.test {
            viewModel.onEvent(CartEvent.Back)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CartUiEvent.Back)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenNavigateToCheckout_whenOnEvent_thenEmitCheckoutEvent() = runTest {
        coEvery { cartManager.getItems("userId") } returns
                flow { emit(MockUtils.mockCartItems) }

        viewModel = CartViewModel(cartManager, authManager)

        viewModel.uiEvent.test {
            viewModel.onEvent(CartEvent.NavigateToCheckout)
            testDispatcher.scheduler.advanceUntilIdle()

            val event = awaitItem()
            assert(event is CartUiEvent.Checkout)

            cancelAndIgnoreRemainingEvents()
        }
    }

}