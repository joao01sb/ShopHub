package com.joao01sb.shophub.core.domain.model

import com.google.firebase.firestore.PropertyName
import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Order(
    @PropertyName("id") val id: String = "",
    @PropertyName("items") val items: List<CartItem> = emptyList(),
    @PropertyName("total") val total: Double = 0.0,
    @PropertyName("status") val status: OrderStatus = OrderStatus.PENDING,
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("paymentInfo") val paymentInfo: CheckoutInfo = CheckoutInfo(),
    @PropertyName("orderNumber") val orderNumber: String = generateOrderNumber(createdAt)
) {
    companion object {
        private fun generateOrderNumber(timestamp: Long): String {
            val date = Date(timestamp)
            val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            val year = yearFormat.format(date)
            val sequentialNumber = String.format(Locale.getDefault(),"%03d", (timestamp % 1000))
            return "#ORD-$year-$sequentialNumber"
        }
    }
}
