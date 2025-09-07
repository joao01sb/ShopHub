package com.joao01sb.shophub.features.orders.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RemoteOrdersDataSourceImpTest {

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val mockCollectionReference = mockk<CollectionReference>(relaxed = true)
    private val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
    private val mockQuerySnapshot = mockk<QuerySnapshot>()
    private val mockDocumentSnapshot = mockk<DocumentSnapshot>()
    private val mockQueryTask = mockk<Task<QuerySnapshot>>(relaxed = true)
    private val mockDocTask = mockk<Task<DocumentSnapshot>>(relaxed = true)

    private lateinit var remoteOrdersDataSource: RemoteOrdersDataSourceImp

    @Before
    fun setup() {
        clearAllMocks()
        remoteOrdersDataSource = RemoteOrdersDataSourceImp(firestore, firebaseAuth)

        every { firestore.collection("users") } returns mockCollectionReference
        every { mockCollectionReference.document(any()) } returns mockDocumentReference
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenGetOrders_whenCalled_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val mockOrder1 = createMockOrder("order1")
        val mockOrder2 = createMockOrder("order2")
        val mockOrderDocuments = listOf(
            mockk<DocumentSnapshot> { every { toObject(Order::class.java) } returns mockOrder1 },
            mockk<DocumentSnapshot> { every { toObject(Order::class.java) } returns mockOrder2 }
        )

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockQueryTask
        every { mockQueryTask.isSuccessful } returns true
        every { mockQueryTask.isComplete } returns true
        every { mockQueryTask.exception } returns null
        every { mockQueryTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns mockOrderDocuments

        val result = remoteOrdersDataSource.getOrders(userId)

        assert(result is FirebaseResult.Success)
        val orders = (result as FirebaseResult.Success).data
        assert(orders.size == 2)
        assert(orders[0].id == "order1")
        assert(orders[1].id == "order2")
        verify(exactly = 1) { mockCollectionReference.get() }
    }

    @Test
    fun givenGetOrders_whenFirestoreFails_thenReturnFirebaseError() = runTest {
        val userId = "userId123"
        val exception = FirebaseFirestoreException(
            "Permission denied",
            FirebaseFirestoreException.Code.PERMISSION_DENIED
        )

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockQueryTask
        every { mockQueryTask.isSuccessful } returns false
        every { mockQueryTask.isComplete } returns true
        every { mockQueryTask.isCanceled } returns false
        every { mockQueryTask.exception } returns exception

        val result = remoteOrdersDataSource.getOrders(userId)

        assert(result is FirebaseResult.FirebaseError)
        val error = result as FirebaseResult.FirebaseError
        assert(error.message == "Permission denied")
        verify(exactly = 1) { mockCollectionReference.get() }
    }

    @Test
    fun givenGetOrders_whenDocumentMappingFails_thenReturnSuccess_withEmptyList() = runTest {
        val userId = "userId123"
        val mockOrderDocuments = listOf(
            mockk<DocumentSnapshot> { every { toObject(Order::class.java) } returns null }
        )

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockQueryTask
        every { mockQueryTask.isSuccessful } returns true
        every { mockQueryTask.isComplete } returns true
        every { mockQueryTask.exception } returns null
        every { mockQueryTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns mockOrderDocuments

        val result = remoteOrdersDataSource.getOrders(userId)

        assert(result is FirebaseResult.Success)
        val orders = (result as FirebaseResult.Success).data
        assert(orders.isEmpty())
    }

    @Test
    fun givenGetCurrentUserId_whenUserIsLoggedIn_thenReturnUserId() {
        val userId = "userId123"
        val mockUser = mockk<FirebaseUser>()
        every { firebaseAuth.currentUser } returns mockUser
        every { mockUser.uid } returns userId

        val result = remoteOrdersDataSource.getCurrentUserId()

        assert(result == userId)
        verify(exactly = 1) { firebaseAuth.currentUser }
        verify(exactly = 1) { mockUser.uid }
    }

    @Test
    fun givenGetCurrentUserId_whenNoUserLoggedIn_thenReturnNull() {
        every { firebaseAuth.currentUser } returns null

        val result = remoteOrdersDataSource.getCurrentUserId()

        assert(result == null)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenGetOrderById_whenDocumentExists_thenReturnSuccess() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val mockOrder = createMockOrder(orderId)
        val mockData = mapOf("id" to orderId, "status" to "PENDING")

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.document(orderId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockDocTask
        every { mockDocTask.isSuccessful } returns true
        every { mockDocTask.isComplete } returns true
        every { mockDocTask.exception } returns null
        every { mockDocTask.result } returns mockDocumentSnapshot
        every { mockDocumentSnapshot.exists() } returns true
        every { mockDocumentSnapshot.data } returns mockData
        every { mockDocumentSnapshot.toObject(Order::class.java) } returns mockOrder

        val result = remoteOrdersDataSource.getOrderById(userId, orderId)

        assert(result is FirebaseResult.Success)
        val order = (result as FirebaseResult.Success).data
        assert(order?.id == orderId)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun givenGetOrderById_whenDocumentDoesNotExist_thenReturnSuccess_withNull() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.document(orderId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockDocTask
        every { mockDocTask.isSuccessful } returns true
        every { mockDocTask.isComplete } returns true
        every { mockDocTask.exception } returns null
        every { mockDocTask.result } returns mockDocumentSnapshot
        every { mockDocumentSnapshot.exists() } returns false
        every { mockDocumentSnapshot.toObject(Order::class.java) } returns null

        val result = remoteOrdersDataSource.getOrderById(userId, orderId)

        assert(result is FirebaseResult.Success)
        val order = (result as FirebaseResult.Success).data
        assert(order == null)
    }

    @Test
    fun givenGetOrderById_whenDocumentDataIsEmpty_thenReturnUnknownError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.document(orderId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockDocTask
        every { mockDocTask.isSuccessful } returns true
        every { mockDocTask.isComplete } returns true
        every { mockDocTask.exception } returns null
        every { mockDocTask.result } returns mockDocumentSnapshot
        every { mockDocumentSnapshot.exists() } returns true
        every { mockDocumentSnapshot.data } returns emptyMap()

        val result = remoteOrdersDataSource.getOrderById(userId, orderId)

        assert(result is FirebaseResult.UnknownError)
        val error = result as FirebaseResult.UnknownError
        assert(error.exception.message == "Order data is empty")
    }

    @Test
    fun givenGetOrderById_whenFirestoreFails_thenReturnFirebaseError() = runTest {
        val userId = "userId123"
        val orderId = "orderId456"
        val exception = FirebaseFirestoreException(
            "Network error",
            FirebaseFirestoreException.Code.UNAVAILABLE
        )

        every { mockDocumentReference.collection("orders") } returns mockCollectionReference
        every { mockCollectionReference.document(orderId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockDocTask
        every { mockDocTask.isSuccessful } returns false
        every { mockDocTask.isComplete } returns true
        every { mockDocTask.isCanceled } returns false
        every { mockDocTask.exception } returns exception

        val result = remoteOrdersDataSource.getOrderById(userId, orderId)

        assert(result is FirebaseResult.FirebaseError)
        val error = result as FirebaseResult.FirebaseError
        assert(error.message == "Network error")
    }

    private fun createMockOrder(orderId: String): Order {
        return mockk<Order> {
            every { id } returns orderId
        }
    }
}