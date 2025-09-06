package com.joao01sb.shophub.features.home.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.repository.ProductRepository
import com.joao01sb.shophub.features.home.utils.collectDataForTest
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllproductsUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var getAllProductsUseCase: GetAllproductsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getAllProductsUseCase = GetAllproductsUseCase(repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun givenInvoke_whenCalled_thenCallRepositoryGetAllProducts() = runTest {
        val products = MockUtils.productsDto.map { it.toDomain() }
        val expectedCount = products.size
        val paging = PagingData.from(products)

        every { repository.getAllProducts() } returns flowOf(paging)

        getAllProductsUseCase().test {
            val emission = awaitItem()

            assert(emission is PagingData<Product>)

            val items = emission.collectDataForTest(testDispatcher)
            assertEquals(expectedCount, items.size)
            assertEquals(products, items)
            awaitComplete()
        }
    }

    @Test
    fun givenInvoke_whenCalled_thenReturnEmptyList() = runTest {
        every { repository.getAllProducts() } returns flowOf(PagingData.from(emptyList()))

        getAllProductsUseCase().test {
            val emission = awaitItem()

            assert(emission is PagingData<Product>)

            val items = emission.collectDataForTest(testDispatcher)
            assertTrue("List should be empty", items.isEmpty())
            assertEquals(0, items.size)

            awaitComplete()
        }
    }

    @Test
    fun givenInvoke_whenCalled_thenReturnError() = runTest {
        every { repository.getAllProducts() } throws IOException("Network Error")

        try {
            getAllProductsUseCase().test {
                awaitItem()
            }
        } catch (e: Exception) {
            assertEquals("Network Error", e.message)
        }
    }

}