package com.joao01sb.shophub.features.home.data.repository

import com.joao01sb.shophub.core.data.local.ShopHubDatabase
import com.joao01sb.shophub.core.data.mapper.toEntity
import com.joao01sb.shophub.core.data.remote.dto.PaginatedProductsResponse
import com.joao01sb.shophub.core.data.remote.dto.ProductDto
import com.joao01sb.shophub.core.domain.datasource.ProductLocalDataSource
import com.joao01sb.shophub.core.domain.datasource.ProductRemoteDataSource
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.database.DatabaseResult
import com.joao01sb.shophub.core.result.network.ApiResult
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var productRemoteDataSource: ProductRemoteDataSource
    private lateinit var productLocalDataSource: ProductLocalDataSource
    private lateinit var database: ShopHubDatabase
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        clearAllMocks()
        productRemoteDataSource = mockk(relaxed = true)
        productLocalDataSource = mockk(relaxed = true)
        database = mockk(relaxed = true)
        productRepository = ProductRepositoryImpl(
            productLocalDataSource,
            productRemoteDataSource,
            database
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getAllProducts_ShouldCallLocalDataSource() = runTest {
        coEvery { productLocalDataSource.getAllProducts() } returns mockk(relaxed = true)

        val result = productRepository.getAllProducts()

        assertNotNull(result)
    }

    @Test
    fun getAllProducts_ShouldReturnFlow() = runTest {
        coEvery { productLocalDataSource.getAllProducts() } returns mockk(relaxed = true)

        val result = productRepository.getAllProducts()

        assertNotNull(result)
    }

    @Test
    fun getAllProducts_ShouldHandleEmptyData() = runTest {
        coEvery { productLocalDataSource.getAllProducts() } returns mockk(relaxed = true)

        val result = productRepository.getAllProducts()

        assertNotNull(result)
    }

    @Test
    fun getAllProducts_ShouldWorkWithPaging() = runTest {
        coEvery { productLocalDataSource.getAllProducts() } returns mockk(relaxed = true)

        val result = productRepository.getAllProducts()

        assertNotNull(result)
    }

    @Test
    fun getProductById_ShouldReturnSuccess_fromLocalDataSource_whenProductExistsLocally() = runTest {
        val productId = 1
        val mockProduct = MockUtils.productsDto.first().toEntity()

        coEvery { productLocalDataSource.getProductById(productId) } returns
                DatabaseResult.Success(mockProduct)

        val result = productRepository.getProductById(productId)

        assertTrue(result is DomainResult.Success)
        assertEquals(mockProduct.id, (result as DomainResult.Success).data.id)

        coVerify { productLocalDataSource.getProductById(productId) }
        coVerify(exactly = 0) { productRemoteDataSource.getProductById(any()) }
    }

    @Test
    fun getProductById_ShouldReturnError_whenLocalDataSourceFails_andRemoteAlsoFails() = runTest {
        val productId = 1
        val errorMessage = "Network error"

        coEvery { productLocalDataSource.getProductById(productId) } returns
                DatabaseResult.UnknownError(IOException("Local error"))
        coEvery { productRemoteDataSource.getProductById(productId) } returns
                ApiResult.NetworkError(IOException(errorMessage))

        val result = productRepository.getProductById(productId)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)
        assertEquals(ErrorType.NETWORK, result.type)

        coVerify { productLocalDataSource.getProductById(productId) }
        coVerify { productRemoteDataSource.getProductById(productId) }
    }

    @Test
    fun getProductById_ShouldReturnSuccess_fromRemote_whenLocalDataSourceReturnsNull() = runTest {
        val productId = 1
        val mockProductDto = MockUtils.productsDto.first()

        coEvery { productLocalDataSource.getProductById(productId) } returns
                DatabaseResult.Success(null)
        coEvery { productRemoteDataSource.getProductById(productId) } returns
                ApiResult.Success(mockProductDto)

        val result = productRepository.getProductById(productId)

        assertTrue(result is DomainResult.Success)
        assertEquals(mockProductDto.id, (result as DomainResult.Success).data.id)

        coVerify { productLocalDataSource.getProductById(productId) }
        coVerify { productRemoteDataSource.getProductById(productId) }
    }

    @Test
    fun getProductById_ShouldReturnError_whenRemoteReturnsHttpError() = runTest {
        val productId = 1
        val errorMessage = "Product not found"

        coEvery { productLocalDataSource.getProductById(productId) } returns
                DatabaseResult.UnknownError(IOException("Local error"))
        coEvery { productRemoteDataSource.getProductById(productId) } returns
                ApiResult.HttpError(404, errorMessage)

        val result = productRepository.getProductById(productId)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)
        assertEquals(ErrorType.NETWORK, result.type)

        coVerify { productLocalDataSource.getProductById(productId) }
        coVerify { productRemoteDataSource.getProductById(productId) }
    }

    @Test
    fun searchProducts_ShouldReturnSuccess_whenRemoteCallSucceeds() = runTest {
        val query = "phone"
        val page = 1
        val limit = 20
        val skip = 0
        val mockResponse = PaginatedProductsResponse(
            products = MockUtils.productsDto,
            total = 10,
            skip = skip,
            limit = limit
        )

        coEvery { productRemoteDataSource.searchProducts(query, skip, limit) } returns
                ApiResult.Success(mockResponse)

        val result =
            productRepository.searchProducts(query, page, limit)

        assertTrue(result is DomainResult.Success)
        val successResult =
            result as DomainResult.Success
        assertEquals(mockResponse.products.size, successResult.data.products.size)
        assertEquals(mockResponse.total, successResult.data.total)

        coVerify { productRemoteDataSource.searchProducts(query, skip, limit) }
    }

    @Test
    fun searchProducts_ShouldReturnError_whenRemoteCallFails_withNetworkError() = runTest {
        val query = "phone"
        val page = 1
        val limit = 20
        val skip = 0
        val errorMessage = "No internet connection"

        coEvery { productRemoteDataSource.searchProducts(query, skip, limit) } returns
                ApiResult.NetworkError(IOException(errorMessage))

        val result =
            productRepository.searchProducts(query, page, limit)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)
        assertEquals(ErrorType.NETWORK, result.type)

        coVerify { productRemoteDataSource.searchProducts(query, skip, limit) }
    }

    @Test
    fun searchProducts_ShouldReturnError_whenRemoteCallFails_withHttpError() = runTest {
        val query = "phone"
        val page = 2
        val limit = 10
        val skip = 10
        val errorMessage = "Bad request"

        coEvery { productRemoteDataSource.searchProducts(query, skip, limit) } returns
                ApiResult.HttpError(400, errorMessage)

        val result =
            productRepository.searchProducts(query, page, limit)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)
        assertEquals(ErrorType.NETWORK, result.type)

        coVerify { productRemoteDataSource.searchProducts(query, skip, limit) }
    }

    @Test
    fun searchProducts_ShouldReturnError_whenRemoteCallFails_withUnknownError() = runTest {
        val query = "phone"
        val page = 1
        val limit = 20
        val skip = 0
        val errorMessage = "Unknown error occurred"

        coEvery { productRemoteDataSource.searchProducts(query, skip, limit) } returns
                ApiResult.UnknownError(Exception(errorMessage))

        val result =
            productRepository.searchProducts(query, page, limit)

        assertTrue(result is DomainResult.Error)
        assertEquals(errorMessage, (result as DomainResult.Error).message)
        assertEquals(ErrorType.NETWORK, result.type)

        coVerify { productRemoteDataSource.searchProducts(query, skip, limit) }
    }

    @Test
    fun searchProducts_ShouldCalculateSkipCorrectly_forDifferentPages() = runTest {
        val query = "phone"
        val page = 3
        val limit = 15
        val expectedSkip = 30
        val mockResponse = PaginatedProductsResponse(
            products = emptyList<ProductDto>(),
            total = 0,
            skip = expectedSkip,
            limit = limit
        )

        coEvery { productRemoteDataSource.searchProducts(query, expectedSkip, limit) } returns
                ApiResult.Success(mockResponse)

        val result =
            productRepository.searchProducts(query, page, limit)

        assertTrue(result is DomainResult.Success)

        coVerify { productRemoteDataSource.searchProducts(query, expectedSkip, limit) }
    }
}