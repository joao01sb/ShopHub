package com.joao01sb.shophub.features.cart.domain.usecase

import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestoreException
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils
import com.joao01sb.shophub.features.cart.data.fake.FakeCartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCartItemsUseCaseTest {

    private val cartRepository = FakeCartRepository()

    private lateinit var getCartItemsUseCase: GetCartItemsUseCase

    @Before
    fun setUp() {
        cartRepository.resetCounters()
        getCartItemsUseCase = GetCartItemsUseCase(cartRepository)
    }

    @Test
    fun givenEmptyCart_whenGetCartItems_thenReturnEmptyList() = runTest {
        val userId = "user1"

        getCartItemsUseCase(userId).test {
            val items = awaitItem()
            assert(items.isEmpty())
            awaitComplete()
        }

        assert(cartRepository.getObserveCartItemsCallCount() == 1)

    }

    @Test
    fun givenNonEmptyCart_whenGetCartItems_thenReturnSortedItems() = runTest {
        val userId = "user1"

        cartRepository.setCartItems(MockUtils.mockCartItems)

        getCartItemsUseCase(userId).test {
            val items = awaitItem()
            assert(items.size == MockUtils.mockCartItems.size)
            assert(items == MockUtils.mockCartItems.sortedBy { it.name })
            awaitComplete()
        }

        assert(cartRepository.getObserveCartItemsCallCount() == 1)
    }

    @Test
    fun givenErrorState_whenGetCartItems_thenThrowException() = runTest {
        val userId = "user1"

        cartRepository.setErrorState(true)

        getCartItemsUseCase(userId).test {
            val error = awaitError()
            assert(error is FirebaseFirestoreException)
            assert(error.message == "Error observing cart items")
        }

        assert(cartRepository.getObserveCartItemsCallCount() == 1)
    }

}