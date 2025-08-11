package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.sharedui.theme.CompletedBackgroundColor
import com.joao01sb.shophub.sharedui.theme.PendingBackgroundColor
import com.joao01sb.shophub.sharedui.theme.ProcessingBackgroundColor
import com.joao01sb.shophub.sharedui.theme.CancelledBackgroundColor
import com.joao01sb.shophub.sharedui.theme.CompletedTextColor
import com.joao01sb.shophub.sharedui.theme.PendingTextColor
import com.joao01sb.shophub.sharedui.theme.ProcessingTextColor
import com.joao01sb.shophub.sharedui.theme.CancelledTextColor

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        OrderStatus.COMPLETED -> Triple(CompletedBackgroundColor, CompletedTextColor, "COMPLETED")
        OrderStatus.PENDING -> Triple(PendingBackgroundColor, PendingTextColor, "PENDING")
        OrderStatus.PROCESSING -> Triple(ProcessingBackgroundColor, ProcessingTextColor, "PROCESSING")
        OrderStatus.CANCELLED -> Triple(CancelledBackgroundColor, CancelledTextColor, "CANCELLED")
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