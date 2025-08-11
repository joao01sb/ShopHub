package com.joao01sb.shophub.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(
        "dd 'de' MMMM, yyyy",
        Locale("pt", "BR")
    )
    return formatter.format(Date(timestamp))
}

fun maskCardNumber(cardNumber: String): String {
    if (cardNumber.length < 4) return cardNumber
    return "**** **** **** ${cardNumber.takeLast(4)}"
}