package com.joao01sb.shophub.features.orders.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.data.mock.MockOrders
import com.joao01sb.shophub.core.domain.enums.OrderFilter
import com.joao01sb.shophub.core.domain.enums.OrderStatus
import com.joao01sb.shophub.core.domain.model.Order
import com.joao01sb.shophub.features.orders.presentation.components.OrderCard
import com.joao01sb.shophub.features.orders.presentation.components.OrderFilterTabs
import com.joao01sb.shophub.features.orders.presentation.state.OrdersUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Composable
fun OrdersScreen(
    orderUiState: OrdersUiState,
    onOrderClick: (Order) -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit = {}
) {

    when (orderUiState) {
        is OrdersUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${orderUiState.message}",
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry ) {
                        Text(text = "Retry")
                    }
                }
            }
        }

        is OrdersUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    Modifier.padding(16.dp)
                )
            }
        }

        is OrdersUiState.Success -> {

            var selectedFilter by remember { mutableStateOf(OrderFilter.ALL) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
            ) {
                TopAppBarCustom(
                    title = "My Orders",
                    onNavigationClick = onBackClick
                )

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    OrderFilterTabs(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val filteredOrders = when (selectedFilter) {
                        OrderFilter.ALL -> orderUiState.orders
                        OrderFilter.COMPLETED -> orderUiState.orders.filter { it.status == OrderStatus.COMPLETED }
                        OrderFilter.PENDING -> orderUiState.orders.filter {
                            it.status == OrderStatus.PENDING || it.status == OrderStatus.PROCESSING
                        }
                    }

                    if (filteredOrders.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No orders found.")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredOrders) { order ->
                                OrderCard(
                                    order = order,
                                    onClick = { onOrderClick(order) }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}


@Preview
@Composable
private fun OrdersScreenPreview() {
    OrdersScreen(
        orderUiState = OrdersUiState.Success(
            orders = MockOrders.orders
        ),
        onOrderClick = {  },
        onBackClick = {  }
    )
}