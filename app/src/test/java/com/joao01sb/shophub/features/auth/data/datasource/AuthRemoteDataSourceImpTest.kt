package com.joao01sb.shophub.features.auth.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.mockk
import org.junit.Before

class AuthRemoteDataSourceImpTest {

    private val firebaseAuth = mockk<FirebaseAuth>()
    private val firestore = mockk<FirebaseFirestore>()
    private val authResult = mockk<AuthResult>()
    private val firebaseUser = mockk<FirebaseUser>()
    private val task = mockk<Task<AuthResult>>()

    private lateinit var authRemoteDataSource: AuthRemoteDataSourceImp

    @Before
    fun setUp() {
        authRemoteDataSource = AuthRemoteDataSourceImp(firebaseAuth, firestore)
    }

}