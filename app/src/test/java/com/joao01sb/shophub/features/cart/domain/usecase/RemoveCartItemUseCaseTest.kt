package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.fake.FakeCartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveCartItemUseCaseTest {

    private val cartRepository = FakeCartRepository()

    private lateinit var removeCartItemUseCase: RemoveCartItemUseCase

    @Before
    fun setUp() {
        cartRepository.resetCounters()
        removeCartItemUseCase = RemoveCartItemUseCase(cartRepository)
    }

    @Test
    fun givenValidInput_whenRemoveCartItem_thenCallRepository() = runTest {
        val userId = "user1"
        val productId = "product1"

        val result = removeCartItemUseCase(userId, productId)

        assert(result is DomainResult.Success)
        assert(cartRepository.getRemoveItemCallCount() == 1)
    }

    @Test
    fun givenErrorState_whenRemoveCartItem_thenCallRepository() = runTest {
        val userId = "user1"
        val productId = "product1"

        cartRepository.setErrorState(true)

        val result = removeCartItemUseCase(userId, productId)

        assert(result is DomainResult.Error)
        assert(cartRepository.getRemoveItemCallCount() == 1)
    }

}