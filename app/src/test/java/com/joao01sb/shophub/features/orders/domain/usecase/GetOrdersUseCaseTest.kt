package com.joao01sb.shophub.features.orders.domain.usecase

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.orders.data.datasource.MockUtils
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetOrdersUseCaseTest {

    private val ordersRepository = mockk<OrdersRepository>(relaxed = true)
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @Before
    fun setup() {
        clearAllMocks()
        getOrdersUseCase = GetOrdersUseCase(ordersRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetOrdersUseCase_whenRepositoryReturnsSuccess_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val mockOrders = MockUtils.orders
        val successResult = DomainResult.Success(mockOrders)

        coEvery { ordersRepository.getOrders(userId) } returns successResult

        val result = getOrdersUseCase.invoke(userId)

        assert(result is DomainResult.Success)
        val orders = (result as DomainResult.Success).data
        assert(orders.size == mockOrders.size)
        coVerify(exactly = 1) { ordersRepository.getOrders(userId) }
    }

    @Test
    fun givenGetOrdersUseCase_whenRepositoryReturnsSuccess_withEmptyList_thenReturnSuccess() =
        runTest {
            val userId = "userId123"
            val successResult = DomainResult.Success(emptyList<Order>())

            coEvery { ordersRepository.getOrders(userId) } returns successResult

            val result = getOrdersUseCase.invoke(userId)

            assert(result is DomainResult.Success)
            val orders = (result as DomainResult.Success).data
            assert(orders.isEmpty())
            coVerify(exactly = 1) { ordersRepository.getOrders(userId) }
        }

    @Test
    fun givenGetOrdersUseCase_whenRepositoryReturnsError_thenReturnError() = runTest {
        val userId = "userId123"
        val errorMessage = "Database error"
        val errorResult = DomainResult.Error(
            message = errorMessage,
            type = ErrorType.DATABASE
        )

        coEvery { ordersRepository.getOrders(userId) } returns errorResult

        val result = getOrdersUseCase.invoke(userId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.DATABASE)
        coVerify(exactly = 1) { ordersRepository.getOrders(userId) }
    }

    @Test
    fun givenGetOrdersUseCase_whenRepositoryReturnsAuthError_thenReturnAuthError() = runTest {
        val userId = "userId123"
        val errorMessage = "Authentication required"
        val errorResult = DomainResult.Error(
            message = errorMessage,
            type = ErrorType.AUTHENTICATION
        )

        coEvery { ordersRepository.getOrders(userId) } returns errorResult

        val result = getOrdersUseCase.invoke(userId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.AUTHENTICATION)
        coVerify(exactly = 1) { ordersRepository.getOrders(userId) }
    }
}