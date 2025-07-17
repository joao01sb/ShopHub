package com.joao01sb.shophub.core.domain.mapper

import com.joao01sb.shophub.core.domain.model.CartItem
import com.joao01sb.shophub.core.domain.model.Product

fun Product.toCartItem() = CartItem(
    idProduto = id,
    nome = name,
    quantidade = 1,
    precoUni = price,
    precoTotal = price,
    categoria = category,
    imageUrl = imageUrl ?: ""
)