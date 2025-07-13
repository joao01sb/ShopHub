package com.joao01sb.shophub.features.cart.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.features.cart.presentation.components.BottomBarCart
import com.joao01sb.shophub.features.cart.presentation.components.CartItemCard
import com.joao01sb.shophub.features.cart.presentation.state.CartUiState
import com.joao01sb.shophub.shared_ui.components.TopAppBarCustom

@Composable
fun CartScreen(
    uiState: CartUiState,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarCustom(
            title = "Shopping Cart",
            onNavigationClick = onBack
        )
        when (uiState) {
            is CartUiState.Error -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.message}"
                    )
                    Button(
                        onClick = onRetry
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
            is CartUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading..."
                    )
                }
            }
            is CartUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.cart.size) { index ->
                        val item = uiState.cart[index]
                        CartItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                onQuantityChange(newQuantity)
                            },
                            onRemoveItem = {
                                onRemoveItem()
                            }
                        )
                    }
                }
                BottomBarCart(
                    subtotal = uiState.cart.sumOf { it.precoUni * it.quantidade },
                    onFinalizarCompra = onCheckout
                )
            }
        }

    }

}