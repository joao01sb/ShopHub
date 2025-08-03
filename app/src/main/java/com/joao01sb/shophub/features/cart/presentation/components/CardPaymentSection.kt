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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao01sb.shophub.features.cart.domain.model.CheckoutInfo
import com.joao01sb.shophub.features.cart.presentation.utils.CardNumberVisualTransformation
import com.joao01sb.shophub.features.cart.presentation.utils.ExpiryDateVisualTransformation

@Composable
fun CardPaymentSection(
    onCardNumberChanged: (String) -> Unit,
    onCardHolderNameChanged: (String) -> Unit,
    onCardExpiration: (String) -> Unit,
    onCardCVVChanged: (String) -> Unit,
    checkoutInfo: CheckoutInfo,
    modifier: Modifier = Modifier
) {
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
                    imageVector = Icons.Outlined.AccountBox,
                    contentDescription = "credit card",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Card Details",
                    fontWeight = FontWeight.Bold,
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = checkoutInfo.numberCard,
                onValueChange = { newValue: String ->
                    val digitsOnly = newValue.filter { it.isDigit() }.take(16)
                    onCardNumberChanged(digitsOnly)
                },
                label = { Text("Card number") },
                placeholder = { Text("1234 5678 9012 3456") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = CardNumberVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Card",
                    )
                },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = checkoutInfo.nameCard,
                onValueChange = onCardHolderNameChanged,
                label = { Text("Name on Card") },
                placeholder = { Text("JOÃO SILVA") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = checkoutInfo.dateCard,
                    onValueChange = { newValue: String ->
                        val digitsOnly = newValue.filter { it.isDigit() }.take(4)
                        onCardExpiration(digitsOnly)
                    },
                    label = { Text("Validity") },
                    placeholder = { Text("MM/AA") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = ExpiryDateVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                        )
                    },
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = checkoutInfo.cvvCard,
                    onValueChange = { newValue: String ->
                        if (newValue.length <= 3) {
                            onCardCVVChanged(newValue)
                        }
                    },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Security",
                        )
                    },
                    singleLine = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardPaymentSectionPreview() {
    CardPaymentSection(
        onCardExpiration = {},
        onCardCVVChanged = {},
        onCardNumberChanged = {},
        onCardHolderNameChanged = {},
        checkoutInfo = CheckoutInfo(
            fullName = "João Silva",
            phoneNumber = "(11) 99999-9999",
            numberCard = "1234 5678 9012 3456",
            nameCard = "JOÃO SILVA",
            dateCard = "12/25",
            cvvCard = "123"
        )
    )
}