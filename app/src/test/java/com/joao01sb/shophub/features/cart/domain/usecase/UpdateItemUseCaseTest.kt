package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.datasource.MockUtils
import com.joao01sb.shophub.features.cart.data.fake.FakeCartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateItemUseCaseTest {

    val cartRepository = FakeCartRepository()
    lateinit var updateItemUseCase: UpdateItemUseCase

    @Before
    fun setUp() {
        cartRepository.resetCounters()
        updateItemUseCase = UpdateItemUseCase(cartRepository)
    }

    @Test
    fun givenValidInput_whenUpdateItem_thenCallRepository() = runTest {
        val userId = "user1"
        val product = MockUtils.mockCartItems.first()
        val newQuantity = 3

        val result = updateItemUseCase(userId, product, newQuantity)

        assert(result is DomainResult.Success)
        assert(cartRepository.getUpdateItemCallCount() == 1)
        assert(cartRepository.getLastUpdatedItem() == product.copy(quantity = newQuantity))
    }

    @Test
    fun givenErrorState_whenUpdateItem_thenReturnError() = runTest {
        val userId = "user1"
        val product = MockUtils.mockCartItems.first()
        val newQuantity = 3

        cartRepository.setErrorState(true)

        val result = updateItemUseCase(userId, product, newQuantity)

        assert(result is DomainResult.Error)
        assert(cartRepository.getUpdateItemCallCount() == 1)
    }

}