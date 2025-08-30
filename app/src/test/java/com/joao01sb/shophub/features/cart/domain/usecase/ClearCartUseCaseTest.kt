package com.joao01sb.shophub.features.cart.domain.usecase

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.cart.data.fake.FakeCartRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearCartUseCaseTest {

    private lateinit var clearCartUseCase: ClearCartUseCase
    private var cartRepository = FakeCartRepository()

    @Before
    fun setup() {
        cartRepository.resetCounters()
        clearCartUseCase = ClearCartUseCase(cartRepository)
    }

    @Test
    fun givenValidUserId_whenClearCart_thenSuccess() = runTest {
        val userId = "user123"

        val result = clearCartUseCase(userId)

        assert(result is DomainResult.Success)
        assert(1 == cartRepository.getClearCartCallCount())
    }

    @Test
    fun givenInvalidUserId_whenClearCart_thenError() = runTest {
        val userId = ""

        cartRepository.setErrorState(true)
        val result = clearCartUseCase(userId)

        assert(result is DomainResult.Error && result.type == ErrorType.UNKNOWN)
        assert(1 == cartRepository.getClearCartCallCount())
    }

}