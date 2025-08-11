package com.joao01sb.shophub.features.cart.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.utils.ConstantsFloat
import com.joao01sb.shophub.sharedui.theme.SuccessGreen

@Composable
fun OrderSummarySection(
    cartItems: List<CartItem>,
    freight: Double = 0.0,
    modifier: Modifier = Modifier
) {
    val subtotal = cartItems.sumOf { it.precoTotal }
    val total = subtotal + freight

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "ShoppingCart",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Resume checkout",
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            cartItems.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.quantidade}x ${item.nome}",
                        modifier = Modifier.weight(ConstantsFloat.const_10),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$ ${String.format("%.2f", item.precoTotal).replace('.', ',')}",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (cartItems.size > 3) {
                Text(
                    text = "... and more ${cartItems.size - 3} itens",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal (${cartItems.size} itens)",
                    )
                    Text(
                        text = "$ ${String.format("%.2f", subtotal).replace('.', ',')}",
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Freight",
                    )
                    Text(
                        text = if (freight == 0.0) "Free" else "$ ${
                            String.format("%.2f", freight).replace('.', ',')
                        }",
                        color = if (freight == 0.0) SuccessGreen else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$ ${String.format("%.2f", total).replace('.', ',')}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrderSummarySectionPreview() {
    OrderSummarySection(
        cartItems = listOf<CartItem>(
            CartItem(nome = "Item 1", precoTotal = 10.0),
            CartItem(nome = "Item 2", precoTotal = 20.0),
            CartItem(nome = "Item 3", precoTotal = 30.0),
            CartItem(nome = "Item 4", precoTotal = 40.0),
        ),
        freight = 0.0,
        modifier = Modifier.padding(16.dp)
    )
}