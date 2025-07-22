package com.joao01sb.shophub.features.orders.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.data.mock.MockOrders
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.presentation.components.OrderItemsSection
import com.joao01sb.shophub.features.orders.presentation.components.OrderStatusSection
import com.joao01sb.shophub.features.orders.presentation.components.PaymentInfoSection
import com.joao01sb.shophub.features.orders.presentation.components.PersonalInfoSection
import com.joao01sb.shophub.features.orders.presentation.state.OrderDetailsUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Composable
fun OrderDetailsScreen(
    orderDetalsState: OrderDetailsUiState,
    onBackClick: () -> Unit = {},
    onRetry: () -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBarCustom(
            title = "Order Details",
            onNavigationClick = onBackClick
        )

        when(orderDetalsState) {
            is OrderDetailsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Error: ${orderDetalsState.message}")
                    Button(onClick = onRetry) {
                        Text(text = "Retry")
                    }
                }
            }
            is OrderDetailsUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is OrderDetailsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        OrderStatusSection(orderDetalsState.order)
                    }
                    item {
                        OrderItemsSection(orderDetalsState.order)
                    }
                    item {
                        PaymentInfoSection(orderDetalsState.order.paymentInfo)
                    }
                    item {
                        PersonalInfoSection(orderDetalsState.order.paymentInfo)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrderDetailsScreenPreview() {
    OrderDetailsScreen(
        orderDetalsState = OrderDetailsUiState.Success(
            order = MockOrders.orders.first()
        ),
        onBackClick = {},
        onRetry = {}
    )
}