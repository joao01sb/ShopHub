package com.joao01sb.shophub.features.home.data.datasource

import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.service.ApiService
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.result.network.ApiResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductRemoteDataSourceImpTest {

    private lateinit var remoteDataSource: ProductRemoteDataSource
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        clearAllMocks()
        apiService = mockk<ApiService>(relaxed = true)
        remoteDataSource = ProductRemoteDataSourceImp(apiService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetAllProducts_whenCalled_thenCallApiService() = runTest {
        val response = PaginatedProductsResponse(
            total = 3,
            skip = 0,
            limit = 3,
            products = MockUtils.productsDto
        )

        coEvery { apiService.getAllProducts(any(), any()) } returns response
        val result = remoteDataSource.getAllProducts(limit = 3, skip = 0)

        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == response)
        coVerify { apiService.getAllProducts(limit = 3, skip = 0) }

    }

}