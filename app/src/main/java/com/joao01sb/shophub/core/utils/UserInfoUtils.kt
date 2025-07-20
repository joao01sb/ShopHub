package com.joao01sb.shophub.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatCardNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return digitsOnly.chunked(4).joinToString(" ").take(19)
}

fun formatCardDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length >= 2 -> "${digitsOnly.take(2)}/${digitsOnly.drop(2).take(2)}"
        else -> digitsOnly
    }
}

fun formatPhoneNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length >= 11 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2).take(5)}-${digitsOnly.drop(7).take(4)}"
        digitsOnly.length >= 7 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2).take(4)}-${digitsOnly.drop(6)}"
        digitsOnly.length >= 3 -> "(${digitsOnly.take(2)}) ${digitsOnly.drop(2)}"
        else -> digitsOnly
    }
}

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale("pt", "BR"))
    return formatter.format(Date(timestamp))
}

fun maskCardNumber(cardNumber: String): String {
    if (cardNumber.length < 4) return cardNumber
    return "**** **** **** ${cardNumber.takeLast(4)}"
}