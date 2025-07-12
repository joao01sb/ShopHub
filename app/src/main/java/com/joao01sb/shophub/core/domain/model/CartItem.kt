package com.joao01sb.shophub.core.domain.model

data class CartItem(
    val idProduto: Int = 0,
    val nome: String = "",
    val quantidade: Int = 0,
    val precoUni: Double = 0.0,
    val precoTotal: Double = 0.0,
    val categoria: String = "",
    val imageUrl: String = ""
)
