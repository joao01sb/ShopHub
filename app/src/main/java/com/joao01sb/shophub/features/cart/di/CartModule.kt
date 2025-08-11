package com.joao01sb.shophub.features.cart.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joao01sb.shophub.features.cart.data.datasource.CartRemoteDataSourceImpl
import com.joao01sb.shophub.features.cart.data.repository.CartRepositoryImp
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository
import com.joao01sb.shophub.features.cart.domain.usecase.ClearCartUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.GetCartItemsUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.PlaceOrderUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.RemoveCartItemUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.UpdateItemUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.ValidateCheckoutInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    fun provideCartRemoteDataSource(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : CartRemoteDataSource {
        return CartRemoteDataSourceImpl(firestore, firebaseAuth)
    }

    @Provides
    fun provideCartRepository(
        remoteDataSource: CartRemoteDataSource
    ) : CartRepository {
        return CartRepositoryImp(remoteDataSource)
    }

    @Provides
    fun provideGetCartItemsUseCase(
        cartRepository: CartRepository
    ) : GetCartItemsUseCase {
        return GetCartItemsUseCase(cartRepository)
    }

    @Provides
    fun provideUpdateItemUseCase(
        cartRepository: CartRepository
    ) : UpdateItemUseCase {
        return UpdateItemUseCase(cartRepository)
    }

    @Provides
    fun provideRemoveCartItemUseCase(
        cartRepository: CartRepository
    ) : RemoveCartItemUseCase {
        return RemoveCartItemUseCase(cartRepository)
    }

    @Provides
    fun provideClearCartUseCase(
        cartRepository: CartRepository
    ) : ClearCartUseCase {
        return ClearCartUseCase(cartRepository)
    }

    @Provides
    fun providePlaceOrderUseCase(
        cartRepository: CartRepository
    ) : PlaceOrderUseCase {
        return PlaceOrderUseCase(cartRepository)
    }

    @Provides
    fun provideValidateCheckoutInfoUseCase() : ValidateCheckoutInfoUseCase {
        return ValidateCheckoutInfoUseCase()
    }

}