package com.joao01sb.shophub.core.di

import com.joao01sb.shophub.core.domain.manager.AuthManager
import com.joao01sb.shophub.core.domain.manager.CartManager
import com.joao01sb.shophub.features.auth.domain.usecase.GetCurrentUserIdUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.IsUserLoggedInUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LoginUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.LogoutUseCase
import com.joao01sb.shophub.features.auth.domain.usecase.RegisterUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.ClearCartUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.GetCartItemsUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.PlaceOrderUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.RemoveCartItemUseCase
import com.joao01sb.shophub.features.cart.domain.usecase.UpdateItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideCartManager(
        add: UpdateItemUseCase,
        remove: RemoveCartItemUseCase,
        get: GetCartItemsUseCase,
        clear: ClearCartUseCase,
        placeOrder: PlaceOrderUseCase
    ): CartManager = CartManager(add, remove, get, clear, placeOrder)

    @Provides
    fun provideAuthManager(
        register: RegisterUseCase,
        login: LoginUseCase,
        logout: LogoutUseCase,
        currentUser: GetCurrentUserIdUseCase,
        isLoggedIn: IsUserLoggedInUseCase
    ) : AuthManager {
        return AuthManager(register, login, logout, currentUser, isLoggedIn)
    }
}