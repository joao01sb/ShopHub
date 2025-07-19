package com.joao01sb.shophub.features.orders.domain.usecase

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository

class GetOrdersUseCase(
    private val ordersRepository: OrdersRepository
) {

    suspend operator fun invoke(userId: String) : Result<List<Order>> {
        return try {
            val orders = ordersRepository.getOrders(userId)
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}