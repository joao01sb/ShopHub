package com.joao01sb.shophub.core.utils

import androidx.compose.ui.graphics.Color
import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.sharedui.theme.CancelledBackgroundColor
import com.joao01sb.shophub.sharedui.theme.CancelledTextColor
import com.joao01sb.shophub.sharedui.theme.CompletedBackgroundColor
import com.joao01sb.shophub.sharedui.theme.CompletedTextColor
import com.joao01sb.shophub.sharedui.theme.PendingBackgroundColor
import com.joao01sb.shophub.sharedui.theme.PendingTextColor
import com.joao01sb.shophub.sharedui.theme.ProcessingBackgroundColor
import com.joao01sb.shophub.sharedui.theme.ProcessingTextColor
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
        OrderStatus.COMPLETED -> CompletedBackgroundColor
        OrderStatus.PENDING -> PendingBackgroundColor
        OrderStatus.PROCESSING -> ProcessingBackgroundColor
        OrderStatus.CANCELLED -> CancelledBackgroundColor
    }
}

fun getStatusTextColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.COMPLETED -> CompletedTextColor
        OrderStatus.PENDING -> PendingTextColor
        OrderStatus.PROCESSING -> ProcessingTextColor
        OrderStatus.CANCELLED -> CancelledTextColor
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