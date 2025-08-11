package com.joao01sb.shophub.features.cart.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitsOnly = text.text.filter { it.isDigit() }
        val formatted = digitsOnly.chunked(4).joinToString(" ").take(19)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitsBeforeOffset = text.text.take(offset).count { it.isDigit() }
                val spacesBeforeOffset = (digitsBeforeOffset - 1) / 4
                return (digitsBeforeOffset + spacesBeforeOffset).coerceAtMost(formatted.length)
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