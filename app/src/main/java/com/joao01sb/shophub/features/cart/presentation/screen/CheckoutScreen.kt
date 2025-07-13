package com.joao01sb.shophub.features.cart.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.presentation.components.CardPaymentSection
import com.joao01sb.shophub.features.cart.presentation.components.ConfirmOrderButton
import com.joao01sb.shophub.features.cart.presentation.components.OrderSummarySection
import com.joao01sb.shophub.features.cart.presentation.components.PersonalDataSection

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    cartItems: List<CartItem>,
    onPlaceOrder: (CheckoutInfo) -> Unit
) {

    var checkoutInfo by remember { mutableStateOf(CheckoutInfo()) }

    val isFormValid = checkoutInfo.numberCard.isNotBlank() &&
            checkoutInfo.nameCard.isNotBlank() &&
            checkoutInfo.dateCard.isNotBlank() &&
            checkoutInfo.cvvCard.isNotBlank() &&
            checkoutInfo.fullName.isNotBlank() &&
            checkoutInfo.phoneNumber.isNotBlank()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }

        item {
            CardPaymentSection(
                checkoutInfo = checkoutInfo,
                onCheckoutInfoChange = { checkoutInfo = it }
            )
        }

        item {
            PersonalDataSection(
                checkoutInfo = checkoutInfo,
                onCheckoutInfoChange = { checkoutInfo = it }
            )
        }

        item {
            OrderSummarySection(
                cartItems = cartItems,
                freight = 0.0
            )
        }

        item {
            ConfirmOrderButton(
                onClick = { onPlaceOrder(checkoutInfo) },
                enabled = isFormValid
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}