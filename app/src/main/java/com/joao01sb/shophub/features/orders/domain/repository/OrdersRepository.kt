package com.joao01sb.shophub.features.orders.domain.repository

import com.joao01sb.shophub.core.domain.model.Order

interface OrdersRepository {

    suspend fun getOrders(userId: String) : List<Order>
    fun getCurrentUserId(): String?
    suspend fun getOrderById(userId: String, id: String): Order?
}