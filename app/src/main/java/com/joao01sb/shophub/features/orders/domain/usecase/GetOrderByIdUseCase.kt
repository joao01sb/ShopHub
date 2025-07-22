package com.joao01sb.shophub.features.orders.domain.usecase

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository

class GetOrderByIdUseCase(
    private val ordersRepository: OrdersRepository
) {

    suspend operator fun invoke(userId: String, orderId: String): Result<Order> {
        return try {
            val order = ordersRepository.getOrderById(userId, orderId) ?: throw NoSuchElementException("Order not found")
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}