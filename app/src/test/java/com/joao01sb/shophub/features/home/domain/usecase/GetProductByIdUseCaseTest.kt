package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetProductByIdUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var getProductByIdUseCase: GetProductByIdUseCase

    @Before
    fun setUp() {
        clearAllMocks()
        repository = mockk(relaxed = true)
        getProductByIdUseCase = GetProductByIdUseCase(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositoryGetProductById() = runTest {
        val productId = 1
        val product = MockUtils.productsDto.first { it.id == productId }

        coEvery { repository.getProductById(productId) } returns DomainResult.Success(product.toDomain())
        val result = getProductByIdUseCase(productId)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data.id == product.toDomain().id)
        coEvery { repository.getProductById(productId) }

    }

    @Test
    fun givenInvoke_whenCalledAndRepositoryThrowsException_thenReturnDomainResultError() = runTest {
        val productId = 1

        coEvery { repository.getProductById(productId) } returns
                DomainResult.Error("Error", ErrorType.NETWORK)
        val result = getProductByIdUseCase(productId)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).type == ErrorType.NETWORK)
        coEvery { repository.getProductById(productId) }
    }

}