package com.joao01sb.shophub.features.cart.data.repository

import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class CartRepositoryImpTest {

    val dataSource: CartRemoteDataSource = mockk(relaxed = true)

    private lateinit var repository: CartRepositoryImp

    @Before
    fun setup() {
        clearAllMocks()
        repository = CartRepositoryImp(dataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenObserveCartItems_whenCalled_returnsItens_success() = runTest {
        val userId = "userId123"
        val itens = MockUtils.mockCartItems

        every { dataSource.observeCartItems(userId) } returns flowOf(itens)

        repository.observeCartItems(userId).test {
            val item = awaitItem()
            assert(item == itens)
            assert(item.size == itens.size)
            awaitComplete()
        }

        verify(exactly = 1) { dataSource.observeCartItems(userId) }

    }

    @Test
    fun givenObserveCartItems_whenCalled_returnsEmptyList_success() = runTest {
        val userId = "userId123"
        val itens = emptyList<CartItem>()

        every { dataSource.observeCartItems(userId) } returns flowOf(itens)

        repository.observeCartItems(userId).test {
            val item = awaitItem()
            assert(item == itens)
            assert(item.isEmpty())
            awaitComplete()
        }

        verify(exactly = 1) { dataSource.observeCartItems(userId) }

    }

    @Test
    fun givenObserveCartItems_whenCalled_multipleEmissions_success() = runTest {
        val userId = "userId123"
        val itens1 = MockUtils.mockCartItems
        val itens2 = MockUtils.mockCartItems.map { it.copy(quantity = it.quantity + 1) }
        val itens3 = emptyList<CartItem>()

        every { dataSource.observeCartItems(userId) } returns
                flowOf(itens1, itens2, itens3)

        repository.observeCartItems(userId).test {
            val item1 = awaitItem()
            assert(item1 == itens1)
            assert(item1.size == itens1.size)

            val item2 = awaitItem()
            assert(item2 == itens2)
            assert(item2.size == itens2.size)

            val item3 = awaitItem()
            assert(item3 == itens3)
            assert(item3.isEmpty())

            awaitComplete()
        }

        verify(exactly = 1) { dataSource.observeCartItems(userId) }

    }

    @Test
    fun givenObserveCartItems_whenCalled_returnsError() = runTest {
        val userId = "userId123"

        every { dataSource.observeCartItems(userId) } throws
                FirebaseFirestoreException(
                    "Test exception",
                    FirebaseFirestoreException.Code.UNKNOWN
                )

        try {
            repository.observeCartItems(userId).test {
                awaitItem()
            }
        } catch (e: Exception) {
            assert(e is FirebaseFirestoreException)
            assert(e.message == "Test exception")
        }

        verify(exactly = 1) { dataSource.observeCartItems(userId) }

    }

    @Test
    fun givenUpdateItem_whenCalled_returnsSuccess() = runTest {
        val userId = "userId123"
        val item = MockUtils.mockCartItems.first()

        coEvery { dataSource.updateItem(userId, item) } returns
                FirebaseResult.Success(Unit)

        val result = repository.updateItem(userId, item)

        assert(result is DomainResult.Success)

        coVerify(exactly = 1) { dataSource.updateItem(userId, item) }

    }

    @Test
    fun givenUpdateItem_whenCalled_returnsError() = runTest {
        val userId = "userId123"
        val item = MockUtils.mockCartItems.first()

        coEvery { dataSource.updateItem(userId, item) } returns
                FirebaseResult.FirebaseError("500", "Network error")

        val result = repository.updateItem(userId, item)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Network error")

        coVerify(exactly = 1) { dataSource.updateItem(userId, item) }
    }

    @Test
    fun givenRemoveItem_whenCalled_returnsSuccess() = runTest {
        val userId = "userId123"
        val productId = "productId123"

        coEvery { dataSource.removeItem(userId, productId) } returns
                FirebaseResult.Success(Unit)

        val result = repository.removeItem(userId, productId)

        assert(result is DomainResult.Success)

        coVerify(exactly = 1) { dataSource.removeItem(userId, productId) }
    }

    @Test
    fun givenRemoveItem_whenCalled_returnsError() = runTest {
        val userId = "userId123"
        val productId = "productId123"

        coEvery { dataSource.removeItem(userId, productId) } returns
                FirebaseResult.FirebaseError("500", "Network error")

        val result = repository.removeItem(userId, productId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Network error")

        coVerify(exactly = 1) { dataSource.removeItem(userId, productId) }
    }

    @Test
    fun givenRemoveItem_whenCalled_returnsUnknownError() = runTest {
        val userId = "userId123"
        val productId = "productId123"

        coEvery { dataSource.removeItem(userId, productId) } returns
                FirebaseResult.UnknownError(Exception("Unknown error"))

        val result = repository.removeItem(userId, productId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Remove failed: Unknown error")

        coVerify(exactly = 1) { dataSource.removeItem(userId, productId) }
    }

    @Test
    fun givenClearCart_whenCalled_returnsSuccess() = runTest {
        val userId = "userId123"

        coEvery { dataSource.clearCart(userId) } returns
                FirebaseResult.Success(Unit)

        val result = repository.clearCart(userId)

        assert(result is DomainResult.Success)

        coVerify(exactly = 1) { dataSource.clearCart(userId) }
    }

    @Test
    fun givenClearCart_whenCalled_returnsError() = runTest {
        val userId = "userId123"

        coEvery { dataSource.clearCart(userId) } returns
                FirebaseResult.FirebaseError("500", "Network error")

        val result = repository.clearCart(userId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Network error")

        coVerify(exactly = 1) { dataSource.clearCart(userId) }
    }

    @Test
    fun givenClearCart_whenCalled_returnsUnknownError() = runTest {
        val userId = "userId123"

        coEvery { dataSource.clearCart(userId) } returns
                FirebaseResult.UnknownError(Exception("Unknown error"))

        val result = repository.clearCart(userId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Clear cart failed: Unknown error")

        coVerify(exactly = 1) { dataSource.clearCart(userId) }
    }

    @Test
    fun givenPlaceOrder_whenCalled_returnsSuccess() = runTest {
        val userId = "userId123"
        val items = MockUtils.mockCartItems
        val info = MockUtils.info

        coEvery { dataSource.placeOrder(userId, any(), any()) } returns
                FirebaseResult.Success(Unit)

        val result = repository.placeOrder(userId, items, info)

        assert(result is DomainResult.Success)
        coVerify(exactly = 1) { dataSource.placeOrder(userId, items, info) }
    }

    @Test
    fun givenPlaceOrder_whenCalled_returnsError() = runTest {
        val userId = "userId123"
        val items = MockUtils.mockCartItems
        val info = MockUtils.info

        coEvery { dataSource.placeOrder(userId, any(), any()) } returns
                FirebaseResult.FirebaseError("500", "Network error")

        val result = repository.placeOrder(userId, items, info)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Network error")

        coVerify(exactly = 1) { dataSource.placeOrder(userId, items, info) }
    }

    @Test
    fun givenPlaceOrder_whenCalled_returnsUnknownError() = runTest {
        val userId = "userId123"
        val items = MockUtils.mockCartItems
        val info = MockUtils.info

        coEvery { dataSource.placeOrder(userId, any(), any()) } returns
                FirebaseResult.UnknownError(Exception("Unknown error"))

        val result = repository.placeOrder(userId, items, info)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).message == "Order placement failed: Unknown error")

        coVerify(exactly = 1) { dataSource.placeOrder(userId, items, info) }
    }

    @Test
    fun givenGetCurrentUserId_whenCalled_returnsUserId() {
        val userId = "userId123"
        every { dataSource.getCurrentUserId() } returns userId

        val result = repository.getCurrentUserId()

        assert(result == userId)

        verify(exactly = 1) { dataSource.getCurrentUserId() }
    }

    @Test
    fun givenGetCurrentUserId_whenCalled_returnsNull() {
        every { dataSource.getCurrentUserId() } returns null

        val result = repository.getCurrentUserId()

        assert(result == null)

        verify(exactly = 1) { dataSource.getCurrentUserId() }
    }

}