package com.joao01sb.shophub.core.domain.model

data class CartItem(
    val productId: Int = 0,
    val name: String = "",
    val quantity: Int = 0,
    val uniPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val category: String = "",
    val urlImage: String = ""
)
