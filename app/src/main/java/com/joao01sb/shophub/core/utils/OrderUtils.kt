package com.joao01sb.shophub.core.utils

import androidx.compose.ui.graphics.Color
import com.joao01sb.shophub.core.domain.enums.OrderStatus
import java.util.Locale
import java.util.Locale.getDefault

fun getCategoryIcon(category: String): String {
    return when (category.lowercase(getDefault())) {
        "eletrônicos" -> "📱"
        "áudio" -> "🎧"
        "roupas" -> "👕"
        "casa" -> "🏠"
        "livros" -> "📚"
        else -> "📦"
    }
}

fun getStatusBackgroundColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.COMPLETED -> Color(0xFFD4EDDA)
        OrderStatus.PENDING -> Color(0xFFFFF3CD)
        OrderStatus.PROCESSING -> Color(0xFFCCE7FF)
        OrderStatus.CANCELLED -> Color(0xFFF8D7DA)
    }
}

fun getStatusTextColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.COMPLETED -> Color(0xFF155724)
        OrderStatus.PENDING -> Color(0xFF856404)
        OrderStatus.PROCESSING -> Color(0xFF004085)
        OrderStatus.CANCELLED -> Color(0xFF721C24)
    }
}

fun getStatusDisplayName(status: OrderStatus): String {
    return when (status) {
        OrderStatus.COMPLETED -> "COMPLETED"
        OrderStatus.PENDING -> "PENDING"
        OrderStatus.PROCESSING -> "PROCESSING"
        OrderStatus.CANCELLED -> "CANCELLED"
    }
}