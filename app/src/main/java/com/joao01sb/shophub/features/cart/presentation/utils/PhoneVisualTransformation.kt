package com.joao01sb.shophub.features.cart.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitsOnly = text.text.filter { it.isDigit() }
        val formatted = when {
            digitsOnly.length >= 11 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2).take(5)}-${digitsOnly.drop(7).take(4)}"
            digitsOnly.length >= 7 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2).take(4)}-${digitsOnly.drop(6)}"
            digitsOnly.length >= 3 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2)}"
            else -> digitsOnly
        }
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitsBeforeOffset = text.text.take(offset).count { it.isDigit() }
                var transformedOffset = digitsBeforeOffset
                
                if (digitsBeforeOffset > 0) transformedOffset += 1
                if (digitsBeforeOffset > 2) transformedOffset += 2
                if (digitsBeforeOffset > 7) transformedOffset += 1
                
                return transformedOffset.coerceAtMost(formatted.length)
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
