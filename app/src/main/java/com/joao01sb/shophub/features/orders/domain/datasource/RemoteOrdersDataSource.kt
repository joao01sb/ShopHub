package com.joao01sb.shophub.features.orders.domain.datasource

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.result.firebase.FirebaseResult

interface RemoteOrdersDataSource {

    suspend fun getOrders(userId: String) : FirebaseResult<List<Order>>

    fun getCurrentUserId(): String?

    suspend fun getOrderById(userId: String, id: String): FirebaseResult<Order?>

}