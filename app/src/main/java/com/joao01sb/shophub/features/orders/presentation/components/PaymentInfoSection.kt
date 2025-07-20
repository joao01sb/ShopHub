package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.utils.maskCardNumber
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

@Composable
fun PaymentInfoSection(paymentInfo: CheckoutInfo) {
    OrderSection(
        icon = "💳",
        title = "Information Payment"
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Column {
                PaymentInfoRow(
                    label = "Card:",
                    value = maskCardNumber(paymentInfo.numberCard)
                )
                PaymentInfoRow(
                    label = "Holder Name:",
                    value = paymentInfo.nameCard
                )
                PaymentInfoRow(
                    label = "Method:",
                    value = "Card Credit",
                    isLast = true
                )
            }
        }
    }
}