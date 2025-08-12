package com.joao01sb.shophub.features.auth.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AuthRemoteDataSourceImpTest {

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val mockAuthTask = mockk<Task<AuthResult>>(relaxed = true)
    private val mockFirestoreTask = mockk<Task<Void>>(relaxed = true)
    private val mockAuthResult = mockk<AuthResult>(relaxed = true)
    private val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
    private val mockCollectionReference = mockk<CollectionReference>(relaxed = true)
    private val mockDocumentReference = mockk<DocumentReference>(relaxed = true)

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImp

    @Before
    fun setUp() {
        clearAllMocks()

        authRemoteDataSource = AuthRemoteDataSourceImp(firebaseAuth, firestore)

        every { firestore.collection("users") } returns mockCollectionReference
        every { mockCollectionReference.document(any()) } returns mockDocumentReference
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun givenValidEmailAndPassword_whenRegisterUser_thenReturnsSuccessWithUid() = runTest {
        val expectedUid = "Uid123"
        val email = "test@email.com"
        val password = "password123"

        every { firebaseAuth.createUserWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns null
        every { mockAuthTask.isCanceled } returns false
        every { mockAuthTask.result } returns mockAuthResult
        every { mockAuthResult.user } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns expectedUid

        val result = authRemoteDataSource.registerUser(email, password)

        assertTrue(result is FirebaseResult.Success)
        assertEquals(expectedUid, (result as FirebaseResult.Success).data)

        verify(exactly = 1) { firebaseAuth.createUserWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenExistingEmail_whenRegisterUser_thenReturnsFirebaseError() = runTest {
        val email = "existing@email.com"
        val password = "password123"
        val exception = RuntimeException("Email already exists")

        every { firebaseAuth.createUserWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns exception
        every { mockAuthTask.result } throws exception

        val result = authRemoteDataSource.registerUser(email, password)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { firebaseAuth.createUserWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenFirebaseReturnsNullUid_whenRegisterUser_thenReturnsUnknownError() = runTest {
        val email = "test@email.com"
        val password = "password123"

        every { firebaseAuth.createUserWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns null
        every { mockAuthTask.result } returns mockAuthResult
        every { mockAuthResult.user } returns null

        val result = authRemoteDataSource.registerUser(email, password)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { firebaseAuth.createUserWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenValidCredentials_whenLogin_thenReturnsSuccess() = runTest {
        val email = "test@email.com"
        val password = "password123"

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns null
        every { mockAuthTask.result } returns mockAuthResult

        val result = authRemoteDataSource.login(email, password)

        assertTrue(result is FirebaseResult.Success)
        verify(exactly = 1) { firebaseAuth.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenInvalidCredentials_whenLogin_thenReturnsFirebaseError() = runTest {
        val email = "test@email.com"
        val password = "wrongpassword"
        val exception = RuntimeException("Invalid credentials")

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns exception
        every { mockAuthTask.result } throws exception

        val result = authRemoteDataSource.login(email, password)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { firebaseAuth.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenUserNotFound_whenLogin_thenReturnsFirebaseError() = runTest {
        val email = "notfound@email.com"
        val password = "password123"
        val exception = RuntimeException("User not found")

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns mockAuthTask
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.exception } returns exception
        every { mockAuthTask.result } throws exception

        val result = authRemoteDataSource.login(email, password)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { firebaseAuth.signInWithEmailAndPassword(email, password) }
    }

    @Test
    fun givenValidUserData_whenSaveUser_thenReturnsSuccess() = runTest {
        val uid = "test-uid"
        val email = "test@email.com"
        val name = "Test User"

        every { mockDocumentReference.set(any()) } returns mockFirestoreTask
        every { mockFirestoreTask.isComplete } returns true
        every { mockFirestoreTask.exception } returns null
        every { mockFirestoreTask.result } returns null

        val result = authRemoteDataSource.saveUser(uid, email, name)

        assertTrue(result is FirebaseResult.Success)
        verify(exactly = 1) { mockDocumentReference.set(any()) }
        verify(exactly = 1) { firestore.collection("users") }
        verify(exactly = 1) { mockCollectionReference.document(uid) }
    }

    @Test
    fun givenFirestoreConnectionError_whenSaveUser_thenReturnsNetworkError() = runTest {
        val uid = "test-uid"
        val email = "test@email.com"
        val name = "Test User"
        val exception = IOException("Network connection failed")

        every { mockDocumentReference.set(any()) } returns mockFirestoreTask
        every { mockFirestoreTask.isComplete } returns true
        every { mockFirestoreTask.exception } returns exception
        every { mockFirestoreTask.result } throws exception

        val result = authRemoteDataSource.saveUser(uid, email, name)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { mockDocumentReference.set(any()) }
    }

    @Test
    fun givenFirestorePermissionDenied_whenSaveUser_thenReturnsFirebaseError() = runTest {
        val uid = "test-uid"
        val email = "test@email.com"
        val name = "Test User"
        val exception = RuntimeException("Permission denied")

        every { mockDocumentReference.set(any()) } returns mockFirestoreTask
        every { mockFirestoreTask.isComplete } returns true
        every { mockFirestoreTask.exception } returns exception
        every { mockFirestoreTask.result } throws exception

        val result = authRemoteDataSource.saveUser(uid, email, name)

        assertTrue(result is FirebaseResult.UnknownError)
        verify(exactly = 1) { mockDocumentReference.set(any()) }
    }

    @Test
    fun testLogout_success() {
        every { firebaseAuth.signOut() } just Runs

        val result = authRemoteDataSource.logout()

        assertTrue(result is FirebaseResult.Success)
        verify(exactly = 1) { firebaseAuth.signOut() }
    }

    @Test
    fun givenFirebaseThrowsException_whenLogout_thenReturnsUnknownError() {
        val exception = RuntimeException("Logout failed")
        every { firebaseAuth.signOut() } throws exception

        val result = authRemoteDataSource.logout()

        assertTrue(result is FirebaseResult.UnknownError)
        assertEquals(exception, (result as FirebaseResult.UnknownError).exception)
        verify(exactly = 1) { firebaseAuth.signOut() }
    }

    @Test
    fun givenMultipleLogoutCalls_whenLogout_thenAlwaysReturnsSuccess() {
        every { firebaseAuth.signOut() } just Runs

        val result1 = authRemoteDataSource.logout()
        val result2 = authRemoteDataSource.logout()
        val result3 = authRemoteDataSource.logout()

        assertTrue(result1 is FirebaseResult.Success)
        assertTrue(result2 is FirebaseResult.Success)
        assertTrue(result3 is FirebaseResult.Success)
        verify(exactly = 3) { firebaseAuth.signOut() }
    }

    @Test
    fun givenUserIsLoggedIn_whenIsUserLoggedIn_thenReturnsTrue() {
        every { firebaseAuth.currentUser } returns mockFirebaseUser

        val result = authRemoteDataSource.isUserLoggedIn()

        assertTrue(result)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenUserIsNotLoggedIn_whenIsUserLoggedIn_thenReturnsFalse() {
        every { firebaseAuth.currentUser } returns null

        val result = authRemoteDataSource.isUserLoggedIn()

        assertFalse(result)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenUserLoggedOut_whenIsUserLoggedIn_thenReturnsFalse() {
        every { firebaseAuth.currentUser } returns null

        val result = authRemoteDataSource.isUserLoggedIn()

        assertFalse(result)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenUserIsLoggedIn_whenGetUserId_thenReturnsValidUid() {
        val expectedUid = "test-uid-123"
        every { firebaseAuth.currentUser } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns expectedUid

        val result = authRemoteDataSource.getUserId()

        assertEquals(expectedUid, result)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenUserIsNotLoggedIn_whenGetUserId_thenReturnsNull() {
        every { firebaseAuth.currentUser } returns null

        val result = authRemoteDataSource.getUserId()

        assertNull(result)
        verify(exactly = 1) { firebaseAuth.currentUser }
    }

    @Test
    fun givenUserLoggedButUidIsEmpty_whenGetUserId_thenReturnsEmptyString() {
        val emptyUid = ""
        every { firebaseAuth.currentUser } returns mockFirebaseUser
        every { mockFirebaseUser.uid } returns emptyUid

        val result = authRemoteDataSource.getUserId()

        assertEquals(emptyUid, result)
        verify(exactly = 1) { firebaseAuth.currentUser }
        verify(exactly = 1) { mockFirebaseUser.uid }
    }
}