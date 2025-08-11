package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.core.utils.formatDate
import com.joao01sb.shophub.core.utils.getStatusBackgroundColor
import com.joao01sb.shophub.core.utils.getStatusDisplayName
import com.joao01sb.shophub.core.utils.getStatusTextColor

@Composable
fun OrderStatusSection(order: Order) {
    OrderSection(
        icon = "📦",
        title = "Stats Order"
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    getStatusBackgroundColor(order.status),
                    RoundedCornerShape(25.dp)
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getStatusDisplayName(order.status),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = getStatusTextColor(order.status)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoRow(label = "Order:", value = order.orderNumber)
        InfoRow(label = "Date:", value = formatDate(order.createdAt))
    }
}