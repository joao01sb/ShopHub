package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.runtime.Composable
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo

@Composable
fun PersonalInfoSection(checkoutInfo: CheckoutInfo) {
    OrderSection(
        icon = "👤",
        title = "Personal Information"
    ) {
        InfoRow(label = "Full Name:", value = checkoutInfo.fullName)
        InfoRow(label = "Fone:", value = checkoutInfo.phoneNumber)
    }
}