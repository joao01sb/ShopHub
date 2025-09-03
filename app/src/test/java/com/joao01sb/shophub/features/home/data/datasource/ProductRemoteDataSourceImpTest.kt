package com.joao01sb.shophub.features.home.data.datasource

import android.accounts.NetworkErrorException
import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.data.remote.service.ApiService
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.result.network.ApiResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

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

    @Test
    fun givenGetAllProdcts_whenCalled_thenReturnEmptyList() = runTest {
        val response = PaginatedProductsResponse(
            total = 0,
            skip = 0,
            limit = 3,
            products = listOf<ProductDto>()
        )

        coEvery { apiService.getAllProducts(any(), any()) } returns response
        val result = remoteDataSource.getAllProducts(limit = 3, skip = 0)

        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == response)
        assert(result.data.products.isEmpty())
        coVerify { apiService.getAllProducts(limit = 3, skip = 0) }

    }

    @Test
    fun givenGetAllProducts_whenApiServiceThrowsException_thenReturnApiResultError() = runTest {
        coEvery { apiService.getAllProducts(any(), any()) } throws NetworkErrorException("Network error")
        val result = remoteDataSource.getAllProducts(limit = 3, skip = 0)

        assert(result is ApiResult.UnknownError)
        assert((result as ApiResult.UnknownError).exception is NetworkErrorException)
        coVerify { apiService.getAllProducts(limit = 3, skip = 0) }
    }

    @Test
    fun givenGetProductById_whenCalled_thenCallApiService() = runTest {
        val product = MockUtils.productsDto.first { it.id == 1 }

        coEvery { apiService.getProductById(1) } returns product
        val result = remoteDataSource.getProductById(1)

        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == product)
        coVerify { apiService.getProductById(1) }
    }

    @Test
    fun givenGetProductById_whenProductNotFound_thenReturnApiResultError() = runTest {
        coEvery { apiService.getProductById(999) } throws HttpException(
            retrofit2.Response.error<Any>(400, ResponseBody.create(null, "Not Found"))
        )
        val result = remoteDataSource.getProductById(999)
        assert(result is ApiResult.HttpError)
        assert((result as ApiResult.HttpError).code == 400)
        coVerify { apiService.getProductById(999) }
    }

    @Test
    fun givenGetProductById_whenApiServiceThrowsException_thenReturnApiResultError() = runTest {
        coEvery { apiService.getProductById(1) } throws NetworkErrorException("Network error")
        val result = remoteDataSource.getProductById(1)

        assert(result is ApiResult.UnknownError)
        assert((result as ApiResult.UnknownError).exception is NetworkErrorException)
        coVerify { apiService.getProductById(1) }
    }

    @Test
    fun givenSearchProducts_whenCalled_thenCallApiService() = runTest {
        val query = "Product"
        val response = PaginatedProductsResponse(
            total = 3,
            skip = 0,
            limit = 3,
            products = MockUtils.productsDto
        )

        coEvery { apiService.searchProducts(query, 0, 3) } returns response
        val result = remoteDataSource.searchProducts(query, skip = 0, limit = 3)

        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == response)
        coVerify { apiService.searchProducts(query, 0, 3) }
    }

    @Test
    fun givenSearchProducts_whenNoResults_thenReturnEmptyList() = runTest {
        val query = "NonExistentProduct"
        val response = PaginatedProductsResponse(
            total = 0,
            skip = 0,
            limit = 3,
            products = listOf<ProductDto>()
        )

        coEvery { apiService.searchProducts(query, 0, 3) } returns response
        val result = remoteDataSource.searchProducts(query, skip = 0, limit = 3)

        assert(result is ApiResult.Success)
        assert((result as ApiResult.Success).data == response)
        assert(result.data.products.isEmpty())
        coVerify { apiService.searchProducts(query, 0, 3) }
    }

    @Test
    fun givenSearchProducts_whenApiServiceThrowsException_thenReturnApiResultError() = runTest {
        val query = "Product"
        coEvery {
            apiService.searchProducts(
                query,
                0,
                3
            )
        } throws NetworkErrorException("Network error")
        val result = remoteDataSource.searchProducts(query, skip = 0, limit = 3)
        assert(result is ApiResult.UnknownError)
        assert((result as ApiResult.UnknownError).exception is NetworkErrorException)
        coVerify { apiService.searchProducts(query, 0, 3) }
    }

}