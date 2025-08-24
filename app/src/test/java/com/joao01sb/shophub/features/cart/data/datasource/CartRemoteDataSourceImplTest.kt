package com.joao01sb.shophub.features.cart.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

class CartRemoteDataSourceImplTest {

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val mockFirestoreTask = mockk<Task<Void>>(relaxed = true)
    private val mockCollectionReference = mockk<CollectionReference>(relaxed = true)
    private val mockDocumentReference = mockk<DocumentReference>(relaxed = true)

    private lateinit var cartRemoteDataSource: CartRemoteDataSourceImpl

    @Before
    fun setup() {
        clearAllMocks()
        cartRemoteDataSource = CartRemoteDataSourceImpl(firestore, firebaseAuth)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}