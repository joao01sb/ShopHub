package com.joao01sb.shophub.features.home.domain.usecase

import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class SearchProductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: SearchProductsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = SearchProductsUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositorySearchProducts() = runTest {
        val query = "Product 3"
        val limit = 1
        val product = MockUtils.productsDto.filter { it.nameInApi == query }
        val paginedResult = PaginatedProductsResponse(
            total = 1,
            skip = 0,
            limit = 1,
            products = product
        )

        coEvery { repository.searchProducts(query, limit) } returns DomainResult.Success(paginedResult)
        val result = useCase(query, limit)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data.results.size == paginedResult.products.size)
        assert(result.data.results.first().name == product.first().toDomain().name)
        assertFalse(result.data.hasMore)
        coEvery { repository.searchProducts(query, limit) }
    }

    @Test
    fun givenInvoke_whenCalledAndNoProductsFound_thenReturnEmptyList() = runTest {
        val query = "NonExistingProduct"
        val limit = 1
        val paginedResult = PaginatedProductsResponse(
            total = 0,
            skip = 0,
            limit = 1,
            products = emptyList<ProductDto>()
        )

        coEvery { repository.searchProducts(query, limit) } returns DomainResult.Success(paginedResult)
        val result = useCase(query, limit)

        assert(result is DomainResult.Success)
        assert((result as DomainResult.Success).data.results.isEmpty())
        assertFalse(result.data.hasMore)
        coEvery { repository.searchProducts(query, limit) }
    }

    @Test
    fun givenInvoke_whenCalledAndRepositoryThrowsException_thenReturnDomainResultError() = runTest {
        val query = "Product 3"
        val limit = 1

        coEvery { repository.searchProducts(query, limit) } returns DomainResult.Error(
            "DB Error",
            ErrorType.UNKNOWN
        )
        val result = useCase(query, limit)

        assert(result is DomainResult.Error)
        assert((result as DomainResult.Error).type == ErrorType.UNKNOWN)
        coEvery { repository.searchProducts(query, limit) }

    }

}