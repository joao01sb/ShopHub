package com.joao01sb.shophub.features.home.presentation.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.joao01sb.shophub.core.data.mapper.toDomain
import com.joao01sb.shophub.core.domain.model.Product
import com.joao01sb.shophub.features.home.data.datasource.MockUtils
import com.joao01sb.shophub.features.home.domain.usecase.GetAllproductsUseCase
import com.joao01sb.shophub.features.home.utils.collectDataForTest
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getAllproductsUseCase: GetAllproductsUseCase

    @Before
    fun setup() {
        clearAllMocks()
        Dispatchers.setMain(testDispatcher)
        getAllproductsUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun givenViewModel_whenInit_thenCallGetAllProductsUseCase() = runTest {
        val mockProducts = MockUtils.productsDto.map { it.toDomain() }
        val expectedPagingData = PagingData.from(mockProducts)

        every { getAllproductsUseCase.invoke() } answers {
            flowOf(expectedPagingData)
        }

        val viewModel = HomeViewModel(getAllproductsUseCase)

        viewModel.products.test {
            advanceUntilIdle()
            val pagingData = awaitItem()

            val items = pagingData.collectDataForTest(testDispatcher)
            assertEquals("Should have correct number of products", mockProducts.size, items.size)
            assertEquals("Should have correct products", mockProducts, items)
        }

        verify { getAllproductsUseCase.invoke() }
    }

    @Test
    fun givenViewModel_whenUseCaseReturnsEmpty_thenEmitEmptyPagingData() = runTest {
        val emptyPagingData = PagingData.from(emptyList<Product>())

        every { getAllproductsUseCase.invoke() } returns flowOf(emptyPagingData)

        val viewModel = HomeViewModel(getAllproductsUseCase)

        viewModel.products.test {
            advanceUntilIdle()

            val pagingData = awaitItem()

            val items = pagingData.collectDataForTest(testDispatcher)
            assertTrue("Should be empty", items.isEmpty())
            assertEquals("Should have 0 items", 0, items.size)

            cancelAndIgnoreRemainingEvents()
        }

        verify { getAllproductsUseCase.invoke() }
    }

    @Test
    fun givenViewModel_whenUseCaseThrowsException_thenHandleError() = runTest {
        val exception = RuntimeException("Network error")

        every { getAllproductsUseCase.invoke() } throws exception

        try {
            val viewModel = HomeViewModel(getAllproductsUseCase)
            advanceUntilIdle()
            verify { getAllproductsUseCase.invoke() }
        } catch (e: Exception) {
            assertEquals("Should have correct error message", "Network error", e.message)
        }
    }
}