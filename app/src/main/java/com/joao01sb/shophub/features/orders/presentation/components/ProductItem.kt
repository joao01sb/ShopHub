package com.joao01sb.shophub.features.orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.utils.ConstantsFloat
import com.joao01sb.shophub.core.utils.getCategoryIcon
import com.joao01sb.shophub.sharedui.theme.BlueGradientLight
import com.joao01sb.shophub.sharedui.theme.PrimaryBlue
import com.joao01sb.shophub.sharedui.theme.TextDarkGray
import com.joao01sb.shophub.sharedui.theme.TextGray

@Composable
fun ProductItem(item: CartItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(BlueGradientLight, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getCategoryIcon(item.categoria),
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(ConstantsFloat.const_10)) {
            Text(
                text = item.nome,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDarkGray
            )

            Text(
                text = item.categoria,
                fontSize = 12.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Qtd: ${item.quantidade}",
                    fontSize = 14.sp,
                    color = TextGray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$ %.2f".format(item.precoUni * item.quantidade),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlue
                )
            }
        }
    }
}