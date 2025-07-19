package com.joao01sb.shophub.features.orders.data.repository

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.domain.datasource.RemoteOrdersDataSource
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository

class OrdersRepositoryImp(
    private val dataSourceImp: RemoteOrdersDataSource
) : OrdersRepository{

    override suspend fun getOrders(userId: String): List<Order> {
        return dataSourceImp.getOrders(userId)
    }

    override fun getCurrentUserId(): String? {
        return dataSourceImp.getCurrentUserId()
    }
}