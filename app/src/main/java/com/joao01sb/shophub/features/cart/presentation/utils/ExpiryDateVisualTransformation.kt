package com.joao01sb.shophub.features.cart.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitsOnly = text.text.filter { it.isDigit() }
        val formatted = when {
            digitsOnly.length >= 2 -> "${digitsOnly.take(2)}/${digitsOnly.drop(2).take(2)}"
            else -> digitsOnly
        }
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitsBeforeOffset = text.text.take(offset).count { it.isDigit() }
                return when {
                    digitsBeforeOffset <= 2 -> digitsBeforeOffset
                    else -> digitsBeforeOffset + 1 // +1 para a barra
                }.coerceAtMost(formatted.length)
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                val digitsCount = formatted.take(offset).count { it.isDigit() }
                return digitsCount.coerceAtMost(text.text.length)
            }
        }
        
        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = offsetMapping
        )
    }
}