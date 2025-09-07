package com.joao01sb.shophub.features.orders.data.repository

import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.orders.data.datasource.MockUtils
import com.joao01sb.shophub.features.orders.domain.datasource.RemoteOrdersDataSource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class OrdersRepositoryImpTest {

    private val remoteOrdersDataSource = mockk<RemoteOrdersDataSource>(relaxed = true)
    private lateinit var ordersRepository: OrdersRepositoryImp

    @Before
    fun setup() {
        clearAllMocks()
        ordersRepository = OrdersRepositoryImp(remoteOrdersDataSource)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetOrders_whenDataSourceReturnsSuccess_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val mockOrders = MockUtils.orders

        coEvery { remoteOrdersDataSource.getOrders(userId) } returns FirebaseResult.Success(mockOrders)

        val result = ordersRepository.getOrders(userId)

        assert(result is DomainResult.Success)
        val orders = (result as DomainResult.Success).data
        assert(orders.size == 2)
        assert(orders[0].orderNumber == "1")
        assert(orders[1].orderNumber == "2")
        coVerify(exactly = 1) { remoteOrdersDataSource.getOrders(userId) }
    }

    @Test
    fun givenGetOrders_whenDataSourceReturnsSuccess_withEmptyList_thenReturnSuccess() = runTest {
        val userId = "userId123"

        coEvery { remoteOrdersDataSource.getOrders(userId) } returns FirebaseResult.Success(emptyList())

        val result = ordersRepository.getOrders(userId)

        assert(result is DomainResult.Success)
        val orders = (result as DomainResult.Success).data
        assert(orders.isEmpty())
        coVerify(exactly = 1) { remoteOrdersDataSource.getOrders(userId) }
    }

    @Test
    fun givenGetOrders_whenDataSourceReturnsAuthError_thenReturnAuthError() = runTest {
        val userId = "userId123"
        val errorMessage = "User not authenticated"

        coEvery { remoteOrdersDataSource.getOrders(userId) } returns FirebaseResult.AuthError(errorMessage)

        val result = ordersRepository.getOrders(userId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.AUTHENTICATION)
    }

    @Test
    fun givenGetOrders_whenDataSourceReturnsFirebaseError_thenReturnDatabaseError() = runTest {
        val userId = "userId123"
        val errorMessage = "Database error"
        val codeError = "500"

        coEvery { remoteOrdersDataSource.getOrders(userId) } returns
                FirebaseResult.FirebaseError(codeError, errorMessage)

        val result = ordersRepository.getOrders(userId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.DATABASE)
    }

    @Test
    fun givenGetOrders_whenDataSourceReturnsUnknownError_thenReturnUnknownError() = runTest {
        val userId = "userId123"
        val exception = Exception("Something went wrong")

        coEvery { remoteOrdersDataSource.getOrders(userId) } returns FirebaseResult.UnknownError(exception)

        val result = ordersRepository.getOrders(userId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == "Fetch orders failed: Something went wrong")
        assert(error.type == ErrorType.UNKNOWN)
    }

    @Test
    fun givenGetCurrentUserId_whenDataSourceReturnsUserId_thenReturnUserId() {
        val userId = "userId123"

        every { remoteOrdersDataSource.getCurrentUserId() } returns userId

        val result = ordersRepository.getCurrentUserId()

        assert(result == userId)
        verify(exactly = 1) { remoteOrdersDataSource.getCurrentUserId() }
    }

    @Test
    fun givenGetCurrentUserId_whenDataSourceReturnsNull_thenReturnNull() {
        every { remoteOrdersDataSource.getCurrentUserId() } returns null

        val result = ordersRepository.getCurrentUserId()

        assert(result == null)
        verify(exactly = 1) { remoteOrdersDataSource.getCurrentUserId() }
    }

    @Test
    fun givenGetOrderById_whenDataSourceReturnsSuccess_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val mockOrder = MockUtils.orders.first()

        coEvery { remoteOrdersDataSource.getOrderById(userId, orderId) } returns FirebaseResult.Success(mockOrder)

        val result = ordersRepository.getOrderById(userId, orderId)

        assert(result is DomainResult.Success)
        val order = (result as DomainResult.Success).data
        assert(order?.orderNumber == "1")
        assert(order?.items?.size == 3)
        coVerify(exactly = 1) { remoteOrdersDataSource.getOrderById(userId, orderId) }
    }

    @Test
    fun givenGetOrderById_whenDataSourceReturnsSuccess_withNull_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"

        coEvery { remoteOrdersDataSource.getOrderById(userId, orderId) } returns FirebaseResult.Success(null)

        val result = ordersRepository.getOrderById(userId, orderId)

        assert(result is DomainResult.Success)
        val order = (result as DomainResult.Success).data
        assert(order == null)
        coVerify(exactly = 1) { remoteOrdersDataSource.getOrderById(userId, orderId) }
    }

    @Test
    fun givenGetOrderById_whenDataSourceReturnsAuthError_thenReturnAuthError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val errorMessage = "User not authenticated"

        coEvery { remoteOrdersDataSource.getOrderById(userId, orderId) } returns FirebaseResult.AuthError(errorMessage)

        val result = ordersRepository.getOrderById(userId, orderId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.AUTHENTICATION)
    }

    @Test
    fun givenGetOrderById_whenDataSourceReturnsFirebaseError_thenReturnDatabaseError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val errorMessage = "Database connection failed"
        val codeError = "500"

        coEvery { remoteOrdersDataSource.getOrderById(userId, orderId) } returns
                FirebaseResult.FirebaseError(codeError, errorMessage)

        val result = ordersRepository.getOrderById(userId, orderId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == errorMessage)
        assert(error.type == ErrorType.DATABASE)
    }

    @Test
    fun givenGetOrderById_whenDataSourceReturnsUnknownError_thenReturnUnknownError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val exception = Exception("Unexpected error occurred")

        coEvery { remoteOrdersDataSource.getOrderById(userId, orderId) } returns FirebaseResult.UnknownError(exception)

        val result = ordersRepository.getOrderById(userId, orderId)

        assert(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assert(error.message == "Fetch order failed: Unexpected error occurred")
        assert(error.type == ErrorType.UNKNOWN)
    }
}