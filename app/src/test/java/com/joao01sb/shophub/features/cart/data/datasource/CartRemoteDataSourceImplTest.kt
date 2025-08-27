package com.joao01sb.shophub.features.cart.data.datasource

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.joao01sb.shophub.core.domain.model.CartItem
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
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

        every { mockDocumentSnapshot.toObject(CartItem::class.java) } returns MockItens.mockCartItems.first()

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
            assert(items.first().productId == MockItens.mockCartItems.first().productId)
            assert(items.first().uniPrice == MockItens.mockCartItems.first().uniPrice)
            assert(items.first().name == MockItens.mockCartItems.first().name)
            assert(items.first().quantity == MockItens.mockCartItems.first().quantity)
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

}