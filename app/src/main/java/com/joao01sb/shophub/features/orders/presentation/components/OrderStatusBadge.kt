package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.enums.OrderStatus

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        OrderStatus.COMPLETED -> Triple(Color(0xFFD4EDDA), Color(0xFF155724), "COMPLETED")
        OrderStatus.PENDING -> Triple(Color(0xFFFFF3CD), Color(0xFF856404), "PENDING")
        OrderStatus.PROCESSING -> Triple(Color(0xFFCCE7FF), Color(0xFF004085), "PROCESSING")
        OrderStatus.CANCELLED -> Triple(Color(0xFFF8D7DA), Color(0xFF721C24), "CANCELLED")
    }
    
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}