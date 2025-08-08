package com.joao01sb.shophub.features.orders.domain.repository

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.result.DomainResult

interface OrdersRepository {

    suspend fun getOrders(userId: String) : DomainResult<List<Order>>
    fun getCurrentUserId(): String?
    suspend fun getOrderById(userId: String, id: String): DomainResult<Order?>
}