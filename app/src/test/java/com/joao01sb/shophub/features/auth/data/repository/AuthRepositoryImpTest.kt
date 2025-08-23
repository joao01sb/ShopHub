package com.joao01sb.shophub.features.auth.data.repository

import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.auth.data.datasource.AuthRemoteDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImpTest {

    val authRemoteDataSource: AuthRemoteDataSourceImp = mockk(relaxed = true)

    private lateinit var authRepository: AuthRepositoryImp

    @Before
    fun setup() {
        authRepository = AuthRepositoryImp(authRemoteDataSource)
    }

    @Test
    fun givenValidCredentials_whenRegister_thenSuccess() = runTest {
        val expectedUid = "Uid123"
        val email = "test@email.com"
        val password = "password123"

        coEvery { authRemoteDataSource.registerUser(email, password) } returns FirebaseResult.Success(expectedUid)

        val result = authRemoteDataSource.registerUser(email, password)

        assert(result is FirebaseResult.Success && result.data == expectedUid)

        coVerify(exactly = 1) { authRemoteDataSource.registerUser(email, password) }

    }

    @Test
    fun givenInvalidCredentials_whenRegister_thenFailure() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val errorMessage = "Invalid credentials"

        coEvery { authRemoteDataSource.registerUser(email, password) } returns
                FirebaseResult.UnknownError(Exception(errorMessage))

        val result = authRemoteDataSource.registerUser(email, password)

        assert(result is FirebaseResult.UnknownError)

        assert((result as FirebaseResult.UnknownError).exception.message == errorMessage)

        coVerify(exactly = 1) { authRemoteDataSource.registerUser(email, password) }

    }

    @Test
    fun givenValidCredentials_whenRegister_thenNetworkError() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val errorMessage = "Network error"
        val codeError = "500"

        coEvery { authRemoteDataSource.registerUser(email, password) } returns
                FirebaseResult.FirebaseError(codeError, errorMessage)

        val result = authRemoteDataSource.registerUser(email, password)

        assert(result is FirebaseResult.FirebaseError)

        assert((result as FirebaseResult.FirebaseError).message == errorMessage)

        coVerify(exactly = 1) { authRemoteDataSource.registerUser(email, password) }

    }

    @Test
    fun givenValidCredentials_whenLogin_thenSuccess() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val expectedUid = "Uid123"
        val expectedName = "Test User"

        coEvery { authRemoteDataSource.login(email, password) } returns FirebaseResult.Success(Unit)
        coEvery { authRemoteDataSource.saveUser(expectedUid, email, expectedName) } returns FirebaseResult.Success(Unit)

        val result = authRemoteDataSource.login(email, password)

        assert(result is FirebaseResult.Success)

        coVerify(exactly = 1) { authRemoteDataSource.login(email, password) }

    }

    @Test
    fun givenInvalidCredentials_whenLogin_thenFailure() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val errorMessage = "Invalid credentials"

        coEvery { authRemoteDataSource.login(email, password) } returns
                FirebaseResult.UnknownError(Exception(errorMessage))

        val result = authRemoteDataSource.login(email, password)

        assert(result is FirebaseResult.UnknownError)

        assert((result as FirebaseResult.UnknownError).exception.message == errorMessage)

        coVerify(exactly = 1) { authRemoteDataSource.login(email, password) }

    }

    @Test
    fun givenValidCredentials_whenLogin_thenNetworkError() = runTest {
        val email = "test@email.com"
        val password = "password123"
        val errorMessage = "Network error"
        val codeError = "500"

        coEvery { authRemoteDataSource.login(email, password) } returns
                FirebaseResult.FirebaseError(codeError, errorMessage)

        val result = authRemoteDataSource.login(email, password)

        assert(result is FirebaseResult.FirebaseError)

        assert((result as FirebaseResult.FirebaseError).message == errorMessage)

        coVerify(exactly = 1) { authRemoteDataSource.login(email, password) }

    }

    @Test
    fun givenValidCredentials_whenSaveUser_thenSuccess() = runTest {
        val uid = "Uid123"
        val email = "test@email.com"
        val name = "Test User"

        coEvery { authRemoteDataSource.saveUser(uid, email, name) } returns FirebaseResult.Success(Unit)

        val result = authRemoteDataSource.saveUser(uid, email, name)

        assert(result is FirebaseResult.Success)

        coVerify(exactly = 1) { authRemoteDataSource.saveUser(uid, email, name) }

    }

    @Test
    fun givenInvalidCredentials_whenSaveUser_thenFailure() = runTest {
        val uid = "Uid123"
        val email = "test@email.com"

        coEvery { authRemoteDataSource.saveUser(uid, email, any()) } returns
                FirebaseResult.UnknownError(Exception("Invalid credentials"))

        val result = authRemoteDataSource.saveUser(uid, email, "Test User")

        assert(result is FirebaseResult.UnknownError)

        assert((result as FirebaseResult.UnknownError).exception.message == "Invalid credentials")

        coVerify(exactly = 1) { authRemoteDataSource.saveUser(uid, email, "Test User") }

    }

    @Test
    fun givenValidCredentials_whenSaveUser_thenNetworkError() = runTest {
        val uid = "Uid123"
        val email = "test@email.com"

        coEvery {
            authRemoteDataSource.saveUser(
                uid,
                email,
                any()
            )
        } returns FirebaseResult.FirebaseError("500", "Network error")

        val result = authRemoteDataSource.saveUser(uid, email, "Test User")

        assert(result is FirebaseResult.FirebaseError)

        assert((result as FirebaseResult.FirebaseError).message == "Network error")

        coVerify(exactly = 1) { authRemoteDataSource.saveUser(uid, email, "Test User") }

    }

    @Test
    fun givenValidCredentials_whenLogout_thenSuccess() = runTest {
        coEvery { authRemoteDataSource.logout() } returns FirebaseResult.Success(Unit)

        val result = authRemoteDataSource.logout()

        assert(result is FirebaseResult.Success)

        coVerify(exactly = 1) { authRemoteDataSource.logout() }

    }

    @Test
    fun givenInvalidCredentials_whenLogout_thenFailure() = runTest {
        coEvery { authRemoteDataSource.logout() } returns
                FirebaseResult.UnknownError(Exception("Invalid credentials"))

        val result = authRemoteDataSource.logout()

        assert(result is FirebaseResult.UnknownError)

        assert((result as FirebaseResult.UnknownError).exception.message == "Invalid credentials")

        coVerify(exactly = 1) { authRemoteDataSource.logout() }

    }

    @Test
    fun givenValidCredentials_whenLogout_thenNetworkError() = runTest {
        coEvery { authRemoteDataSource.logout() } returns
                FirebaseResult.FirebaseError("500", "Network error")

        val result = authRemoteDataSource.logout()

        assert(result is FirebaseResult.FirebaseError)

        assert((result as FirebaseResult.FirebaseError).message == "Network error")

        coVerify(exactly = 1) { authRemoteDataSource.logout() }

    }

    @Test
    fun givenValidCredentials_whenIsUserLoggedIn_thenSuccess() = runTest {
        coEvery { authRemoteDataSource.isUserLoggedIn() } returns true

        val result = authRemoteDataSource.isUserLoggedIn()

        assert(result)

        coVerify(exactly = 1) { authRemoteDataSource.isUserLoggedIn() }

    }

    @Test
    fun givenInvalidCredentials_whenIsUserLoggedIn_thenFailure() = runTest {
        coEvery { authRemoteDataSource.isUserLoggedIn() } returns false

        val result = authRemoteDataSource.isUserLoggedIn()

        assert(!result)

        coVerify(exactly = 1) { authRemoteDataSource.isUserLoggedIn() }

    }

    @Test
    fun givenValidCredentials_whenGetUserId_thenSuccess() = runTest {
        val expectedUid = "Uid123"

        coEvery { authRemoteDataSource.getUserId() } returns expectedUid

        val result = authRemoteDataSource.getUserId()

        assert(result == expectedUid)

        coVerify(exactly = 1) { authRemoteDataSource.getUserId() }

    }

    @Test
    fun givenInvalidCredentials_whenGetUserId_thenFailure() = runTest {
        coEvery { authRemoteDataSource.getUserId() } returns null

        val result = authRemoteDataSource.getUserId()

        assert(result == null)

        coVerify(exactly = 1) { authRemoteDataSource.getUserId() }

    }

    @Test
    fun givenValidCredentials_whenGetUserId_thenNetworkError() = runTest {
        coEvery { authRemoteDataSource.getUserId() } returns null

        val result = authRemoteDataSource.getUserId()

        assert(result == null)

        coVerify(exactly = 1) { authRemoteDataSource.getUserId() }

    }

}