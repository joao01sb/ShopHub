package com.joao01sb.shophub.features.cart.data.datasource

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class CartRemoteDataSourceImplTest {

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val mockFirestoreTask = mockk<Task<Void>>(relaxed = true)
    private val mockCollectionReference = mockk<CollectionReference>(relaxed = true)
    private val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
    val mockQuerySnapshot = mockk<QuerySnapshot>()
    val mockDocumentSnapshot = mockk<QueryDocumentSnapshot>()
    val snapshotListener = slot<EventListener<QuerySnapshot>>()

    private lateinit var cartRemoteDataSource: CartRemoteDataSourceImpl

    @Before
    fun setup() {
        clearAllMocks()
        cartRemoteDataSource = CartRemoteDataSourceImpl(firestore, firebaseAuth)

        every { firestore.collection("users") } returns mockCollectionReference
        every { mockCollectionReference.document(any()) } returns mockDocumentReference
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenObserveCartItems_whenCalled_thenReturnFlowOfCartItems_success() = runTest {
        every { mockDocumentSnapshot.toObject(CartItem::class.java) } returns MockUtils.mockCartItems.first()
        every { mockQuerySnapshot.documents } returns listOf(mockDocumentSnapshot)
        every {
            mockCollectionReference
                .document(any())
                .collection("cart")
                .addSnapshotListener(capture(snapshotListener))
        } answers {
            snapshotListener.captured.onEvent(mockQuerySnapshot, null)
            mockk()
        }

        cartRemoteDataSource.observeCartItems("userId").test {
            val items = awaitItem()
            assert(items.isNotEmpty())
            assert(items.first().productId == MockUtils.mockCartItems.first().productId)
            assert(items.first().uniPrice == MockUtils.mockCartItems.first().uniPrice)
            assert(items.first().name == MockUtils.mockCartItems.first().name)
            assert(items.first().quantity == MockUtils.mockCartItems.first().quantity)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun givenObserveCartItens_whenCalled_thenReturnFlowOfCartItens_emptyResults() = runTest {
        every { mockQuerySnapshot.documents } returns emptyList()
        every {
            mockCollectionReference
                .document(any())
                .collection("cart")
                .addSnapshotListener(capture(snapshotListener))
        } answers {
            snapshotListener.captured.onEvent(mockQuerySnapshot, null)
            mockk()
        }

        cartRemoteDataSource.observeCartItems("userId").test {
            val items = awaitItem()
            assert(items.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun givenObserveCartItens_whenListenerRemoved_thenFlowIsCancelled_withCancellationException() = runTest {
        val error = FirebaseFirestoreException(
            "Firestore error",
            FirebaseFirestoreException.Code.UNKNOWN
        )

        val listenerRegistration = mockk<ListenerRegistration> {
            every { remove() } returns Unit
        }
        every {
            mockCollectionReference
                .document(any())
                .collection("cart")
                .addSnapshotListener(capture(snapshotListener))
        } answers {
            snapshotListener.captured.onEvent(null, error)
            listenerRegistration
        }

        cartRemoteDataSource.observeCartItems("userId").test {
            val exception = awaitError()
            assert(exception is CancellationException)
            assert(exception.message == "Error listener cart")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenUpdateItem_whenCalled_thenReturnSuccess() = runTest {
        val cartItem = MockUtils.mockCartItems.first()
        val mockCartCollection = mockk<CollectionReference>()
        val mockCartDocument = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>(relaxed = true)

        every { mockDocumentReference.collection("cart") } returns mockCartCollection
        every { mockCartCollection.document(cartItem.productId.toString()) } returns mockCartDocument
        every { mockCartDocument.set(cartItem) } returns mockTask
        every { mockTask.isSuccessful } returns true
        every { mockTask.isCanceled } returns false
        every { mockTask.isComplete } returns true
        every { mockTask.exception } returns null

        val result = cartRemoteDataSource.updateItem("userId", cartItem)
        assert(result is FirebaseResult.Success)
        verify(exactly = 1) { mockCartDocument.set(cartItem) }
    }

    @Test
    fun givenUpdateItem_whenCalled_thenReturnError() = runTest {
        val cartItem = MockUtils.mockCartItems.first()
        val mockCartCollection = mockk<CollectionReference>()
        val mockCartDocument = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>(relaxed = true)
        val exception = Exception("Error update item")

        every { mockDocumentReference.collection("cart") } returns mockCartCollection
        every { mockCartCollection.document(cartItem.productId.toString()) } returns mockCartDocument
        every { mockCartDocument.set(cartItem) } returns mockTask
        every { mockTask.isSuccessful } returns false
        every { mockTask.isCanceled } returns false
        every { mockTask.isComplete } returns true
        every { mockTask.exception } returns exception

        val result = cartRemoteDataSource.updateItem("userId", cartItem)
        assert(result is FirebaseResult.UnknownError && result.exception.message == "Error update item")
        verify(exactly = 1) { mockCartDocument.set(cartItem) }
    }

    @Test
    fun givenRemoveItem_whenCalled_thenReturnSuccess() = runTest {
        val productId = "1"
        val mockCartCollection = mockk<CollectionReference>()
        val mockCartDocument = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>(relaxed = true)

        every { mockDocumentReference.collection("cart") } returns mockCartCollection
        every { mockCartCollection.document(productId) } returns mockCartDocument
        every { mockCartDocument.delete() } returns mockTask
        every { mockTask.isSuccessful } returns true
        every { mockTask.isCanceled } returns false
        every { mockTask.isComplete } returns true
        every { mockTask.exception } returns null

        val result = cartRemoteDataSource.removeItem("userId", productId)
        assert(result is FirebaseResult.Success)
        verify(exactly = 1) { mockCartDocument.delete() }
    }

    @Test
    fun givenRemoveItem_whenCalled_thenReturnError() = runTest {
        val productId = "1"
        val mockCartCollection = mockk<CollectionReference>()
        val mockCartDocument = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>(relaxed = true)
        val exception = FirebaseFirestoreException("Error remove item", FirebaseFirestoreException.Code.UNKNOWN)

        every { mockDocumentReference.collection("cart") } returns mockCartCollection
        every { mockCartCollection.document(productId) } returns mockCartDocument
        every { mockCartDocument.delete() } returns mockTask
        every { mockTask.isSuccessful } returns false
        every { mockTask.isCanceled } returns false
        every { mockTask.isComplete } returns true
        every { mockTask.exception } returns exception

        val result = cartRemoteDataSource.removeItem("userId", productId)
        assert(result is FirebaseResult.FirebaseError && result.message == "Error remove item")
        verify(exactly = 1) { mockCartDocument.delete() }
    }

    @Test
    fun givenClearCart_whenFirestoreOperationSucceeds_thenReturnSuccess() = runTest {
        val mockDocument1 = mockk<QueryDocumentSnapshot>()
        val mockDocument2 = mockk<QueryDocumentSnapshot>()
        val mockDocRef1 = mockk<DocumentReference>()
        val mockDocRef2 = mockk<DocumentReference>()
        val mockGetTask = mockk<Task<QuerySnapshot>>(relaxed = true)
        val mockDeleteTask1 = mockk<Task<Void>>(relaxed = true)
        val mockDeleteTask2 = mockk<Task<Void>>(relaxed = true)

        every { mockDocumentReference.collection("cart") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockGetTask
        every { mockGetTask.isSuccessful } returns true
        every { mockGetTask.isComplete } returns true
        every { mockGetTask.exception } returns null
        every { mockGetTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns listOf(mockDocument1, mockDocument2)
        every { mockDocument1.reference } returns mockDocRef1
        every { mockDocument2.reference } returns mockDocRef2
        every { mockDocRef1.delete() } returns mockDeleteTask1
        every { mockDocRef2.delete() } returns mockDeleteTask2
        every { mockDeleteTask1.isSuccessful } returns true
        every { mockDeleteTask1.isComplete } returns true
        every { mockDeleteTask1.exception } returns null
        every { mockDeleteTask2.isSuccessful } returns true
        every { mockDeleteTask2.isComplete } returns true
        every { mockDeleteTask2.exception } returns null

        val result = cartRemoteDataSource.clearCart("userId")

        assert(result is FirebaseResult.Success)
        verify(exactly = 1) { mockCollectionReference.get() }
        verify(exactly = 1) { mockDocRef1.delete() }
        verify(exactly = 1) { mockDocRef2.delete() }
    }

    @Test
    fun givenClearCart_whenFirestoreGetFails_thenReturnFirebaseError() = runTest {
        val mockGetTask = mockk<Task<QuerySnapshot>>(relaxed = true)
        val firestoreException = FirebaseFirestoreException(
            "Permission denied",
            FirebaseFirestoreException.Code.PERMISSION_DENIED
        )

        every { mockDocumentReference.collection("cart") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockGetTask
        every { mockGetTask.isSuccessful } returns false
        every { mockGetTask.isComplete } returns true
        every { mockGetTask.isCanceled } returns false
        every { mockGetTask.exception } returns firestoreException

        val result = cartRemoteDataSource.clearCart("userId")
        assert(result is FirebaseResult.FirebaseError)
        val error = result as FirebaseResult.FirebaseError
        assert(firestoreException.message == error.message)
        verify(exactly = 1) { mockCollectionReference.get() }
    }

    @Test
    fun givenPlaceOrder_whenCalled_thenReturnSuccess() = runTest {
        val mockOrderCollection = mockk<CollectionReference>()
        val mockOrderDocument = mockk<DocumentReference>()
        val mockOrderTask = mockk<Task<Void>>(relaxed = true)
        val mockCartGetTask = mockk<Task<QuerySnapshot>>(relaxed = true)

        every { mockDocumentReference.collection("orders") } returns mockOrderCollection
        every { mockOrderCollection.document(any<String>()) } returns mockOrderDocument
        every { mockOrderDocument.set(any<Order>()) } returns mockOrderTask
        every { mockDocumentReference.collection("cart") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockCartGetTask
        every { mockCartGetTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns emptyList()
        every { mockOrderTask.isSuccessful } returns true
        every { mockOrderTask.isComplete } returns true
        every { mockOrderTask.exception } returns null
        every { mockCartGetTask.isSuccessful } returns true
        every { mockCartGetTask.isComplete } returns true
        every { mockCartGetTask.exception } returns null

        val result = cartRemoteDataSource.placeOrder("userId", MockUtils.mockCartItems, MockUtils.info)

        assert(result is FirebaseResult.Success)
        verify(exactly = 1) { mockOrderDocument.set(any<Order>()) }
    }

    @Test
    fun givenPlaceOrder_whenOrderSetFails_thenReturnFirebaseError() = runTest {
        val mockOrderCollection = mockk<CollectionReference>()
        val mockOrderDocument = mockk<DocumentReference>()
        val mockOrderTask = mockk<Task<Void>>(relaxed = true)
        val mockCartGetTask = mockk<Task<QuerySnapshot>>(relaxed = true)
        val exception = FirebaseFirestoreException("Error placing order", FirebaseFirestoreException.Code.UNKNOWN)

        every { mockDocumentReference.collection("orders") } returns mockOrderCollection
        every { mockOrderCollection.document(any<String>()) } returns mockOrderDocument
        every { mockOrderDocument.set(any<Order>()) } returns mockOrderTask
        every { mockDocumentReference.collection("cart") } returns mockCollectionReference
        every { mockCollectionReference.get() } returns mockCartGetTask
        every { mockCartGetTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns emptyList()
        every { mockOrderTask.isSuccessful } returns false
        every { mockOrderTask.isComplete } returns true
        every { mockOrderTask.exception } returns exception
        every { mockCartGetTask.isSuccessful } returns true
        every { mockCartGetTask.isComplete } returns true
        every { mockCartGetTask.exception } returns null

        val result = cartRemoteDataSource.placeOrder("userId", MockUtils.mockCartItems, MockUtils.info)

        assert(result is FirebaseResult.FirebaseError && result.message == "Error placing order")
        verify(exactly = 1) { mockOrderDocument.set(any<Order>()) }
    }

    @Test
    fun givenGetCurrentUserId_whenUserIsLoggedIn_thenReturnUserId() {
        val userId = "userId123"
        val mockUser = mockk<FirebaseUser>()
        every { firebaseAuth.currentUser } returns mockUser
        every { mockUser.uid } returns userId

        val result = cartRemoteDataSource.getCurrentUserId()
        assert(result == userId)
        verify(exactly = 1) { firebaseAuth.currentUser }
        verify(exactly = 1) { mockUser.uid }
    }

    @Test
    fun givenGetCurrentUserId_whenNoUserLoggedIn_thenReturnNull() {
        every { firebaseAuth.currentUser } returns null

        val result = cartRemoteDataSource.getCurrentUserId()
        assert(result == null)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

}