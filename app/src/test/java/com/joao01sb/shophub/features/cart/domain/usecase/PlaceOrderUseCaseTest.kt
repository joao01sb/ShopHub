package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils
import com.joao01sb.shophub.features.cart.data.fake.FakeCartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PlaceOrderUseCaseTest {

    private val cartRepository = FakeCartRepository()

    private lateinit var placeOrderUseCase: PlaceOrderUseCase

    @Before
    fun setUp() {
        cartRepository.resetCounters()
        placeOrderUseCase = PlaceOrderUseCase(cartRepository)
    }

    @Test
    fun givenValidInput_whenPlaceOrder_thenReturnSuccess() = runTest {
        val userId = "user1"
        val items = MockUtils.mockCartItems
        val info = MockUtils.info

        val result = placeOrderUseCase(userId, items, info)

        assert(result is DomainResult.Success)
        assert(cartRepository.getPlaceOrderCallCount() == 1)
    }

    @Test
    fun givenErrorState_whenPlaceOrder_thenReturnError() = runTest {
        val userId = "user1"
        val items = MockUtils.mockCartItems
        val info = MockUtils.info

        cartRepository.setErrorState(true)

        val result = placeOrderUseCase(userId, items, info)

        assert(result is DomainResult.Error)
        assert(cartRepository.getPlaceOrderCallCount() == 1)
    }

}