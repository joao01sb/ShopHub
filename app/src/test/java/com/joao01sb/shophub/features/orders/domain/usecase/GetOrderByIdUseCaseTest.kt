package com.joao01sb.shophub.features.orders.domain.usecase

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
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

class GetOrderByIdUseCaseTest {

    private val ordersRepository = mockk<OrdersRepository>(relaxed = true)
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    @Before
    fun setup() {
        clearAllMocks()
        getOrderByIdUseCase = GetOrderByIdUseCase(ordersRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetOrderByIdUseCase_whenRepositoryReturnsSuccess_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val mockOrder = mockk<Order>()
        val successResult = DomainResult.Success(mockOrder)

        coEvery { ordersRepository.getOrderById(userId, orderId) } returns successResult

        val result = getOrderByIdUseCase.invoke(userId, orderId)

        assert(result is DomainResult.Success)
        val order = (result as DomainResult.Success).data
        assert(order == mockOrder)
        coVerify(exactly = 1) { ordersRepository.getOrderById(userId, orderId) }
    }

    @Test
    fun givenGetOrderByIdUseCase_whenRepositoryReturnsError_thenReturnError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val errorMessage = "Order not found"
        val errorResult = DomainResult.Error(
            message = errorMessage,
            type = ErrorType.DATABASE
        )

        coEvery { ordersRepository.getOrderById(userId, orderId) } returns errorResult

        val result = getOrderByIdUseCase.invoke(userId, orderId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.DATABASE)
        coVerify(exactly = 1) { ordersRepository.getOrderById(userId, orderId) }
    }

    @Test
    fun givenGetOrderByIdUseCase_whenRepositoryReturnsSuccess_withNull_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val successResult = DomainResult.Success<Order?>(null)

        coEvery { ordersRepository.getOrderById(userId, orderId) } returns successResult

        val result = getOrderByIdUseCase.invoke(userId, orderId)

        assert(result is DomainResult.Success)
        val order = (result as DomainResult.Success).data
        assert(order == null)
        coVerify(exactly = 1) { ordersRepository.getOrderById(userId, orderId) }
    }
}