package com.joao01sb.shophub.features.orders.domain.datasource

import com.joao01sb.shophub.core.domain.model.Order

interface RemoteOrdersDataSource {

    suspend fun getOrders(userId: String) : List<Order>

    fun getCurrentUserId(): String?

}