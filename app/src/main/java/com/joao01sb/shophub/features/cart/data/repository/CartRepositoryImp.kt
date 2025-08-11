package com.joao01sb.shophub.features.cart.data.repository

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.cart.domain.datasource.CartRemoteDataSource
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImp(
    private val remoteDataSource: CartRemoteDataSource
) : CartRepository {

    override fun observeCartItems(userId: String): Flow<List<CartItem>> {
        return remoteDataSource.observeCartItems(userId)
    }

    override suspend fun updateItem(
        userId: String,
        item: CartItem
    ) : DomainResult<Unit> {
        return when (val result = remoteDataSource.updateItem(userId, item)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )
            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )

            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Update failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun removeItem(
        userId: String,
        productId: String
    ) : DomainResult<Unit> {
        return when (val result = remoteDataSource.removeItem(userId, productId)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )

            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )

            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Remove failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun clearCart(userId: String) : DomainResult<Unit> {
        return when (val result = remoteDataSource.clearCart(userId)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )

            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )

            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Clear cart failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override suspend fun placeOrder(
        userId: String,
        items: List<CartItem>,
        info: CheckoutInfo
    ) : DomainResult<Unit> {
        return when (val result = remoteDataSource.placeOrder(userId, items, info)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )

            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )

            is FirebaseResult.Success -> DomainResult.Success(Unit)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Order placement failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override fun getCurrentUserId(): String? {
        return remoteDataSource.getCurrentUserId()
    }
}