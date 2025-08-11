package com.joao01sb.shophub.features.orders.domain.usecase

import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.result.DomainResult
import com.joao01sb.shophub.features.orders.domain.repository.OrdersRepository

class GetOrderByIdUseCase(
    private val ordersRepository: OrdersRepository
) {

    suspend operator fun invoke(userId: String, orderId: String): DomainResult<Order?> {
        return when (val result = ordersRepository.getOrderById(userId, orderId)) {
            is DomainResult.Success -> result
            is DomainResult.Error -> result
        }
    }
}