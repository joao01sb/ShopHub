package com.joao01sb.shophub.features.orders.data.repository

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.error.ErrorType
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.core.result.firebase.FirebaseResult
import com.joao01sb.shophub.features.orders.domain.datasource.RemoteOrdersDataSource
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository

class OrdersRepositoryImp(
    private val dataSourceImp: RemoteOrdersDataSource
) : OrdersRepository {

    override suspend fun getOrders(userId: String): DomainResult<List<Order>> {
        return when (val result = dataSourceImp.getOrders(userId)) {
            is FirebaseResult.AuthError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.AUTHENTICATION
            )
            is FirebaseResult.FirebaseError -> DomainResult.Error(
                message = result.message,
                type = ErrorType.DATABASE
            )
            is FirebaseResult.Success -> DomainResult.Success(result.data)
            is FirebaseResult.UnknownError -> DomainResult.Error(
                message = "Fetch orders failed: ${result.exception.message}",
                type = ErrorType.UNKNOWN
            )
        }
    }

    override fun getCurrentUserId(): String? {
        return dataSourceImp.getCurrentUserId()
    }

    override suspend fun getOrderById(
        userId: String,
        id: String
    ): DomainResult<Order?> {
        return when (val result = dataSourceImp.getOrderById(userId, id)) {
            is FirebaseResult.AuthError -> {
                DomainResult.Error(
                    message = result.message,
                    type = ErrorType.AUTHENTICATION
                )
            }
            is FirebaseResult.FirebaseError -> {
                DomainResult.Error(
                    message = result.message,
                    type = ErrorType.DATABASE
                )
            }
            is FirebaseResult.Success -> {
                DomainResult.Success(result.data)
            }
            is FirebaseResult.UnknownError -> {
                DomainResult.Error(
                    message = "Fetch order failed: ${result.exception.message}",
                    type = ErrorType.UNKNOWN
                )
            }
        }
    }
}