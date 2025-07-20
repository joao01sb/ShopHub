package com.joao01sb.shophub.features.cart.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.presentation.components.CardPaymentSection
import com.joao01sb.shophub.features.cart.presentation.components.ConfirmOrderButton
import com.joao01sb.shophub.features.cart.presentation.components.OrderSummarySection
import com.joao01sb.shophub.features.cart.presentation.components.PersonalDataSection
import com.joao01sb.shophub.features.cart.presentation.state.CheckoutUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    uiState: CheckoutUiState = CheckoutUiState(),
    onChangedCardNumber: (String) -> Unit = {},
    onChangedCardHolderName: (String) -> Unit = {},
    onChangedExpirationCard: (String) -> Unit = {},
    onChangedCvv: (String) -> Unit = {},
    onChangedFullName: (String) -> Unit = {},
    onChangedPhone: (String) -> Unit = {},
    onPlaceOrder: () -> Unit,
    onBack: () -> Unit
) {

    val checkoutInfo = CheckoutInfo(
        cvvCard = uiState.cvv,
        numberCard = uiState.cardNumber,
        nameCard = uiState.cardHolderName,
        dateCard = uiState.expiryDate,
        fullName = uiState.fullName,
        phoneNumber = uiState.phone
    )

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.5f),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
    } else if (uiState.itens.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Your cart is empty")
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TopAppBarCustom(
                title = "Checkout",
                onNavigationClick = onBack,
            )

            LazyColumn(
                modifier = modifier
                    .weight(1f)
                    .background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(8.dp),
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
                        onCardNumberChanged = onChangedCardNumber,
                        onCardHolderNameChanged = onChangedCardHolderName,
                        onCardExpiration = onChangedExpirationCard,
                        onCardCVVChanged = onChangedCvv
                    )
                }

                item {
                    PersonalDataSection(
                        checkoutInfo = checkoutInfo,
                        onFullNameChanged = onChangedFullName,
                        onPhoneChanged = onChangedPhone
                    )
                }

                item {
                    OrderSummarySection(
                        cartItems = uiState.itens,
                        freight = 0.0
                    )
                }

                item {
                    ConfirmOrderButton(
                        onClick = onPlaceOrder,
                        enabled = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}

@Preview
@Composable
private fun CheckoutScreenPreview() {
    CheckoutScreen(
        modifier = Modifier,
        onPlaceOrder = {

        },
        onBack = {

        }
    )
}