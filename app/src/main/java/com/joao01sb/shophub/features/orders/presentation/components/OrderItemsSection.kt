package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.sharedui.theme.BorderLightGray
import com.joao01sb.shophub.sharedui.theme.PrimaryBlue
import com.joao01sb.shophub.sharedui.theme.TextDarkGray

@Composable
fun OrderItemsSection(order: Order) {
    OrderSection(
        icon = "🛍️",
        title = "Itens Order"
    ) {
        order.items.forEachIndexed { index, item ->
            ProductItem(item = item)
            if (index < order.items.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = BorderLightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = BorderLightGray)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDarkGray
            )
            Text(
                text = "$ %.2f".format(order.total),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryBlue
            )
        }
    }
}