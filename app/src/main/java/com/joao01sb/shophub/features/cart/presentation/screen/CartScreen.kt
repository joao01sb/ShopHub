package com.joao01sb.shophub.features.cart.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.utils.ConstantsFloat
import com.joao01sb.shophub.features.cart.presentation.components.BottomBarCart
import com.joao01sb.shophub.features.cart.presentation.components.CartItemCard
import com.joao01sb.shophub.features.cart.presentation.state.CartUiState
import com.joao01sb.shophub.sharedui.components.TopAppBarCustom

@Composable
fun CartScreen(
    uiState: CartUiState,
    onQuantityChange: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
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
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(
                            text = "Error: ${uiState.message}"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onRetry
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
            is CartUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading..."
                    )
                }
            }
            is CartUiState.Success -> {
                if (uiState.cart.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your cart is empty"
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(ConstantsFloat.const_10),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.cart.size) { index ->
                            val item = uiState.cart[index]
                            CartItemCard(
                                item = item,
                                onQuantityChange = { newQuantity ->
                                    onQuantityChange(item, newQuantity)
                                },
                                onRemoveItem = {
                                    onRemoveItem(item)
                                }
                            )
                        }
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