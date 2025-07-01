package com.joao01sb.shophub.features.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joao01sb.shophub.features.auth.data.datasource.AuthRemoteDataSourceImp
import com.joao01sb.shophub.features.auth.data.repository.AuthRepositoryImp
import com.joao01sb.shophub.features.auth.domain.datasource.AuthRemoteDataSource
import com.joao01sb.shophub.features.auth.domain.repository.AuthRepository
import com.joao01sb.shophub.features.auth.domain.usecase.GetCurrentUserIdUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.IsUserLoggedInUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LoginUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LogoutUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        firebaseAuth: FirebaseAuth
    ) : AuthRemoteDataSource {
        return AuthRemoteDataSourceImp(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDataSource: AuthRemoteDataSource,
        firestore: FirebaseFirestore
    ) : AuthRepository {
        return AuthRepositoryImp(authRemoteDataSource,firestore)
    }

    @Provides
    @Singleton
    fun provideRegisterUserCase(
        repository: AuthRepository
    ) : RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        repository: AuthRepository
    ) : LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(
        repository: AuthRepository
    ) : LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideIsUserLoggedInUseCase(
        repository: AuthRepository
    ) : IsUserLoggedInUseCase {
        return IsUserLoggedInUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserIdUseCase(
        repository: AuthRepository
    ) : GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(repository)
    }
}